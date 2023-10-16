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

import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json.{Format, OFormat, __}

case class TestOnlyAddTokenRequest(token: Option[Token], principal: String, permissions: Set[Permission])

object TestOnlyAddTokenRequest {
  val format: OFormat[TestOnlyAddTokenRequest] = {
    implicit val pf: Format[Permission] = Permission.format
    ((__ \ "token").formatNullable[String].inmap[Option[Token]](_.map(Token.apply), _.map(_.value))
      ~ (__ \ "principal").format[String]
      ~ (__ \ "permissions")
        .format[Set[Permission]])(TestOnlyAddTokenRequest.apply, unlift(TestOnlyAddTokenRequest.unapply))
  }
}
