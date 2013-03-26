name := "minibcs"

version := "1.0.0"

scalaVersion := "2.10.1"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

site.settings

ghpages.settings

git.remoteRepo := "git@github.com:breakpoint-eval/minibcs.git"

site.includeScaladoc()
