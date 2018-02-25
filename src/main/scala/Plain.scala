import org.atnos.eff.{Eff, Fx}
//object Plain extends App {
//  import org.atnos.eff._, all._, syntax.all._
//  import cats.data.Writer
//
//  type S = Fx.fx1[Writer[String, ?]]
//
//  import cats.data.Writer
//  import cats.instances.string._ // for Monoid
//
//  val action: Eff[S, Int] = for {
//    a <- pure[S, Int](1)
//    _ <- tell(s"first value: $a")
//    b <- pure[S, Int](2)
//    _ <- tell(s"second value $b")
//
//  } yield a + b
//
//  println(
//    action
//  )
//}

object FromNet extends App {

  import cats.data._
  import cats.Eval
  import org.atnos.eff.all._
  import org.atnos.eff.syntax.all._


  import scala.collection.mutable.ListBuffer


  def sideEffecting = {
    type S = Fx.fx1[Writer[String, ?]]

    val action: Eff[S, String] = for {
      f <- tell[S, String]("hello")
      h <- tell[S, String]("world")
    } yield "hello world"

    val messages: ListBuffer[String] = new ListBuffer[String]

    action.runWriterUnsafe((m: String) => messages.append(m)).run

    println(messages)
  }

  def evalWriting = {
    type S = Fx.fx2[Writer[String, ?], Eval]

    val action: Eff[S, String] = for {
      f <- tell[S, String]("hello")
      h <- tell[S, String]("world")
    } yield "hello world"

    val messages: ListBuffer[String] = new ListBuffer[String]

    val ranWriterAction = action.runWriterEval((m: String) => Eval.later(messages.append(m)))

//    ranWriterAction.runEval.run ==== "hello world"
//    ranWriterAction.runEval.run ==== "hello world"

//    messages.toList ==== List("hello", "world", "hello", "world")

    println(ranWriterAction)
  }


  sideEffecting
  evalWriting
}
