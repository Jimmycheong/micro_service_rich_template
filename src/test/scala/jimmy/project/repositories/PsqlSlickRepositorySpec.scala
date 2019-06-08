package jimmy.project.repositories

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging
import jimmy.project.DbMigrations
import jimmy.project.Models.User
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException
import org.scalatest.concurrent.{PatienceConfiguration, ScalaFutures}
import org.scalatest.time._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.ExecutionContext.Implicits._

class PsqlSlickRepositorySpec extends WordSpec with Matchers
  with ScalaFutures
  with PsqlSlickRepositoryFixture
  with BeforeAndAfterAll
  with PatienceConfiguration
  with StrictLogging
{
  implicit override val patienceConfig: PatienceConfig =
    PatienceConfig(timeout = scaled(Span(2, Seconds)), interval = scaled(Span(5, Millis)))

  override def beforeAll {
    val migrationTestScripts  = config.getString("psqldb.migrationsFolder")
    val migrationsDone        = DbMigrations.performMigrations(config, Seq("/db/migrations", migrationTestScripts))
    println(s"No. of total db migrations: $migrationsDone")
  }

  val psqlSlickRepository = new PsqlSlickRepository(db)

  "The repository" should {

    "get a list of users" in {
      psqlSlickRepository.getAllUsers.futureValue shouldBe existingUsers
    }

    "add a new user to the database" in {
      psqlSlickRepository.getAllUsers.futureValue shouldBe existingUsers

      val returnedId = psqlSlickRepository.addUser(userId3).futureValue

      returnedId shouldBe 1 // One row returned
      psqlSlickRepository.getAllUsers.futureValue shouldBe existingUsers ++ Seq(userId3)
    }

  }

}

trait PsqlSlickRepositoryFixture {
  import slick.jdbc.PostgresProfile.api._
  val config: Config = ConfigFactory.load()

  val db = Database.forURL(
    url       = config.getString("psqldb.jdbcUrl"),
    user      = config.getString("psqldb.properties.user"),
    password  = config.getString("psqldb.properties.password")
  )

  val userId1 = User(1, "John")
  val userId2 = User(2, "Sarah")
  val userId3 = User(3, "Billy")

  val existingUsers: Seq[User] = Seq(
    userId1,
    userId2
  )


}
