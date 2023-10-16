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
import play.api.libs.json._

sealed trait TokenType
object TokenType {
  case object Service extends TokenType
  case class User(
    teams: Set[String],
    ownedServices: Set[String]
  ) extends TokenType

  val format: Format[TokenType] = {
    val reads: Reads[TokenType] =
      (__ \ "tokenType").read[String].flatMap[TokenType] {
        case "service" => Reads.pure(Service)
        case "user"    =>
          ((__ \ "teams").read[Set[String]]
            ~ (__ \ "ownedServices").read[Set[String]])(User.apply _)
      }

    val writes: Writes[TokenType] = {
      case Service                    => JsObject(Seq("tokenType" -> JsString("service")))
      case User(teams, ownedServices) =>
        JsObject(
          Seq(
            "tokenType"     -> JsString("user"),
            "teams"         -> Json.toJson(teams),
            "ownedServices" -> Json.toJson(ownedServices)
          )
        )
    }

    Format(reads, writes)
  }
}
