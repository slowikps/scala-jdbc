object PrintlnOps {

  implicit class PrintLn[T](val in: T) extends AnyVal {
    def print(): Unit = in match {
      case l: Seq[_] => l.foreach(_.print())
      case Right(elem) =>
        println(s"Right of:")
        elem.print()
      case _ => println(in)
    }
  }
}
