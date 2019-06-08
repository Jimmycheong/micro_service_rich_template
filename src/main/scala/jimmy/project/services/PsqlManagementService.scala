package jimmy.project.services

import jimmy.project.Models.User
import jimmy.project.repositories.DbRepository

import scala.concurrent.{ExecutionContext, Future}

class PsqlManagementService[T](dbRepo: DbRepository[T])(implicit executionContext: ExecutionContext)
  extends UserManagementService[T] {

  override def getUsers: Future[Seq[User]] = dbRepo.getAllUsers

  override def addUsers(user: User): Future[T] = dbRepo.addUser(user)
}
