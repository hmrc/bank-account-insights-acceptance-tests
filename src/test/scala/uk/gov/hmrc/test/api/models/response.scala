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

import java.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

/** riskScore: 0-100 <br/> reason:
  * ACCOUNT_NOT_ON_WATCH_LIST|ACCOUNT_ON_WATCH_LIST
  */
package response {
  object risklist_response_codes {
    val ACCOUNT_ON_WATCH_LIST: String     = "ACCOUNT_ON_WATCH_LIST"
    val ACCOUNT_NOT_ON_WATCH_LIST: String = "ACCOUNT_NOT_ON_WATCH_LIST"
  }
  final case class BankAccountInsightsResponse(
    correlationId: String,
    riskScore: Int,
    reason: String
  )

  object BankAccountInsightsResponse {
    object implicits {
      implicit val bankAccountInsightsResponseFormat: Format[BankAccountInsightsResponse] =
        Json.format[BankAccountInsightsResponse]
    }
  }

  object BankAccountIpp {
    final case class AttributeValue(value: String, numOfOccurrences: Long, lastSeen: LocalDateTime)

    final case class Attribute(attribute: String, count: Long, attributeValues: List[AttributeValue])

    final case class Risk(riskScore: Int, reason: String)

    final case class Insights(risk: Risk, relationships: List[Attribute])

    final case class BankAccountIppResponse(
      sortCode: String,
      accountNumber: String,
      correlationId: String,
      insights: Insights
    )

    object BankAccountIppResponse {
      object Implicits {
        implicit val ippResponseComponentValueFormat: Format[AttributeValue] = Json.format[AttributeValue]
        implicit val ippResponseComponentFormat: Format[Attribute]           = Json.format[Attribute]
        implicit val ippResponseInsightsRiskFormat: Format[Risk]             = Json.format[Risk]
        implicit val ippResponseInsightsFormat: Format[Insights]             = Json.format[Insights]
        implicit val ippResponseFormat: Format[BankAccountIppResponse]       = Json.format[BankAccountIppResponse]
      }
    }
  }
}
