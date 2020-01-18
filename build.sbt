name := "sbt-swagger-generator-plugin"

version := "0.1"

scalaVersion := "2.12.8"
//https://repo1.maven.org/maven2/
resolvers += Resolver.mavenCentral

lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    name := "sbt-swagger-generator-plugin",
    organization := "com.github.mercurievv",
    version := "0.1-SNAPSHOT",
    sbtPlugin := true,
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX", "-      Dsbt.log.noformat").exists(a.startsWith)
    ),
    scriptedBufferLog := false
  )
libraryDependencies += "org.openapitools" % "openapi-generator" % "4.2.2"
//sbtTestDirectory := sourceDirectory.value / "sbt-test"
