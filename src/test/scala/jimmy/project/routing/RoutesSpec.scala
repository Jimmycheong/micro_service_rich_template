package jimmy.project.routing

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{MessageEntity, StatusCodes}
import akka.http.scaladsl.server._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import jimmy.project.Models.{User, Users}
import jimmy.project.services.UserManagementService
import jimmy.project.{Models, UserJsonProtocol}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import spray.json._

import scala.concurrent.Future //

class RoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with RoutesFixture with UserJsonProtocol with ScalaFutures {

  val testRoutes: Route = new Routes(mockedService).allRoutes

  "The routing setup" should {

    "return a list of all users" in {
      Get("/api/users") ~> testRoutes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[String] shouldBe Users(users).toJson.toString
      }
    }

    "add a user" in {
      val user = User(1, "user1")
      val userEntity = Marshal(user).to[MessageEntity].futureValue

      Post("/api/addUser").withEntity(userEntity) ~> testRoutes ~> check {
        status shouldBe StatusCodes.OK
        entityAs[String] shouldBe s"New user added: ${user.toJson.toString}"
      }
    }

  }


}

trait RoutesFixture {

  val users: Seq[User] = Seq(User(1, "Jimmy"), User(2, "James"))

  import scala.concurrent.ExecutionContext.Implicits._
  val mockedService: UserManagementService[Unit] = new UserManagementService[Unit] {
    override def getUsers: Future[Seq[Models.User]] = Future(users)

    override def addUsers(user: User): Future[Unit] = Future(Unit)
  }

}
