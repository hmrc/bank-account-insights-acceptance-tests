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

case class Permission(resource: Resource, actions: Set[IAAction])

object Permission {
  val format: Format[Permission] = {
    implicit val af: Format[IAAction] = IAAction.format
    ((__ \ "resourceType").format[ResourceType](ResourceType.format)
      ~ (__ \ "resourceLocation").format[ResourceLocation](ResourceLocation.format)
      ~ (__ \ "actions").format[Set[IAAction]])(
      (resourceType, resourceLocation, actions) => Permission(Resource(resourceType, resourceLocation), actions),
      permission => (permission.resource.resourceType, permission.resource.resourceLocation, permission.actions)
    )
  }
}
