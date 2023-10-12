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
import play.api.libs.json._

case class Resource(resourceType: ResourceType, resourceLocation: ResourceLocation) {

  /**
    * Note: This method will return true if both resources are equivalent. The compared resource does
    * not need to be nested.
    * e.g Resource.from("*", "*").isAncestorOf(Resource.from("object-store", "bucket")) == true
    */
  def isAncestorOf(other: Resource): Boolean = {
    val tokenizedLocations      = resourceLocation.value.split("/")
    val otherTokenizedLocations = other.resourceLocation.value.split("/")

    isResourceTypeAllowed(resourceType, other.resourceType) &&
    tokenizedLocations.size <= otherTokenizedLocations.size &&
    areAncestorLocationsEqual(tokenizedLocations, otherTokenizedLocations)
  }

  private def isResourceTypeAllowed(r1: ResourceType, r2: ResourceType): Boolean =
    r1.value == "*" || r1 == r2

  private def areAncestorLocationsEqual(
    tokenizedLocations: Array[String],
    otherTokenizedLocations: Array[String]
  ): Boolean =
    tokenizedLocations
      .zip(otherTokenizedLocations)
      .takeWhile(_._1 != "*")
      .forall { case (k, v) => k == v }
}

object Resource {
  val format: OFormat[Resource] =
    ((__ \ "resourceType").format[ResourceType](ResourceType.format)
      ~ (__ \ "resourceLocation")
        .format[ResourceLocation](ResourceLocation.format))(Resource.apply, unlift(Resource.unapply))

  def from(resourceType: String, resourceLocation: String): Resource =
    Resource(ResourceType(resourceType), ResourceLocation(resourceLocation))
}
