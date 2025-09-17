import sbt._

object Dependencies {

  private val doobieVersion = "0.13.4"

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"                  %% "api-test-runner"          % "0.10.0",
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-test-play-30"  % "2.7.0",
    "org.wiremock"                  % "wiremock"                 % "3.13.1",
    "org.tpolecat"                 %% "doobie-core"              % doobieVersion,
    "org.tpolecat"                 %% "doobie-postgres"          % doobieVersion,
    "org.tpolecat"                 %% "doobie-scalatest"         % doobieVersion,
    "org.typelevel"                %% "cats-core"                % "2.13.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"     % "2.19.2",
    "io.swagger.parser.v3"          % "swagger-parser"           % "2.1.31",
    "org.openapi4j"                 % "openapi-schema-validator" % "1.0.7"
  ).map(_ % Test)
}
