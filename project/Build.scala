import sbt._
import Keys._

object MiniBCSBuild extends Build {

  val buildOptions = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.0-M7",
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    resolvers += "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    resolvers += "Central" at "https://oss.sonatype.org/content/repositories/releases/",
    libraryDependencies := Seq(
      // Update these to use %% when 2.10 goes live because I have no idea how to force it to use that.
      "com.typesafe.akka" % "akka-actor_2.10.0-M7" % "2.1-M2",
      "com.typesafe.akka" % "akka-remote_2.10.0-M7" % "2.1-M2",
      "org.joda" % "joda-convert" % "1.2",
      "joda-time" % "joda-time" % "2.1"
    ),
    version := "1.0.0",
    scalacOptions ++= Seq(
      "-deprecation",
      "-feature"
    )
  )

  lazy val root = Project(
    "minibcs",
    file("."),
    settings = buildOptions
  ).dependsOn(
    uri("https://github.com/breakpoint-eval/scala-common.git"))

}
