resolvers += Classpaths.typesafeResolver

resolvers ++= Seq(
    "scct-github-repository" at "http://mtkopone.github.com/scct/maven-repo",
    "sonatype-oss-repo" at "https://oss.sonatype.org/content/groups/public/",
    "jgit-repo" at "http://download.eclipse.org/jgit/maven"
)

addSbtPlugin("reaktor" % "sbt-scct" % "0.2-SNAPSHOT")

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.0")

addSbtPlugin("com.github.theon" %% "xsbt-coveralls-plugin" % "0.0.2-SNAPSHOT")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.1")

