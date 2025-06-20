
# bank-account-insights-acceptance-tests

This repository contains acceptance tests for the Bank Account Insights service.

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").

# Running the tests

Prior to executing the tests ensure you have:

- Installed/configured [service manager](https://github.com/hmrc/service-manager).

## Start the local services

If you don't have postgres installed locally you can run it in docker using the following command

    docker run -d --rm --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:10.14



```shell
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
        "-Dmicroservice.risk-lists.database.use-canned-data=true",
        "-Dmicroservice.bank-account-insights.database.use-canned-data=true"
      ]
    }'
```
## Running specs

Execute the `run_specs.sh` script:

`./run-specs.sh`