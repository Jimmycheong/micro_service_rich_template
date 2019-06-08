import Dependencies._

name := "micro_service_rich_template"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions += "-Ypartial-unification"


libraryDependencies ++= Seq(
  "com.typesafe.scala-logging"  %% "scala-logging"    % "3.9.2",
  "org.flywaydb"                % "flyway-core"       % "5.2.4",
  "org.scalatest"               %% "scalatest"        % "3.0.5" % Test
) ++ akkaHttpDeps ++ slickDeps ++ h2DatabaseDeps ++ catsDeps