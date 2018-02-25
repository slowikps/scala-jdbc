import cats.data.Writer
import cats.effect.IO
import doobie.free.connection.ConnectionIO
import doobie.util.transactor.Transactor
import model.{Book, MariuszWolny}
import org.atnos.eff.Fx
import repository.{AuthorRepository, BooksRepository}

class RollbackAction(action: => Unit, errorAction: (Throwable) => Unit = _ => ()) {

  def onError(errorAction: => Unit)           = new RollbackAction(action, _ => errorAction)
  def onError(errorAction: Throwable => Unit) = new RollbackAction(action, errorAction)

  import cats.syntax.either._

  def rollback: Either[Throwable, Unit] =
    Either
      .catchNonFatal(action)
      .leftMap(er => {
        errorAction(er)
        er
      })

}

object EffApplication extends App {

  private val port = 32777

  private lazy val xa =
    Transactor.fromDriverManager[IO]("org.postgresql.Driver", s"jdbc:postgresql://localhost:$port/test", "test", "test")


  type StackWithWriter = Fx.fx3[ConnectionIO, IO, Writer[RollbackAction, ?]]
  type Stack = Fx.fx2[ConnectionIO, IO]

  private val booksRepository  = new BooksRepository
  private val authorRepository = new AuthorRepository

  import org.atnos.eff.Eff
  import org.atnos.eff.syntax.addon.doobie._
  import org.atnos.eff.syntax.all._

  val inSingleTransaction: Eff[Stack, List[Book]] =
    for {
      _ <- booksRepository.truncate.run.send[Stack]
      _ <- authorRepository.truncate.run.send[Stack]
      _ <- authorRepository.insert(MariuszWolny).run.send[Stack]
      _ <- MariuszWolny.books
            .map(booksRepository.insert)
            .map(_.run)
            .reduce((l, r) => l.flatMap(_ => r))
            .send[Stack]
      dbBooks <- booksRepository.selectAll.send[Stack]
    } yield {
      import cats.implicits._
      if (dbBooks.length === 2) {
        //Boom rollback everything
        println("We are about to throw exception")
        throw new IllegalArgumentException
      } else dbBooks
    }

  import PrintlnOps._

  inSingleTransaction.runConnectionIO(xa).detach.unsafeRunSync().print()

  println("The end")
}
