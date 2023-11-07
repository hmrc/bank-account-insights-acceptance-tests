package uk.gov.hmrc.test.api.specs

//import io.swagger.v3.parser.OpenAPIV3Parser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.parser.OpenAPIV3Parser
import io.swagger.v3.parser.core.models.ParseOptions
import org.openapi4j.schema.validator.v3.SchemaValidator
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import uk.gov.hmrc.test.api.conf.TestConfiguration

import scala.jdk.StreamConverters._

class SwaggerExampleSpec extends AnyWordSpec {

  val bankAccountGatewayUserAgent = "bank-account-gateway"
  val host: String = TestConfiguration.url("bank-account-gateway")
  val openApiUrl: String = s"$host/api/conf/1.0/application.yaml"

  "Open API spec" should {
    "parse" in {
      val parseOptions = new ParseOptions();
      parseOptions.setResolve(true); // implicit
      parseOptions.setResolveFully(true);


      val x = new OpenAPIV3Parser().read(openApiUrl, null, parseOptions)
      val o = new ObjectMapper()

      x.getPaths.forEach { case (path, p) =>

        val post = p.getPost
        val request = post.getRequestBody


        request.getContent.forEach { case (contentType, c) =>

          val sc = c.getSchema
          val j = o.writeValueAsString(sc)

          val jsonNode: JsonNode = o.readTree(j);
          val v = new SchemaValidator(null, jsonNode)

          Option(c.getExamples).map { examples =>
            val es = c.getExamples.entrySet().stream().map(e => e.getKey -> e.getValue).toScala[Seq[(String, Example)]](Seq)
            es.map { case (name, example) =>

              v.validate(null)
            }
          }

          c.getExamples.forEach {case (name, e) =>

            e

          }



        }

      }

      x.getServers.size shouldBe 2




    }
  }




}
