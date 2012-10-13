import sbt._
import Keys._

object MiniBCSBuild extends Build {

  val buildOptions = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0-M7",
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies := Seq(
      "com.typesafe.akka" % "akka-actor" % "2.1-M1",
      "com.typesafe.akka" % "akka-remote" % "2.1-M1",
      "org.joda" % "joda-convert" % "1.2",
      "joda-time" % "joda-time" % "2.1"
    ),
    version := "1.0.0"
  )

  lazy val root = Project(
    "minibcs",
    file("."),
    settings = buildOptions
  ).dependsOn(
    uri("https://github.com/breakpoint-eval/scala-common.git"))

}
