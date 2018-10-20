package repository

import doobie._
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import io.circe.Json
import model.AuthorAudit
import org.postgresql.util.PGobject
import io.circe.parser._
import cats.syntax.either._

class AuthorAuditRepository {
  implicit val JsonMeta: Meta[Json] =
    Meta.other[PGobject]("jsonb").xmap[Json](
      a => parse(a.getValue).leftMap[Json](e => throw e).merge,
      a => {
        val o = new PGobject
        o.setType("jsonb")
        o.setValue(a.noSpaces)
        o
      }
    )

  private object q {

    val select = fr"SELECT id, author_id, created_by, created_at, data"
    val from = fr"FROM author_audit"

    def insert(in: AuthorAudit) =
      sql"""
      INSERT INTO author_audit(created_by, author_id, created_at, data)
      VALUES (${in.createdBy}, ${in.authorId}, ${in.createdAt}, ${in.data})
        """

    val delete = fr"DELETE" ++ from
  }


  def insert(in: AuthorAudit): Update0 = q.insert(in).update
}
