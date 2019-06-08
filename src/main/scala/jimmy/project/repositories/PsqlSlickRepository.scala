package jimmy.project.repositories

import jimmy.project.Models.User
import jimmy.project.schema.UserSchema
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile.backend

import scala.concurrent.{ExecutionContext, Future}

class PsqlSlickRepository(database: backend.Database) extends DbRepository[Int] {

  val users = TableQuery[UserSchema]

  def getAllUsers(implicit executionContext: ExecutionContext): Future[Seq[User]] =
    database.run(users.result).map { // Map future
      _.map { case (id, username) => // Map user
        User(id, username)
      }
    }

  def addUser(user: User)(implicit executionContext: ExecutionContext): Future[Int] =
    database.run(users += (user.id, user.username))
  // Insert the id and returns the id, figure out why this isn't working
//    database.run((users returning users.map(_.id)) += (user.id, user.username))

}
