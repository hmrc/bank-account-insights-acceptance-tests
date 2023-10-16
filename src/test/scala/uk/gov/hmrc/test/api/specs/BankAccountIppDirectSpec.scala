/*
 * Copyright 2023 HM Revenue & Customs
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
import play.api.libs.json.Json
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.{Attribute, BankAccountIppResponse}
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.BankAccountIppResponse.Implicits._
import uk.gov.hmrc.test.api.models.response.risklist_response_codes.{ACCOUNT_NOT_ON_WATCH_LIST, ACCOUNT_ON_WATCH_LIST}
import uk.gov.hmrc.test.api.testdata.BankAccounts.{RISKY_ACCOUNT, RISKY_ACCOUNT_NOT_IN_IPP, UNKNOWN_ACCOUNT}

import scala.concurrent.duration.DurationInt

class BankAccountIppDirectSpec extends BaseSpec with WireMockTrait {

  val expectedRiskyAccountAttribute         = "paye_ref"
  val expectedUnKnownAccountAttribute       = "ct_utr"
  val expectedRiskyAccountAttributeValues   =
    "{\"value\": \"AB1324578721\",\"numOfOccurrences\": 4,\"lastSeen\": \"2023-01-10T12:35:00.830828\"}"
  val expectedUnKnownAccountAttributeValues =
    "{\"value\": \"AB1324578722\",\"numOfOccurrences\": 3,\"lastSeen\": \"2023-01-10T12:35:00.404784\"}"

  val expectedRiskyAccountRelationshipsString =
    s"""
      |{
      |        "attribute": "$expectedRiskyAccountAttribute",
      |        "count": 1,
      |        "attributeValues": [
      |           $expectedRiskyAccountAttributeValues
      |        ]
      |      }
      |""".stripMargin

  val expectedUnknownAccountRelationshipsString =
    s"""
      |{
      |        "attribute": "$expectedUnKnownAccountAttribute",
      |        "count": 1,
      |        "attributeValues": [
      |           $expectedUnKnownAccountAttributeValues
      |        ]
      |      }
      |""".stripMargin

  val expectedRiskyAccountRelationships   = Seq(Json.parse(expectedRiskyAccountRelationshipsString).as[Attribute])
  val expectedUnknownAccountRelationships = Seq(Json.parse(expectedUnknownAccountRelationshipsString).as[Attribute])

  Feature("Check the bank account IPP API directly") {

    Scenario("Get IPP and risk information for a bank account on the risk list") {
      Given("I want to see if we hold any risk and IPP information for a bank account")

      When("I use the IPP API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountResponseFromIpp(RISKY_ACCOUNT)

      Then("I am given the relevant risk and IPP information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(100)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedRiskyAccountRelationships)

      verifyIppAudit(
        actual,
        TestConfiguration,
        RISKY_ACCOUNT.sortCode,
        RISKY_ACCOUNT.accountNumber,
        expectedRiskyAccountAttribute,
        expectedRiskyAccountAttributeValues
      )
    }

    Scenario("Get IPP information for an UNKNOWN bank account which exists in IPP") {
      Given("I want to see if we hold any IPP information for an unknown bank account")

      When("I use the check IPP API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountResponseFromIpp(UNKNOWN_ACCOUNT)

      Then("I am given the relevant IPP and risk information")
      assertThat(actual.insights.risk.riskScore).isEqualTo(0)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_NOT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(expectedUnknownAccountRelationships)

      verifyIppAudit(
        actual,
        TestConfiguration,
        UNKNOWN_ACCOUNT.sortCode,
        UNKNOWN_ACCOUNT.accountNumber,
        expectedUnKnownAccountAttribute,
        expectedUnKnownAccountAttributeValues
      )
    }

    Scenario("Get risk information for a bank account on the risk list but doesn't exist in the IPP") {
      Given(
        "I want to see if we hold any risk information for bank account on the risk list but doesn't exist in the IPP"
      )

      When("I use the IPP API to see what information we hold")
      val actual = bankAccountCheckHelper.parseValidBankAccountResponseFromIpp(RISKY_ACCOUNT_NOT_IN_IPP)

      Then("I am given the relevant risk information only")
      assertThat(actual.insights.risk.riskScore).isEqualTo(100)
      assertThat(actual.insights.risk.reason).isEqualTo(ACCOUNT_ON_WATCH_LIST)
      assertThat(actual.insights.relationships).isEqualTo(Seq.empty)
    }
  }

  private def verifyIppAudit(
    actual: BankAccountIppResponse,
    testConfiguration: TestConfiguration.type,
    sortCode: String,
    accountNumber: String,
    expectedAttribute: String,
    expectedAttributeValues: String
  ): Unit =
    verify(
      delayedFunction(1.seconds)(
        postRequestedFor(urlEqualTo("/write/audit"))
          .withRequestBody(
            matchingJsonPath(
              "$[?(" +
                s"@.auditSource == '${testConfiguration.expectedServiceName}'" +
                "&& @.auditType == 'BankAccountInsightsLookup'" +
                s"&& @.detail.sortCode == '$sortCode'" +
                s"&& @.detail.accountNumber == '$accountNumber'" +
                s"&& @.detail.userAgent == '${testConfiguration.userAgent}'" +
                s"&& @.detail.insights.risk.riskScore == '${actual.insights.risk.riskScore}'" +
                s"&& @.detail.insights.risk.reason == '${actual.insights.risk.reason}'" +
                s"&& @.detail.insights.relationships[0].attribute == '$expectedAttribute'" +
                s"&& @.detail.insights.relationships[0].count == 1" +
                s"&& @.detail.insights.relationships[0].attributeValues[0] == $expectedAttributeValues" +
                ")]"
            )
          )
      )
    )
}
