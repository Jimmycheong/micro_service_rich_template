package jimmy.project

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

import scala.concurrent.Future

class HttpClient(implicit actorSystem: ActorSystem) {

  def sendGetRequest(): Future[HttpResponse] = {
    Http().singleRequest(HttpRequest(uri = "https://google.com"))
  }
}
