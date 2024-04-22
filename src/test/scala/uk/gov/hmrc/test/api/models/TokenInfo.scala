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

package uk.gov.hmrc.test.api.models

import org.bson.types.ObjectId
import play.api.libs.functional.syntax._
import play.api.libs.json._
import uk.gov.hmrc.mongo.play.json.formats.{MongoFormats, MongoJavatimeFormats}

import java.time.Instant

case class TokenInfo(
  tokenId: TokenId,
  token: Token,
  principal: String,
  expiresAt: Instant,
  createdAt: Instant,
  lastUsedAt: Option[Instant],
  tokenType: TokenType
)

object TokenInfo {
  val mongoFormat: OFormat[TokenInfo] = {
    implicit val instf: Format[Instant] = MongoJavatimeFormats.instantFormat
    implicit val tif: Format[ObjectId]  = MongoFormats.objectIdFormat
    implicit val ttf: Format[TokenType] = TokenType.format

    ((__ \ "_id").format[ObjectId].inmap[TokenId](TokenId.apply, _.value)
      ~ (__ \ "token").format[String].inmap[Token](Token.apply, _.value)
      ~ (__ \ "principal").format[String]
      ~ (__ \ "expiresAt").format[Instant]
      ~ (__ \ "createdAt").format[Instant]
      ~ (__ \ "lastUsedAt").formatNullable[Instant]
      ~ __.format[TokenType])(TokenInfo.apply, unlift(TokenInfo.unapply))
  }

  def apiTokenWrite(permissions: Set[Permission]): OWrites[TokenInfo] = {
    implicit val ttf: Format[TokenType] = TokenType.format
    val ignore                          = OWrites[Any](_ => Json.obj())
    ((__ \ "tokenId").write[String].contramap[TokenId](_.value.toString)
      ~ ignore // don't return token
      ~ (__ \ "principal").write[String]
      ~ (__ \ "expiresAt").write[Instant]
      ~ (__ \ "createdAt").write[Instant]
      ~ (__ \ "lastUsedAt").writeNullable[Instant]
      ~ __.write[TokenType])(unlift(TokenInfo.unapply))
      .transform { js =>
        implicit val pf: Format[Permission] = Permission.format
        js ++ JsObject(Seq("permissions" -> Json.toJson(permissions)))
      }
  }
}
