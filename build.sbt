name := "minibcs"

version := "1.0.0"

scalaVersion := "2.10.0-M7"

scalacOptions ++= Seq(
  "-deprecation"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor" % "2.1-M1",
  "com.typesafe.akka" % "akka-remote" % "2.1-M1",
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1"
)
