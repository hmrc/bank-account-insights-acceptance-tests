#!/bin/bash -e

# remove any existing Postgres container and start a new one
docker rm -f postgres
docker run --name postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -d -p 5432:5432 postgres

sm2 --start BANK_ACCOUNT_INSIGHTS_PROXY BANK_ACCOUNT_GATEWAY BANK_ACCOUNT_INSIGHTS INTERNAL_AUTH --appendArgs \
    '{
      "BANK_ACCOUNT_INSIGHTS_PROXY": [
        "-Dmicroservice.services.access-control.enabled=true",
        "-Dmicroservice.services.access-control.allow-list.0=bank-account-gateway",
        "-Dmicroservice.services.access-control.allow-list.1=allowed-test-hmrc-service",
        "-Dapplication.router=testOnlyDoNotUseInAppConf.Routes"
      ],
      "BANK_ACCOUNT_INSIGHTS": [
        "-Dauditing.consumer.baseUri.port=6001",
        "-Dauditing.consumer.baseUri.host=localhost",
        "-Dauditing.enabled=true",
        "-Ddb.default.use-canned-data=true"
      ]
    }'
