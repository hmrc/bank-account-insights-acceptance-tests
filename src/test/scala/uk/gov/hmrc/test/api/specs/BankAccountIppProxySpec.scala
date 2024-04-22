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
import play.api.libs.json.Json
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.Attribute
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.BankAccountIppResponse.Implicits._
import uk.gov.hmrc.test.api.models.response.risklist_response_codes.{ACCOUNT_NOT_ON_WATCH_LIST, ACCOUNT_ON_WATCH_LIST}
import uk.gov.hmrc.test.api.testdata.AccountRelationships.{RISKY_ACCOUNT_RELATIONSHIPS, UNKNOWN_ACCOUNT_RELATIONSHIPS}
import uk.gov.hmrc.test.api.testdata.BankAccounts.{RISKY_ACCOUNT, UNKNOWN_ACCOUNT}

class BankAccountIppProxySpec extends BaseSpec {

  val host: String = TestConfiguration.url("bank-account-insights-proxy")
  val checkAccountURL: String = s"$host/${Endpoints.IPP}"
  val checkAccountURLWithRoute: String = s"$host/${Endpoints.IPP_WITH_ROUTE}"

  val expectedRiskyAccountRelationships   = Seq(Json.parse(RISKY_ACCOUNT_RELATIONSHIPS).as[Attribute])
  val expectedUnknownAccountRelationships = Seq(Json.parse(UNKNOWN_ACCOUNT_RELATIONSHIPS).as[Attribute])

  Feature("Check the ban account IPP API through proxy") {

    Scenario("Get IPP and risk information for a bank account on the risk list using ipp route") {
      Given("I want to see if we hold any risk and IPP information for a bank account using ipp route")

      When("I use the IPP API to see what information we hold")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromIppProxy(checkAccountURL, RISKY_ACCOUNT)

      Then("I am given the relevant risk and IPP information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(100)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedRiskyAccountRelationships)
    }

    Scenario("Get IPP information for an UNKNOWN bank account which exists in IPP using ipp route") {
      Given("I want to see if we hold any risk information for a bank account using ipp route")

      When("I want to see if we hold any IPP information for an unknown bank account")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromIppProxy(checkAccountURL, UNKNOWN_ACCOUNT)

      Then("I am given the relevant risk and IPP information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(0)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedUnknownAccountRelationships)
    }

    Scenario("Get IPP and risk information for a bank account on the risk list using \"bank-account-insights/ipp\" route") {
      Given("I want to see if we hold any risk and IPP information for a bank account using \"bank-account-insights/ipp\" route")

      When("I use the IPP API to see what information we hold")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromIppProxy(checkAccountURLWithRoute, RISKY_ACCOUNT)

      Then("I am given the relevant risk and IPP information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(100)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedRiskyAccountRelationships)
    }

    Scenario("Get IPP information for an UNKNOWN bank account which exists in IPP using \"bank-account-insights/ipp\" route") {
      Given("I want to see if we hold any risk information for a bank account using \"bank-account-insights/ipp\" route")

      When("I want to see if we hold any IPP information for an unknown bank account")
      val actual =
        bankAccountCheckHelper.parseValidBankAccountCheckResponseFromIppProxy(checkAccountURLWithRoute, UNKNOWN_ACCOUNT)

      Then("I am given the relevant risk and IPP information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(0)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedUnknownAccountRelationships)
    }
  }
}
