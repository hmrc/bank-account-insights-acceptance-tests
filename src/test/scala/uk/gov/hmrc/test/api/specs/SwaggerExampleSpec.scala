package uk.gov.hmrc.test.api.specs

//import io.swagger.v3.parser.OpenAPIV3Parser

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.{Content, MediaType}
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import org.openapi4j.schema.validator.ValidationData
import org.openapi4j.schema.validator.v3.SchemaValidator
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.test.api.client.HttpClient
import uk.gov.hmrc.test.api.conf.TestConfiguration

import java.util.stream.{Collectors, Stream => JStream}
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.jdk.StreamConverters._
import scala.jdk.CollectionConverters._

class SwaggerExampleSpec extends AnyWordSpec {

  val bankAccountGatewayUserAgent = "bank-account-gateway"
  val host: String = TestConfiguration.url("bank-account-gateway")
  val openApiUrl: String = s"$host/api/conf/1.0/application.yaml"

  val applicationJson = "application/json"

  val mapper = new ObjectMapper()
  mapper.setSerializationInclusion(Include.NON_NULL);

  val parseOptions = new ParseOptions();
  parseOptions.setResolve(true); // implicit
  parseOptions.setResolveFully(true);

  val client = new HttpClient() {}

  "Open API spec" should {
    "parse" in {
      val result = new OpenAPIV3Parser().readLocation(openApiUrl, null, parseOptions)
      result.getMessages.size() shouldBe 0
    }
  }

  "Open API examples" should {
    "be valid according to the schema" in {
      val openApi = new OpenAPIV3Parser().read(openApiUrl, null, parseOptions)

      openApi.getPaths.forEach { case (path, p) =>
        val verbs = Option(p.getGet) ++ Option(p.getPost)
        val requests = verbs.flatMap(r => r.getRequestBody.getContent.values().stream().toScala[Seq[MediaType]](Seq))
        val responses = verbs
          .flatMap(r => r.getResponses.values().stream().flatMap[MediaType](c => c.getContent.values().stream())
            .toScala[Seq[MediaType]](Seq))

        (requests ++ responses).foreach { r: MediaType =>
          val json = mapper.writeValueAsString(r.getSchema)
          val validator = new SchemaValidator(null, mapper.readTree(json))

          val example = Option(r.getExample)
          val examples = Option(r.getExamples).map(_.values().stream().toScala(Seq)).getOrElse(Seq()).map(_.getValue)

          (example ++ examples).foreach { e =>
            val vd = new ValidationData()
            validator.validate(e.asInstanceOf[JsonNode], vd)

            vd.isValid shouldBe true
          }
        }
      }
    }

    "succeed as requests to the running service and return valid responses" in {
      val openApi = new OpenAPIV3Parser().read(openApiUrl, null, parseOptions)

      openApi.getPaths.forEach { case (path, p) =>
        val verbs = Option(p.getGet).map("GET" -> _) ++ Option(p.getPost).map("POST" -> _)

        val requests = verbs.map { case (verb, r) =>
          val request = r.getRequestBody.getContent.get(applicationJson)
          val responses = r.getResponses.entrySet().stream().map(e => {
            e.getKey -> e.getValue.getContent.get(applicationJson)
          }).toScala(Map)

          verb -> (request, responses)
        }

        requests.foreach { case (verb, (request, responses)) =>
          val example = Option(request.getExample)
          val examples = Option(request.getExamples).map(_.values().stream().toScala(Seq)).getOrElse(Seq()).map(_.getValue)

          (example ++ examples).foreach { e =>
            val headers = Seq("Content-Type"-> applicationJson, "User-Agent" -> "allowed-test-hmrc-service")
            val req = verb match {
              case "GET" => client.get(s"$host$path", headers: _*)
              case "POST" =>
                client.post(s"$host$path", mapper.writeValueAsString(e.asInstanceOf[JsonNode]), headers: _*)
            }

            val response = Await.result(req, 10.seconds)

            Option(responses(response.status.toString)).foreach { r =>
              val json = mapper.writeValueAsString(r.getSchema)
              val validator = new SchemaValidator(null, mapper.readTree(json))

              val vd = new ValidationData()
              validator.validate(mapper.readTree(response.body), vd)

              vd.isValid shouldBe true
            }
          }
        }
      }
    }


  }


}
