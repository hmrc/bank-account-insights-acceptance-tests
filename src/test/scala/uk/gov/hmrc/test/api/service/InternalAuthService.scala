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

package uk.gov.hmrc.test.api.service

import play.api.libs.json.Json
import play.api.libs.ws.StandaloneWSRequest
import uk.gov.hmrc.test.api.client.HttpClient
import uk.gov.hmrc.test.api.conf.TestConfiguration
import uk.gov.hmrc.test.api.helpers.Endpoints
import uk.gov.hmrc.test.api.models.TestOnlyAddTokenRequest

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class InternalAuthService extends HttpClient {
  var internalAuth: String = TestConfiguration.url("internal-auth")

  def postAuthRequest(
    tokenDetails: TestOnlyAddTokenRequest,
    host: String = internalAuth
  ): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        s"$host/${Endpoints.INTERNAL_AUTH}",
        Json.toJson(tokenDetails)(TestOnlyAddTokenRequest.format).toString(),
        ("Content-Type", "application/json"),
        ("Authorization", "token")
      ),
      10.seconds
    )

  def deleteToken(token: String, host: String = internalAuth): StandaloneWSRequest#Self#Response =
    Await.result(
      post(
        s"$host/${Endpoints.DELETE_TOKEN}",
        "",
        ("Authorization", token)
      ),
      10.seconds
    )
}
