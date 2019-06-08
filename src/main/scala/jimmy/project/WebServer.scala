package jimmy.project

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import jimmy.project.repositories.{DbRepository, PsqlSlickRepository}
import jimmy.project.routing.Routes
import jimmy.project.services.{PsqlManagementService, UserManagementService}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object WebServer {

  def migrateDb(): Unit = {

    val config = ConfigFactory.load()
    DbMigrations.performMigrations(config)
  }

  def main(args: Array[String]) {
    import slick.jdbc.PostgresProfile.api._

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher // needed for the future flatMap/onComplete in the end

    migrateDb()

    val database = Database.forConfig("psqldb")
    val psqlDbRepo: PsqlSlickRepository                   = new PsqlSlickRepository(database)
    val psqlManagementService: UserManagementService[Int] = new PsqlManagementService(psqlDbRepo)

    val routes = new Routes(psqlManagementService)

    try {
      val bindingFuture = Http().bindAndHandle(routes.allRoutes, "localhost", 8080)
      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine() // let it run until user presses return
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ => system.terminate()) // and shutdown when done

    } finally {
      database.close
    }

  }
}
