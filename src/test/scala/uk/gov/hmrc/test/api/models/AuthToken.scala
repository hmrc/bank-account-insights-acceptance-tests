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

package uk.gov.hmrc.test.api.models

import play.api.libs.functional.syntax._
import play.api.libs.json.{OFormat, __}

import java.time.Instant

case class AuthToken(token: Token, expiresAt: Instant)

object AuthToken {
  val format: OFormat[AuthToken] =
    ((__ \ "token").format[String].inmap[Token](Token.apply, _.value)
      ~ (__ \ "expiresAt").format[String].inmap[Instant](Instant.parse, _.toString))(
      AuthToken.apply,
      unlift(AuthToken.unapply)
    )
}
