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

package uk.gov.hmrc.test.api.testdata

import uk.gov.hmrc.test.api.models.request.InsightsRequest

object AccountRelationships {
  val RISKY_ACCOUNT_RELATIONSHIPS: String =
    """
      |{
      |        "attribute": "paye_ref",
      |        "count": 1,
      |        "attributeValues": [
      |          {
      |            "value": "AB1324578721",
      |            "numOfOccurrences": 4,
      |            "lastSeen": "2023-01-10T12:35:00.830"
      |          }
      |        ]
      |      }
      |""".stripMargin

  val UNKNOWN_ACCOUNT_RELATIONSHIPS: String =
    """
      |{
      |        "attribute": "ct_utr",
      |        "count": 1,
      |        "attributeValues": [
      |          {
      |            "value": "AB1324578722",
      |            "numOfOccurrences": 3,
      |            "lastSeen": "2023-01-10T12:35:00.404"
      |          }
      |        ]
      |      }
      |""".stripMargin
}
