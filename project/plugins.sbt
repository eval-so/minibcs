resolvers += Classpaths.typesafeResolver

resolvers ++= Seq(
    "sonatype-oss-repo" at "https://oss.sonatype.org/content/groups/public/",
    "jgit-repo" at "http://download.eclipse.org/jgit/maven"
)

//addSbtPlugin("com.github.scct" % "sbt-scct" % "0.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.5.0")

//addSbtPlugin("com.github.theon" %% "xsbt-coveralls-plugin" % "0.0.2-SNAPSHOT")

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.0.1")

