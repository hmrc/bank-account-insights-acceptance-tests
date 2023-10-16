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

package uk.gov.hmrc.test.api.db

import cats.effect.{ContextShift, IO, Timer}
import doobie._
import doobie.implicits._
import doobie.postgres.{PFCM, PHC}
import uk.gov.hmrc.test.api.db.DBHelper._

import java.io.File
import java.net.URI
import java.nio.file.{Files, StandardOpenOption}
import scala.concurrent.ExecutionContext.Implicits.global

trait PostgresDB {

  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO]     = IO.timer(global)

  protected lazy val tx: Transactor[IO] = {
    println(s""">>> schemaName: $schemaName""")
    for {
      ptx <- createInitialTransactor()
      _   <- initialiseDatabaseSchema()(ptx)
      _   <- initialiseTestData()(ptx)
      stx <- createSchemaTransactor()
    } yield stx
  }.unsafeRunSync()

  private def initialiseDatabaseSchema()(implicit t: Transactor[IO]): IO[Transactor[IO]] = {
    (for {
      _ <- printLine(s""">>> Creating schemaName: $schemaName""")
      _ <- update(s"CREATE SCHEMA IF NOT EXISTS $schemaName")
      _ <- printLine(s""">>> Created schemaName: $schemaName""")

      _ <- printLine(s""">>> Setting search path to $schemaName""")
      _ <- update(s"SET SEARCH_PATH TO $schemaName")
      _ <- printLine(s""">>> Set search path to $schemaName""")

      _   <- printLine(s""">>> Creating bank account lookup table""")
      ddl <- classpathResourceURL(s"/data/bank_account_ddl.sql", this)
      _   <- update(new File(ddl), schemaName)
      _   <- printLine(s""">>> Created bank account lookup table""")
    } yield ()).unsafeRunSync()

    IO(t)
  }

  private def initialiseTestData()(implicit t: Transactor[IO]): IO[Transactor[IO]] = {
    (for {
      _   <- printLine(s""">>> Creating bank account lookup view and indexes function""")
      sql <- classpathResourceURL(s"/data/bank_account_reject_list.csv", this)
      _   <- ingestDataFile(s"$schemaName.bankaccount_reject", sql)
      _   <- printLine(s""">>> Created bank account lookup view and indexes function""")
    } yield ()).unsafeRunSync()

    IO(t)
  }

  protected val schemaName = s"public"

  private val jdbcBaseUrl = s"jdbc:postgresql://localhost:5432/postgres"

  private def createInitialTransactor(): IO[Transactor[IO]] =
    createTransactorWith(
      jdbcUrl = s"$jdbcBaseUrl",
      username = "postgres",
      password = "postgres"
    )

  private def createSchemaTransactor(): IO[Transactor[IO]] =
    createTransactorWith(
      jdbcUrl = s"$jdbcBaseUrl?currentSchema=$schemaName",
      username = "postgres",
      password = "postgres"
    )

  private def createTransactorWith(jdbcUrl: String, username: String, password: String): IO[Transactor[IO]] =
    IO {
      Transactor.fromDriverManager[IO](
        driver = "org.postgresql.Driver",
        url = jdbcUrl,
        user = username,
        pass = password
      )
    }

  private def ingestDataFile(table: String, filePath: URI)(implicit tx: Transactor[IO]): IO[Long] = {
    val in = Files.newInputStream(new File(filePath).toPath, StandardOpenOption.READ)
    PHC
      .pgGetCopyAPI(
        PFCM.copyIn(s"""COPY $table FROM STDIN WITH (FORMAT CSV, HEADER, DELIMITER ',');""", in)
      )
      .transact(tx)
  }
}
