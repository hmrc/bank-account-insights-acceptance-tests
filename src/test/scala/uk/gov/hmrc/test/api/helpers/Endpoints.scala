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

object Endpoints {
  val CHECK_INSIGHTS = "check/insights"
  val CHECK_INSIGHTS_WITH_ROUTE = s"bank-account-insights/${CHECK_INSIGHTS}"
  val IPP = "ipp"
  val IPP_WITH_ROUTE = s"bank-account-insights/${IPP}"
  val INTERNAL_AUTH  = "test-only/token"
  val DELETE_TOKEN   = "internal-auth/token/revoke"
}
