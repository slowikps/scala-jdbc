package repository

import doobie.implicits._
import model.{Author, Book}
import model.Book
import doobie._
import doobie.implicits._
import cats._
import cats.data._
import cats.effect.IO
import cats.implicits._
import doobie.postgres.implicits._

class AuthorRepository {
  private object q {

    val select = fr"SELECT id, name, surname"
    val from = fr"FROM author"

    def insert(in: Author) =
      sql"""
      INSERT INTO author(id, name, surname)
      VALUES (${in.id}, ${in.name}, ${in.surname})
        """
    
    val delete = fr"DELETE" ++ from
  }

  def selectAll: doobie.ConnectionIO[List[Author]] =
    (q.select ++
      q.from)
      .query[Author]
      .to[List]

  def insert(in: Author): Update0 = q.insert(in).update
  
  def truncate: Update0 = q.delete.update

}
