package jimmy.project.routing

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.RejectionHandler

trait Rejections {

  implicit def myRejectionHandler: RejectionHandler =
    RejectionHandler.newBuilder()
      .handleNotFound {
        extractUnmatchedPath { p: Uri.Path =>
          complete((NotFound, s"The path you requested [${p}] does not exist."))
        }
      }
      .result()

}
