import sbt._

object Dependencies {
  import Versions._

  val akkaHttpDeps = Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-caching"    % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"          % akkaStreamsVersion,
    "com.typesafe.akka" %% "akka-stream-testkit"  % akkaStreamsVersion % Test
  )

  val slickDeps = Seq(
    "com.typesafe"        % "config"                % "1.3.4",
    "com.typesafe.slick"  %% "slick"                % "3.3.0",
    "com.typesafe.slick"  %% "slick-hikaricp"       % "3.3.0",
    "org.postgresql"      % "postgresql"            % "42.2.5",
    "org.slf4j"           % "slf4j-nop"             % "1.6.4"
  )
  
  val h2DatabaseDeps = Seq(
    "com.h2database" % "h2" % "1.4.199" % Test
  )
}

object Versions {

  val akkaHttpVersion = "10.1.8"
  val akkaStreamsVersion = "2.5.19"
}