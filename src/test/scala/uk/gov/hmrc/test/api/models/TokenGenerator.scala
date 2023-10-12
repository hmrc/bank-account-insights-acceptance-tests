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

import java.security.SecureRandom

object TokenGenerator {

  private val chars: Seq[Char] = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')

  private def randomString(len: Int): String = {
    val srand = new SecureRandom()
    Range(0, len).map(_ => chars(srand.nextInt(chars.length))).mkString
  }

  def randomToken(len: Int = 64): Token =
    Token(s"${randomString(8)}-${randomString(len)}")
}
