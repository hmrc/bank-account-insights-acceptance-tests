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

import play.api.libs.json.{Format, JsResult, JsString, JsValue}

case class IAAction(value: String) extends AnyVal

object IAAction {
  val wildcard: IAAction = IAAction("*")

  val format: Format[IAAction] =
    new Format[IAAction] {
      def reads(json: JsValue): JsResult[IAAction] =
        json.validate[String].map(action => IAAction(action.toUpperCase))

      def writes(action: IAAction): JsValue = JsString(action.value)
    }
}
