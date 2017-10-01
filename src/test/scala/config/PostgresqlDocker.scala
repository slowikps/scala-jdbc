package config

import cats.effect.IO
import doobie.util.transactor.Transactor
import org.testcontainers.containers.PostgreSQLContainer

object PostgresqlDocker {

  val xa = {
    postgres.start()
    Transactor.fromDriverManager[IO]("org.postgresql.Driver",
                                     postgres.getJdbcUrl,
                                     postgres.getUsername,
                                     postgres.getPassword)
  }

  private val postgres: PostgreSQLContainer[Nothing] =
    new PostgreSQLContainer()
}
