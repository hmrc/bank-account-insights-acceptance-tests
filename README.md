
# bank-account-insights-acceptance-tests

This is a placeholder README.md for a new repository

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").


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
