//name := "sbt-swagger-generator-plugin-test"

//version := "0.0.1-SNAPSHOT"

//scalaVersion := "2.12.8"
//https://repo1.maven.org/maven2/
//resolvers += Resolver.mavenCentral
//val swaggerSettings = Seq(
//  language := "scala",
//)

import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin
lazy val root = (project in file("."))
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    scalaVersion := "2.12.8",
    version := "0.0.1-SNAPSHOT",
    language := "kotlin",
  )