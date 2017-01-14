name := """htwg-checkers-server"""

version := "1.0-SNAPSHOT"


lazy val checkers = RootProject(file("../htwg-scala-checkers"))

lazy val root = (project in file(".")).enablePlugins(PlayScala).dependsOn(checkers)

//lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.akka" % "akka-remote_2.11" % "2.4.12"
)

