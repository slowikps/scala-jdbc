name := "scala-jdbc"

version := "0.1"

scalaVersion := "2.12.3"

val DoobieVersion = "0.5.0-M8"

val doobieDependencies = Seq(
  // Start with this one
  "org.tpolecat" %% "doobie-core"      % DoobieVersion,
  // And add any of these as needed
  "org.tpolecat" %% "doobie-hikari"    % DoobieVersion, // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % DoobieVersion, // Postgres driver 42.1.4 + type mappings.
  "org.tpolecat" %% "doobie-specs2"    % DoobieVersion % Test, // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % Test  // ScalaTest support for typechecking statements.
)

libraryDependencies ++= Seq(
  "org.testcontainers" % "postgresql" % "1.4.2" % Test,
) ++ doobieDependencies