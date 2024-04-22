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
import play.api.libs.ws.StandaloneWSRequest
import uk.gov.hmrc.test.api.models._
import uk.gov.hmrc.test.api.service.InternalAuthService

class InternalAuthHelper {
  val internalAuthAPI: InternalAuthService = new InternalAuthService
  val permission: Permission               =
    Permission(Resource(ResourceType("bank-account-insights"), ResourceLocation("check")), Set(IAAction("READ")))
  val defaultToken: Token                  = TokenGenerator.randomToken()

  def postConfigureInternalAuthToken(desiredToken: Token = defaultToken): AuthToken = {
    val tokenRequest: TestOnlyAddTokenRequest =
      new TestOnlyAddTokenRequest(Some(desiredToken), "object-store", Set(permission))

    val internalAuthRequestResponse: StandaloneWSRequest#Self#Response =
      internalAuthAPI.postAuthRequest(tokenRequest)
    Json.parse(internalAuthRequestResponse.body).as(AuthToken.format)
  }

  def deleteToken(token: String): Unit =
    internalAuthAPI.deleteToken(token)
}
