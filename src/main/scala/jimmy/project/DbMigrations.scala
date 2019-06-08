package jimmy.project

import java.util.Properties

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.{MigrationInfo, MigrationInfoService}
import slick.jdbc.DriverDataSource

object DbMigrations extends LazyLogging {

  def performMigrations(dbConfig: Config, migrationScripts: Seq[String] = Seq("/db/migration")): Int = {
    logger.info("Performing dbmigration")

    // MAKE SURE TO ACTUALLY CREATE TWO FOLDERS INSTEAD OF ONE called db.test_scripts
    val flyway: Flyway =
      Flyway
        .configure
        .dataSource(new DriverDataSource(
          url = dbConfig.getString("psqldb.jdbcUrl"),
          user = dbConfig.getString("psqldb.properties.user"),
          password = dbConfig.getString("psqldb.properties.password"),
          driverClassName = dbConfig.getString("psqldb.dataSourceClass")
        ))
//        .locations("/db/test_scripts")
        .locations(migrationScripts: _*)
        .load

    val infoService: MigrationInfoService = flyway.info()

    checkMigrationStatus(infoService)
    infoService.all().foreach { sth: MigrationInfo =>
      println(sth.getScript)
    }

    val numberOfMigrations = flyway.migrate() // perform dbmigration

    logger.info(s"Completed dbmigration")
    numberOfMigrations
  }

  def checkMigrationStatus(migrationInfoService: MigrationInfoService): Unit = {
    println(s"Number applied: ${migrationInfoService.applied().length}")
    println(s"Number pending: ${migrationInfoService.pending().length}")
    println(s"Number all: ${migrationInfoService.all().length}")

  }

}