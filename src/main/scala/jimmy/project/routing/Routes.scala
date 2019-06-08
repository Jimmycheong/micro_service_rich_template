package jimmy.project.routing

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import jimmy.project.Models.{User, Users}
import jimmy.project.UserJsonProtocol
import jimmy.project.services.UserManagementService
import spray.json._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class Routes[T](umService: UserManagementService[T])(implicit executionContext: ExecutionContext)
  extends UserJsonProtocol with Rejections {

  val allRoutes: Route =
    pathPrefix("api") {
      path("users") {
        get {
          onComplete(umService.getUsers) {
            case Success(users) => complete(Users(users))
            case Failure(error) => complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "no users"))
          }
        }
      } ~
        path("addUser") {
          pathEnd {
            post {
              entity(as[User]) { user =>
                onComplete(umService.addUsers(user)) {
                  case Success(value) => complete(s"New user added: ${user.toJson.toString}")
                  case Failure(exception) => complete("error adding user")
                }
              }
            }
          }
        }
    }
}
