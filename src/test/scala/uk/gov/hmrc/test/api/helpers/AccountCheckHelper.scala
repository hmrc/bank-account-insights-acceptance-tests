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

package uk.gov.hmrc.test.api.helpers

import play.api.libs.json.Json
import play.api.libs.ws.{StandaloneWSRequest, StandaloneWSResponse}
import uk.gov.hmrc.test.api.models.request.InsightsRequest
import uk.gov.hmrc.test.api.models.response.BankAccountInsightsResponse
import uk.gov.hmrc.test.api.models.response.BankAccountInsightsResponse.implicits.bankAccountInsightsResponseFormat
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.BankAccountIppResponse
import uk.gov.hmrc.test.api.models.response.BankAccountIpp.BankAccountIppResponse.Implicits._
import uk.gov.hmrc.test.api.models.BadRequest
import uk.gov.hmrc.test.api.service.{BankAccountGatewayCheckService, BankAccountInsightsCheckService, BankAccountIppService}

class AccountCheckHelper {

  val bankAccountInsightsCheckAPI: BankAccountInsightsCheckService = new BankAccountInsightsCheckService
  val bankAccountIppCheckAPI: BankAccountIppService                = new BankAccountIppService
  val bankAccountGatewayCheckAPI: BankAccountGatewayCheckService   = new BankAccountGatewayCheckService
  def parseValidBankAccountCheckResponseFromInsightsProxy(
    checkAccountURL: String,
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountInsightsCheckAPI.postInsightsCheck(checkAccountURL, accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }

  def parseValidBankAccountCheckResponseFromInsightsDirect(
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountInsightsCheckAPI.postInsightsCheckDirect(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }

  def parseValidBankAccountResponseFromIpp(
    accountDetails: InsightsRequest
  ): BankAccountIppResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountIppCheckAPI.postIppCheckDirect(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountIppResponse]
  }

  def parseValidBankAccountCheckResponseFromIppProxy(
    checkAccountURL: String,
    accountDetails: InsightsRequest
  ): BankAccountIppResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountIppCheckAPI.postIppCheck(checkAccountURL, accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountIppResponse]
  }

  def parseInvalidResponseFromBankAccountGateway(
    accountDetails: InsightsRequest
  ): BadRequest = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postInvalidBankAccountGatewayCheck(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BadRequest]
  }

  def parseInvalidResponseFromBankAccountGatewayByInvalidHeader(
    accountDetails: InsightsRequest
  ): BadRequest = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postInvalidBankAccountGatewayCheckByInvalidHeader(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BadRequest]
  }

  def parseValidBankAccountCheckResponseFromGateway(
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postBankAccountGatewayCheck(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }

  def parseValidBankAccountCheckResponseFromGatewayByUserAgents(
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postBankAccountGatewayCheckByMultipleUserAgentHeaders(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }

  def parseValidBankAccountCheckResponseFromGatewayByOriginatorId(
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postBankAccountGatewayCheckByByOriginatorIdHeader(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }

  def parseValidBankAccountCheckResponseFromGatewayByMultipleUserAgentValuesInOneHeader(
    accountDetails: InsightsRequest
  ): BankAccountInsightsResponse = {
    val authServiceRequestResponse: StandaloneWSResponse =
      bankAccountGatewayCheckAPI.postBankAccountGatewayCheckByMultipleUserAgentValuesInOneHeader(accountDetails)
    Json.parse(authServiceRequestResponse.body).as[BankAccountInsightsResponse]
  }
}
