package jimmy.project.services

import jimmy.project.Models
import jimmy.project.Models.User
import jimmy.project.repositories.DbRepository
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.{ExecutionContext, Future}

class PsqlManagementServiceSpec
  extends WordSpec
    with Matchers
    with PsqlManagementServiceFixture
    with ScalaFutures {
  import scala.concurrent.ExecutionContext.Implicits._

  val psqlManagementService = new PsqlManagementService(dbRepo)

  "The Service should" should {

    "return a list of users" in {
      psqlManagementService.getUsers.futureValue shouldBe Seq(User(1,"user1"), User(2, "user2"))
    }

    "a new user to the database" in {
      val newUser = User(1, "newGuy")
      psqlManagementService.addUsers(newUser)

      dbRepo.getAllUsers.futureValue shouldBe  existingUsers ++ Seq(newUser)
    }
  }

}

trait PsqlManagementServiceFixture {

  val existingUsers: Seq[User] = Seq(User(1, "user1"), User(2, "user2"))

  val dbRepo: DbRepository[Unit] = new DbRepository[Unit] {
    var users: Seq[User] = existingUsers

    override def getAllUsers(implicit executionContext: ExecutionContext): Future[Seq[Models.User]] = Future(users)

    override def addUser(user: Models.User)(implicit executionContext: ExecutionContext): Future[Unit] = {
      users ++= Seq(user)
      Future {Unit}
    }
  }

}