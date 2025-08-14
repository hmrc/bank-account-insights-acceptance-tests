#!/usr/bin/env bash
ENV=$1
SPECS=${2:-"uk.gov.hmrc.test.api.db.* testOnly uk.gov.hmrc.test.api.specs.*"}

# Scalafmt checks have been separated from the test command to avoid OutOfMemoryError in Jenkins
sbt scalafmtCheckAll scalafmtSbtCheck
sbt -Denvironment=${ENV:=local} "testOnly ${SPECS}"
