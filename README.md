
# bank-account-insights-acceptance-tests

This repository contains acceptance tests for the Bank Account Insights service built using the [api-test-runner](https://github.com/hmrc/api-test-runner) library.

## Running the tests

Prior to executing the tests ensure you have:

- Installed/configured [sm2 (service manager 2)](https://github.com/hmrc/sm2).
- Postgres DB installed locally or running in Docker.

### Start the local services

If you don't have postgres installed locally you can run it in docker using the following command

    docker run -d --rm --name postgresql -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -p 5432:5432 postgres:10.14

Start the dependent services by running the `.start_services.sh` script. Alternative, you can run the command below manually:

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
### Running specs

Execute the `run_specs.sh` script:

`./run-specs.sh`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").