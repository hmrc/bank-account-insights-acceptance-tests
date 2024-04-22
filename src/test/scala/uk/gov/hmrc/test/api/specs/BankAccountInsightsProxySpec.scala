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

import org.assertj.core.api.Assertions.assertThat
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints
import uk.gov.hmrc.test.api.models.response.risklist_response_codes.{ACCOUNT_NOT_ON_WATCH_LIST, ACCOUNT_ON_WATCH_LIST}
import uk.gov.hmrc.test.api.testdata.BankAccounts.{RISKY_ACCOUNT, UNKNOWN_ACCOUNT}

class BankAccountInsightsProxySpec extends BaseSpec {
  val host: String                     = TestConfiguration.url("bank-account-insights-proxy")
  val checkAccountURL: String          = s"$host/${Endpoints.CHECK_INSIGHTS}"
  val checkAccountURLWithRoute: String = s"$host/${Endpoints.CHECK_INSIGHTS_WITH_ROUTE}"

  Feature("Check the bank account insights API through proxy") {

    Scenario("Get risk information for an UNKNOWN bank account using check/insights route") {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsProxy(checkAccountURL, UNKNOWN_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(0)
      assertThat(actual.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
    }

    Scenario("Get risk information for a bank account on the risk list using \"check/insights\" route") {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsProxy(checkAccountURL, RISKY_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }

    Scenario(
      "Get risk information for an UNKNOWN bank account using \"bank-account-insights/check/insights\" route"
    ) {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsProxy(checkAccountURL, UNKNOWN_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(0)
      assertThat(actual.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
    }

    Scenario(
      "Get risk information for a bank account on the risk list using \"bank-account-insights/check/insights\" route"
    ) {
      Given("I want to see if we hold any risk information for a bank account")

      When("I use the check insights API to see what information we hold")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromInsightsProxy(checkAccountURL, RISKY_ACCOUNT)

      Then("I am given the relevant risk information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }
  }
}
