import cats.effect.IO
import cats.free.Free
import doobie.free.connection
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.transactor.Transactor
import model.{Book, MariuszWolny}
import repository.{AuthorRepository, BooksRepository}

object BasicApplication extends App {

  lazy val xa =
    Transactor.fromDriverManager[IO]("org.postgresql.Driver", s"jdbc:postgresql://localhost:$port/test", "test", "test")
  val port = 32777

  private val booksRepository  = new BooksRepository
  private val authorRepository = new AuthorRepository

  val inSingleTransaction: Free[connection.ConnectionOp, List[Book]] =
    for {
      _ <- booksRepository.truncate.run
      _ <- authorRepository.truncate.run
      _ <- authorRepository.insert(MariuszWolny).run
      _ <- {
        import cats.implicits._
        (println("Testing lift")).pure[ConnectionIO]
      }
      _ <- MariuszWolny.books
            .map(booksRepository.insert)
            .map(_.run)
            .reduce((l, r) => l.flatMap(_ => r))
      dbBooks <- booksRepository.selectAll
    } yield {
      import cats.implicits._
      if (dbBooks.length === 2) {
        //Boom rollback everything
        println("We are about to throw exception")
        throw new IllegalArgumentException
      } else dbBooks
    }

  val sameAs: ConnectionIO[List[Book]] = inSingleTransaction

  println("Before execution")

  import PrintlnOps._

  sameAs
    .transact(xa)
    .attempt
    .unsafeRunSync()
    .print()

}
