name := "scala-jdbc"

version := "0.1"

scalaVersion := "2.12.4"

val DoobieVersion = "0.5.0"
val EffVersion = "5.0.0"

val doobieDependencies = Seq(
  // Start with this one
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  // And add any of these as needed
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion, // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres" % DoobieVersion, // Postgres driver 42.1.4 + type mappings.
  "org.tpolecat" %% "doobie-specs2" % DoobieVersion % Test, // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion % Test, // ScalaTest support for typechecking statements.
  "org.atnos" %% "eff" % EffVersion,
  "org.atnos" %% "eff-doobie" % EffVersion,
  "org.atnos" %% "eff-cats-effect" % EffVersion
)

libraryDependencies ++= Seq(
  "org.flywaydb" % "flyway-core" % "4.2.0",
  "org.testcontainers" % "postgresql" % "1.4.2" % Test
) ++ doobieDependencies

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6")

scalacOptions += "-Ypartial-unification"