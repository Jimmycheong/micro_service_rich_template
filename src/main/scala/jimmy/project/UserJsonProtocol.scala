package jimmy.project

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import jimmy.project.Models.{User, Users}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait UserJsonProtocol extends SprayJsonSupport {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat: RootJsonFormat[User] = jsonFormat2(User)
  implicit val usersJsonFormat: RootJsonFormat[Users] = jsonFormat1(Users)
}
