name := "minibcs"

version := "1.0.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"

site.settings

ghpages.settings

git.remoteRepo := "git@github.com:breakpoint-eval/minibcs.git"

site.includeScaladoc()
