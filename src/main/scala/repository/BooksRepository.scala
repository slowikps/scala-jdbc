package repository

import model.Book
import doobie._
import doobie.implicits._
import cats._
import cats.data._
import cats.effect.IO
import cats.implicits._
import doobie.postgres.implicits._

class BooksRepository {
  private object q {

    val select = fr"SELECT id, name, author, created_at, modified_at"
    val from = fr"FROM book"

    val delete = fr"DELETE" ++ from

    def insert(in: Book) =
      sql"""
      INSERT INTO book(id, name, author, created_at, modified_at)
      VALUES (${in.id}, ${in.name}, ${in.author}, ${in.createdAd}, ${in.modifiedAd})
        """
  }

  def selectAll: doobie.ConnectionIO[List[Book]] =
    (q.select ++
      q.from)
      .query[Book]
      .to[List]

  def insert(in: Book) = q.insert(in).update

  def truncate: Update0 = q.delete.update
}
