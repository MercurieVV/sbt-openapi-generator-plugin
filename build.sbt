lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-swagger-generator-plugin",
    organization := "com.github.mercurievv",
    version := "0.0.1-SNAPSHOT",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-      Dsbt.log.noformat").exists(a.startsWith)
    ),
    scriptedBufferLog := true
  )
libraryDependencies += "org.openapitools" % "openapi-generator" % "4.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
