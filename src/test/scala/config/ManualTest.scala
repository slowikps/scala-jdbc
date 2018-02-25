package config

object ManualTest extends App {

  PostgresqlDocker.start()

  println(
    PostgresqlDocker.connectionData()
  )
}
