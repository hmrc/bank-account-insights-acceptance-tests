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

class BankAccountInsightsCheckService extends HttpClientHelper {
  val hostInsightsDirect: String    = TestConfiguration.url("bank-account-insights")
  val checkAccountDirectURL: String = s"$hostInsightsDirect/${Endpoints.CHECK_INSIGHTS}"

  def postInsightsCheck(
                         checkAccountURL: String,
                         accountDetails: InsightsRequest
                       ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("User-Agent", "allowed-test-hmrc-service")
    )

  def postInsightsCheckDirect(
                               accountDetails: InsightsRequest
                             ): StandaloneWSResponse =
    post(
      checkAccountDirectURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json"),
      ("User-Agent", "allowed-test-hmrc-service"),
      ("Authorization", "Basic YmFuay1hY2NvdW50LWluc2lnaHRzLXByb3h5OmxvY2FsLXRlc3QtdG9rZW4=")
    )

  def postInsightsInvalidCheck(
                                checkAccountURL: String,
                                accountDetails: InsightsRequest
                              ): StandaloneWSResponse =
    post(
      checkAccountURL,
      bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
      ("Content-Type", "application/json")
    )
}
