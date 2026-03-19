import sbt._

object Dependencies {

  private val doobieVersion = "1.0.0-RC12"

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "api-test-runner"          % "0.10.0",
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-test-play-30"  % "2.12.0",
    "org.wiremock"                  % "wiremock"                 % "3.13.2",
    "org.tpolecat"                 %% "doobie-core"              % doobieVersion,
    "org.tpolecat"                 %% "doobie-postgres"          % doobieVersion,
    "org.tpolecat"                 %% "doobie-scalatest"         % doobieVersion,
    "org.typelevel"                %% "cats-core"                % "2.13.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"     % "2.21.1",
    "io.swagger.parser.v3"          % "swagger-parser"           % "2.1.39",
    "org.openapi4j"                 % "openapi-schema-validator" % "1.0.7"
  ).map(_ % Test)
}
