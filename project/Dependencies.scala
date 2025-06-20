import sbt._

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-test-play-30"  % "2.6.0"  % Test,
    "org.playframework"            %% "play-ahc-ws-standalone"   % "3.0.7"  % Test,
    "org.playframework"            %% "play-ws-standalone-json"  % "3.0.7"  % Test,
    "org.scalatest"                %% "scalatest"                % "3.2.19" % Test,
    "com.vladsch.flexmark"          % "flexmark-all"             % "0.64.8" % Test,
    "com.typesafe"                  % "config"                   % "1.4.3"  % Test,
    "org.slf4j"                     % "slf4j-simple"             % "2.0.17" % Test,
    "com.github.tomakehurst"        % "wiremock"                 % "3.0.1"  % Test,
    "org.assertj"                   % "assertj-core"             % "3.27.3" % Test,
    "org.tpolecat"                 %% "doobie-core"              % "0.13.4" % Test,
    "org.tpolecat"                 %% "doobie-postgres"          % "0.13.4" % Test,
    "org.tpolecat"                 %% "doobie-scalatest"         % "0.13.4" % Test,
    "org.typelevel"                %% "cats-core"                % "2.13.0" % Test,
    "com.fasterxml.jackson.module" %% "jackson-module-scala"     % "2.19.1" % Test,
    "io.swagger.parser.v3"          % "swagger-parser"           % "2.1.29" % Test,
    "org.openapi4j"                 % "openapi-schema-validator" % "1.0.7"  % Test
  )
}
