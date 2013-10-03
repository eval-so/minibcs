import scalariform.formatter.preferences._

name := "minibcs"

version := "1.0.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

libraryDependencies ++= Seq(
  "commons-io" % "commons-io" % "2.4",
  "commons-codec" % "commons-codec" % "1.7",
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.1.2",
  "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"
)

testOptions in Test += Tests.Argument("-oDS")

site.settings

ghpages.settings

git.remoteRepo := "git@github.com:eval-so/minibcs.git"

site.includeScaladoc()

seq(sbt.scct.ScctPlugin.instrumentSettings : _*)

defaultScalariformSettings ++ Seq(
  ScalariformKeys.preferences := FormattingPreferences().
  setPreference(PreserveDanglingCloseParenthesis, true).
  setPreference(MultilineScaladocCommentsStartOnFirstLine, true).
  setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true)
)
