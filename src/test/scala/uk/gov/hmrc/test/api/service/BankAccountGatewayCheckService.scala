/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.api.service

import play.api.libs.ws.StandaloneWSRequest
import uk.gov.hmrc.test.api.models.request.InsightsRequest
import uk.gov.hmrc.test.api.models.request.InsightsRequest.implicits.bankAccountInsightsRequestWrites
import uk.gov.hmrc.test.api.client.HttpClient
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints

import scala.concurrent.Await
import scala.concurrent.duration._

class BankAccountGatewayCheckService extends HttpClient {
  val host: String            = TestConfiguration.url("bank-account-gateway")
  val checkAccountURL: String = s"$host/${Endpoints.CHECK_INSIGHTS}"
  val openApiUrl: String      = s"$host/api/conf/1.0/application.yaml"
  val userAgentOne            = "bank-account-gateway"
  val userAgentTwo            = "allowed-test-hmrc-service"

  def getOpenApiSpec(): StandaloneWSRequest#Self#Response =
    Await.result(
      get(
        openApiUrl,
        ("Content-Type", "application/json"),
        ("User-Agent", "allowed-test-hmrc-service")
      ),
      10.seconds
    )

  def postBankAccountGatewayCheck(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("User-Agent", "allowed-test-hmrc-service")
      ),
      10.seconds
    )

  def postBankAccountGatewayCheckByMultipleUserAgentHeaders(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("User-Agent", s"$userAgentOne"),
        ("User-Agent", s"$userAgentTwo")
      ),
      10.seconds
    )

  def postBankAccountGatewayCheckByMultipleUserAgentValuesInOneHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("User-Agent", s"$userAgentOne,$userAgentTwo")
      ),
      10.seconds
    )

  def postBankAccountGatewayCheckByByOriginatorIdHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("OriginatorId", s"$userAgentOne")
      ),
      10.seconds
    )

  def postInvalidBankAccountGatewayCheck(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json")
      ),
      10.seconds
    )

  def postInvalidBankAccountGatewayCheckByInvalidHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("OriginatorId", "Invalid OriginatorID")
      ),
      10.seconds
    )
}
