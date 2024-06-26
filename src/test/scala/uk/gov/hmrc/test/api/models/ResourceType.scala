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

import play.api.libs.json.{Format, JsResult, JsString, JsValue}

case class ResourceType private[models] (value: String) extends AnyVal

object ResourceType {
  val wildcard: ResourceType = ResourceType("*")

  val format: Format[ResourceType] =
    new Format[ResourceType] {
      def reads(json: JsValue): JsResult[ResourceType] =
        json.validate[String].flatMap(Helper.withoutLeadingOrTrailingSlashes).map(s => ResourceType(s.toLowerCase))

      def writes(resource: ResourceType): JsValue = JsString(resource.value)
    }
}
