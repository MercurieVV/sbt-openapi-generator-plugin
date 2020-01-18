name := "sbt-swagger-generator-plugin-test"

version := "0.1"

scalaVersion := "2.12.8"
//https://repo1.maven.org/maven2/
resolvers += Resolver.mavenCentral
lazy val root = (project in file("."))
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    scalaVersion := "2.12.8",
    version := "0.1",
  )