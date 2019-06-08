package jimmy.project.repositories

import jimmy.project.Models.User

import scala.concurrent.{ExecutionContext, Future}

trait DbRepository[T] {

  def getAllUsers(implicit executionContext: ExecutionContext): Future[Seq[User]]

  def addUser(user: User)(implicit executionContext: ExecutionContext): Future[T]

}
