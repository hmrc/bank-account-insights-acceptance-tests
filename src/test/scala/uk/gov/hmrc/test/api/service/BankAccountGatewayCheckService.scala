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

import play.api.libs.ws.StandaloneWSResponse
import uk.gov.hmrc.test.api.client.HttpClientHelper
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints
import uk.gov.hmrc.test.api.models.request.InsightsRequest
import uk.gov.hmrc.test.api.models.request.InsightsRequest.implicits.bankAccountInsightsRequestWrites

class BankAccountGatewayCheckService extends HttpClientHelper {
  val host: String            = TestConfiguration.url("bank-account-gateway")
  val checkAccountURL: String = s"$host/${Endpoints.CHECK_INSIGHTS}"
  val openApiUrl: String      = s"$host/api/conf/1.0/application.yaml"
  val userAgentOne            = "bank-account-gateway"
  val userAgentTwo            = "allowed-test-hmrc-service"

  def postBankAccountGatewayCheck(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("User-Agent", "allowed-test-hmrc-service")
    )

  def postBankAccountGatewayCheckByMultipleUserAgentHeaders(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("User-Agent", s"$userAgentOne"),
      ("User-Agent", s"$userAgentTwo")
    )

  def postBankAccountGatewayCheckByMultipleUserAgentValuesInOneHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("User-Agent", s"$userAgentOne,$userAgentTwo")
    )

  def postBankAccountGatewayCheckByByOriginatorIdHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("OriginatorId", s"$userAgentOne")
    )

  def postInvalidBankAccountGatewayCheck(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json")
    )

  def postInvalidBankAccountGatewayCheckByInvalidHeader(
    accountDetails: InsightsRequest
  ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("OriginatorId", "Invalid OriginatorID")
    )
}
