lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-openapi-generator-plugin",
    organization := "com.github.mercurievv",
    version := "1.0.0",
    sbtPlugin := true,
    publishMavenStyle := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-      Dsbt.log.noformat").exists(a.startsWith)
    ),
    scriptedBufferLog := true,

    publishTo := {
      println(sys.env("OSS_SONATYPE_LOGIN"))
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    credentials := List(Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", sys.env("OSS_SONATYPE_LOGIN"), sys.env("OSS_SONATYPE_PASSWORD")))
  )

libraryDependencies += "org.openapitools" % "openapi-generator" % "4.2.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % Test
