package config

import cats.effect.IO
import doobie.util.transactor.Transactor
import org.testcontainers.containers.PostgreSQLContainer
import org.flywaydb.core.Flyway

object PostgresqlDocker {

  lazy val xa =
    Transactor.fromDriverManager[IO]("org.postgresql.Driver",
                                     postgres.getJdbcUrl,
                                     postgres.getUsername,
                                     postgres.getPassword)

  private val postgres: PostgreSQLContainer[Nothing] = new PostgreSQLContainer()

  def start() = {
    postgres.start()
    val flyway = new Flyway

    flyway.setDataSource(postgres.getJdbcUrl, postgres.getUsername, postgres.getPassword)
    flyway.migrate()
  }

  def connectionData() = s"jdbcUrl: ${postgres.getJdbcUrl}, username: ${postgres.getUsername}, password: ${postgres.getPassword}"
}
