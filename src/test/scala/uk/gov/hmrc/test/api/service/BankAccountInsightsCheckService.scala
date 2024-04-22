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

class BankAccountInsightsCheckService extends HttpClient {
  val hostInsightsDirect: String    = TestConfiguration.url("bank-account-insights")
  val checkAccountDirectURL: String = s"$hostInsightsDirect/${Endpoints.CHECK_INSIGHTS}"

  def postInsightsCheck(
    checkAccountURL: String,
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

  def postInsightsCheckDirect(
    accountDetails: InsightsRequest
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        checkAccountDirectURL,
        bankAccountInsightsRequestWrites.writes(accountDetails).toString(),
        ("Content-Type", "application/json"),
        ("User-Agent", "allowed-test-hmrc-service"),
        ("Authorization", "Basic YmFuay1hY2NvdW50LWluc2lnaHRzLXByb3h5OmxvY2FsLXRlc3QtdG9rZW4=")
      ),
      10.seconds
    )

  def postInsightsInvalidCheck(
    checkAccountURL: String,
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
}
