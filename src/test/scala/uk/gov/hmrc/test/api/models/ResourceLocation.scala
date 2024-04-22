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

case class ResourceLocation private[models] (value: String) extends AnyVal

object ResourceLocation {
  val wildcard: ResourceLocation = ResourceLocation("*")

  val format: Format[ResourceLocation] =
    new Format[ResourceLocation] {
      def reads(json: JsValue): JsResult[ResourceLocation] =
        json.validate[String].flatMap(Helper.withoutLeadingOrTrailingSlashes).map(s => ResourceLocation(s.toLowerCase))

      def writes(resource: ResourceLocation): JsValue = JsString(resource.value)
    }
}
