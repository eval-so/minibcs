name := "minibcs"

version := "1.0.0"

scalaVersion := "2.9.2"

scalacOptions ++= Seq(
  "-deprecation"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.joda" % "joda-convert" % "1.2",
  "joda-time" % "joda-time" % "2.1"
)
