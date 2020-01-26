import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin
lazy val root = (project in file("."))
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    scalaVersion := "2.12.8",
    version := "0.0.1-SNAPSHOT",
    language := "kotlin",
  )