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

package uk.gov.hmrc.test.api.client

import org.apache.pekko.actor.ActorSystem
import play.api.libs.ws.DefaultBodyWritables._
import play.api.libs.ws.ahc.{AhcWSClientConfig, StandaloneAhcWSClient}
import play.api.libs.ws.{DefaultWSProxyServer, StandaloneWSRequest, WSClientConfig}
import uk.gov.hmrc.test.api.conf.TestConfiguration.useZap

import scala.concurrent.{ExecutionContext, Future}

trait HttpClient {

  implicit val actorSystem: ActorSystem = ActorSystem()
  val wsClient: StandaloneAhcWSClient   = StandaloneAhcWSClient(
    AhcWSClientConfig(WSClientConfig())
  )
  val zapHost: Option[String]           = sys.env.get("ZAP_HOST")
  val defaultZapHost: String            = "localhost:11000"
  implicit val ec: ExecutionContext     = ExecutionContext.global

  def zapProxy: DefaultWSProxyServer = {
    val proxyParts: Seq[String] = zapHost.getOrElse(defaultZapHost).split(":").toSeq
    DefaultWSProxyServer(host = proxyParts.headOption.get, port = proxyParts.lastOption.get.toInt)
  }

  def get(url: String, headers: (String, String)*): Future[StandaloneWSRequest#Self#Response] =
    if (useZap) {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .withProxyServer(zapProxy)
        .get()
    } else {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .get()
    }

  def post(url: String, bodyAsJson: String, headers: (String, String)*): Future[StandaloneWSRequest#Self#Response] =
    if (useZap) {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .withProxyServer(zapProxy)
        .post(bodyAsJson)
    } else {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .post(bodyAsJson)
    }

  def delete(url: String, headers: (String, String)*): Future[StandaloneWSRequest#Self#Response] =
    if (useZap) {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .withProxyServer(zapProxy)
        .delete()
    } else {
      wsClient
        .url(url)
        .withHttpHeaders(headers: _*)
        .delete()
    }
}
