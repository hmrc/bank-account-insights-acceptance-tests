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

package uk.gov.hmrc.test.api.specs

import com.github.tomakehurst.wiremock.client.WireMock.{matchingJsonPath, postRequestedFor, urlEqualTo, verify}
import org.assertj.core.api.Assertions.assertThat
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.models.response.BankAccountInsightsResponse
import uk.gov.hmrc.test.api.models.response.risklist_response_codes.{ACCOUNT_NOT_ON_WATCH_LIST, ACCOUNT_ON_WATCH_LIST}
import uk.gov.hmrc.test.api.testdata.BankAccounts.{RISKY_ACCOUNT, UNKNOWN_ACCOUNT}

import scala.concurrent.duration.{DurationInt}

class BankAccountInsightsDirectSpec extends BaseSpec with WireMockTrait {

  Feature("Check the bank account insights API directly") {

    Scenario("Get risk information for a bank account on the risk list") {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsDirect(RISKY_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)

      verifyAudit(actual, TestConfiguration, RISKY_ACCOUNT.accountNumber, RISKY_ACCOUNT.sortCode)
    }

    Scenario("Get risk information for an UNKNOWN bank account") {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsDirect(UNKNOWN_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(0)
      assertThat(actual.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)

      verifyAudit(actual, TestConfiguration, UNKNOWN_ACCOUNT.accountNumber, UNKNOWN_ACCOUNT.sortCode)
    }
  }

  private def verifyAudit(
    actual: BankAccountInsightsResponse,
    testConfiguration: TestConfiguration.type,
    accountNumber: String,
    sortCode: String
  ): Unit =
    verify(
      delayedFunction(1.seconds)(
        postRequestedFor(urlEqualTo("/write/audit"))
          .withRequestBody(
            matchingJsonPath(
              "$[?(" +
                s"@.auditSource == '${testConfiguration.expectedServiceName}'" +
                "&& @.auditType == 'BankAccountInsightsLookup'" +
                s"&& @.detail.accountNumber == '$accountNumber'" +
                s"&& @.detail.sortCode == '$sortCode'" +
                s"&& @.detail.correlationId == '${actual.correlationId}'" +
                s"&& @.detail.riskScore == '${actual.riskScore}'" +
                s"&& @.detail.reason == '${actual.reason}'" +
                s"&& @.detail.userAgent == '${testConfiguration.userAgent}'" +
                ")]"
            )
          )
      )
    )
}
