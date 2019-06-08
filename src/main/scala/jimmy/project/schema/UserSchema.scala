package jimmy.project.schema

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

/**
  * Represents the schema for query
  */

class UserSchema(tag: Tag) extends Table[(Int, String)](tag, "users") {
  def id: Rep[Int]                   = column[Int]("id")
  def username: Rep[String]          = column[String]("username")
  def * : ProvenShape[(Int, String)] = (id, username)
}
