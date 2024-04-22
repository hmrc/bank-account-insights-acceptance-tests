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
import uk.gov.hmrc.test.api.testdata.ApiErrors.UNAUTHORISED
import uk.gov.hmrc.test.api.testdata.BankAccounts.{RISKY_ACCOUNT, UNKNOWN_ACCOUNT}

class BankAccountGatewaySpec extends BaseSpec {

  val bankAccountGatewayUserAgent = "bank-account-gateway"
  val host: String                = TestConfiguration.url("bank-account-insights-proxy")
  val checkAccountURL: String     = s"$host/${Endpoints.CHECK_INSIGHTS}"

  Feature("Check the bank account gateway API") {

    Scenario("Get risking information for an UNKNOWN bank account") {
      Given("I want to see if we hold any risking information for an unknown bank account")

      When("I use the check insights API via bank account gateway to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromGateway(UNKNOWN_ACCOUNT)

      Then("I am given the relevant risking information")
      assertThat(actual.riskScore).isEqualTo(0)
      assertThat(actual.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
    }

    Scenario("Get risking information for a bank account on the risk list using a single User-Agent header") {
      Given("I want to see if we hold any risking information for a bank account using a single User-Agent header")

      When("I use the check insights API via bank account gateway to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromGateway(RISKY_ACCOUNT)

      Then("I am given the relevant risking information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }

    Scenario("Get risking information for a bank account on the risk list using multiple User-Agent headers") {
      Given("I want to see if we hold any risking information for a bank account using multiple User-Agent headers")

      When("I use the check insights API via bank account gateway to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromGatewayByUserAgents(RISKY_ACCOUNT)

      Then("I am given the relevant risking information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }

    Scenario(
      "Get risking information for a bank account on the risk list using multiple User-Agent values in one header"
    ) {
      Given(
        "I want to see if we hold any risking information for a bank account using multiple User-Agent values in one header"
      )

      When("I use the check insights API via bank account gateway to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromGatewayByUserAgents(RISKY_ACCOUNT)

      Then("I am given the relevant risking information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }

    Scenario("Get risking information for a bank account on the risk list using a single OriginatorId header") {
      Given("I want to see if we hold any risking information for a bank account using a single OriginatorId header")

      When("I use the check insights API via bank account gateway to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountCheckResponseFromGatewayByOriginatorId(RISKY_ACCOUNT)

      Then("I am given the relevant risking information")
      assertThat(actual.riskScore).isEqualTo(100)
      assertThat(actual.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
    }

    Scenario("Try to get risking information for a NINO on the risk list without a User-Agent or originatorId header") {
      Given("I want to see if we hold any risking information for a NINO")

      When("I use the NINO check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseInvalidResponseFromBankAccountGateway(RISKY_ACCOUNT)

      Then("My query is rejected")
      assertThat(actual.code).isEqualTo(403)
      assertThat(actual.description).contains(UNAUTHORISED)
    }

    Scenario("Try to get risking information for a NINO on the risk list by using invalid originatorId") {
      Given("I want to see if we hold any risking information for a NINO")

      When("I use the NINO check insights API to see what information we hold")
      val actual = bankAccountCheckHelper.parseInvalidResponseFromBankAccountGatewayByInvalidHeader(RISKY_ACCOUNT)

      Then("My query is rejected")
      assertThat(actual.code).isEqualTo(403)
      assertThat(actual.description).contains(UNAUTHORISED)
    }
  }
}
