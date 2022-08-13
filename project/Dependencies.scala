import sbt._

object Dependencies {
  lazy val akkaVersion = "2.6.19"
  lazy val akkaHttpVersion = "10.2.9"

  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  lazy val jsonData = "org.json4s" %% "json4s-native" % "4.0.5"
  lazy val jacksonData = "org.json4s" %% "json4s-jackson" % "4.0.5"
}
