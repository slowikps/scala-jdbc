package model

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

case class Book(id: UUID,
                name: String,
                author: UUID,
                createdAd: Instant,
                modifiedAd: Instant)

case class Author(id: UUID,
                name: String,
                surname: String)




object MariuszWolny extends Author(UUID.randomUUID(), "Mariusz", "Wolny") {

  val books = List(
    Book(UUID.randomUUID(),
      "Kacper Ryx",
      this.id,
      Instant.now().minus(10, ChronoUnit.DAYS),
      Instant.now()),
    Book(UUID.randomUUID(),
      "Kacper Ryx i król przeklęty",
      this.id,
      Instant.now().minus(9, ChronoUnit.DAYS),
      Instant.now()),
    Book(UUID.randomUUID(),
      "Kacper Ryx i tyran nienawistny",
      this.id,
      Instant.now().minus(8, ChronoUnit.DAYS),
      Instant.now()),
    Book(UUID.randomUUID(),
      "Kacper Ryx i król alchemików",
      this.id,
      Instant.now().minus(7, ChronoUnit.DAYS),
      Instant.now()),
  )
}

