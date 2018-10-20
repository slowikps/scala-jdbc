package config

import java.time.{Duration, Instant}
import java.time.temporal.ChronoUnit
import java.util.UUID

import cats.Traverse
import cats.effect.IO
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import doobie.implicits._
import io.circe.{Json, JsonObject}
import model.{Author, AuthorAudit}
import org.flywaydb.core.Flyway
import repository.{AuthorAuditRepository, AuthorRepository}
import io.circe.syntax._

import scala.collection.immutable
object JustFlyway extends App {

  val port = 5432

  val jdbcUrl  = s"jdbc:postgresql://localhost:$port/postgres"
  val username = "postgres"
  val password = "test"

  lazy val xa =
    Transactor.fromDriverManager[IO]("org.postgresql.Driver", jdbcUrl, username, password)

  def migrate() = {
    val flyway = new Flyway
    flyway.setDataSource(jdbcUrl, username, password)

    flyway.clean()

    flyway.migrate()
  }

  migrate()
  insertAuditData()
  def insertAuditData() = {
    import cats.instances.list._
    import cats.syntax.traverse._

    val auditRepository  = new AuthorAuditRepository
    val authorRepository = new AuthorRepository
    val start            = Instant.now()

    val authors: List[UUID] = UUID.fromString("4d641084-5e59-4870-82d9-1c26cac1b09f") :: (1 to 9).toList.map(_ => UUID.randomUUID())

    val authorInserts: List[ConnectionIO[Int]] = authors.zipWithIndex.map {
      case (uuid, idx) => authorRepository.insert(Author(uuid, s"name_$idx", s"surname_$idx")).run
    }

    val auditInserts: List[ConnectionIO[Int]] = (1 to 100000)
      .map(
        idx =>
          auditRepository
            .insert(
              AuthorAudit(idx, authors(idx % 10), s"created_id_$idx", Instant.now().plus(idx, ChronoUnit.MINUTES), Json.Null)
            )
            .run)
      .toList

    println("Preparation: " + Duration.between(start, Instant.now()).toMillis)

    (authorInserts ++ auditInserts).sequence.transact(xa).unsafeRunSync()
    println(Duration.between(start, Instant.now()).toMillis)
  }
}
