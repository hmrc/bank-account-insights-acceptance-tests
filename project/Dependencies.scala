import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "org.scalatest"                %% "scalatest"                % "3.2.19" % Test,
    "com.vladsch.flexmark"          % "flexmark-all"             % "0.62.2" % Test,
    "com.typesafe"                  % "config"                   % "1.4.3"  % Test,
    "com.typesafe.play"            %% "play-ahc-ws-standalone"   % "2.2.9"  % Test,
    "org.slf4j"                     % "slf4j-simple"             % "2.0.13" % Test,
    "com.typesafe.play"            %% "play-ws-standalone-json"  % "2.2.9"  % Test,
    "com.github.tomakehurst"        % "wiremock"                 % "2.27.2" % Test,
    "org.assertj"                   % "assertj-core"             % "3.26.3" % Test,
    "uk.gov.hmrc"                  %% "attribute-risk-lists"     % "0.+"    % Test,
    "org.tpolecat"                 %% "doobie-core"              % "0.13.4" % Test,
    "org.tpolecat"                 %% "doobie-postgres"          % "0.13.4" % Test,
    "org.tpolecat"                 %% "doobie-scalatest"         % "0.13.4" % Test,
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-test-play-29"  % "2.2.0"  % Test,
    "org.typelevel"                %% "cats-core"                % "2.3.1"  % Test,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"     % "2.17.2" % Test,
    "io.swagger.parser.v3"          % "swagger-parser"           % "2.1.21" % Test,
    "org.openapi4j"                 % "openapi-schema-validator" % "1.0.7"  % Test
  )
}
