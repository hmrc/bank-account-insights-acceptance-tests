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

package uk.gov.hmrc.test.api.conf

import com.typesafe.config.{Config, ConfigFactory}

object TestConfiguration {
  val config: Config        = ConfigFactory.load()
  val env: String           = config.getString("environment")
  val useZap: Boolean       = config.getBoolean("use.zap.proxy")
  val defaultConfig: Config = config.getConfig("local")
  val envConfig: Config     = config.getConfig(env).withFallback(defaultConfig)

  def url(service: String): String =
    env match {
      case "local" => s"$environmentHost:${servicePort(service)}"
      case _       => s"${envConfig.getString(s"services.host")}"
    }
  def userAgent: String            = envConfig.getString("user-agents.test-http-client")
  def expectedServiceName: String  = envConfig.getString("user-agents.service-name")

  def environmentHost: String = envConfig.getString("services.host")

  def servicePort(serviceName: String): String = envConfig.getString(s"services.$serviceName.port")
}
