scalacOptions += "-deprecation"

libraryDependencies +=
  "classycle" % "classycle" % "1.4.2" from
    "https://s3.amazonaws.com/ccl-artifacts/classycle-1.4.2.jar"

resolvers += "netlogo-publish-versioned" at "https://dl.cloudsmith.io/public/netlogo/publish-versioned/maven/"

addSbtPlugin("org.nlogo"          %  "publish-versioned-plugin" % "3.0.0")
addSbtPlugin("org.portable-scala" %  "sbt-scalajs-crossproject" % "1.1.0")
addSbtPlugin("org.scala-js"       %  "sbt-scalajs"              % "0.6.29")
addSbtPlugin("org.scalastyle"     %% "scalastyle-sbt-plugin"    % "1.0.0")
addSbtPlugin("com.timushev.sbt"   %  "sbt-updates"              % "0.5.0")
