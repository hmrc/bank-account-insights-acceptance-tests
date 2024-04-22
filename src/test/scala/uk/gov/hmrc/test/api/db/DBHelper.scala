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

package uk.gov.hmrc.test.api.db

import cats.effect.{IO, Resource}
import doobie.Transactor
import doobie.implicits._
import doobie.util.fragment.Fragment.const

import java.io.File
import java.net.URI
import scala.io.{BufferedSource, Source}

object DBHelper {
  def printLine(s: String): IO[Unit] = IO(println(s))

  def classpathResourceURL[T](resourceName: String, t: T): IO[URI] = {
    println(s""">>> classpathResourceURL(resourceName): $resourceName""")
    IO(t.getClass.getResource(resourceName).toURI)
  }

  def readResource(fileName: File): Resource[IO, BufferedSource] =
    Resource.make(IO(Source.fromURL(fileName.toURI.toURL, "utf-8")))(f => IO(f.close()))

  private val schemaNamePlaceHolder = "__schema__"

  def update(fileName: File, schemaName: String)(implicit tx: Transactor[IO]): IO[doobie.Transactor[IO]] =
    for {
      sql <- readResource(fileName).use(f => IO(f.mkString.replaceAll(schemaNamePlaceHolder, schemaName)))
      n   <- const(sql).update.run.transact(tx)
    } yield tx

  def update(s: String)(implicit tx: Transactor[IO]): IO[Int] =
    const(s).update.run.transact(tx)
}
