import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin
lazy val root = (project in file("."))
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    scalaVersion := "2.12.8",
    version := "1.0.0",
    language := "com.github.mercurievv.openapi.codegen.scala.http4s.Http4sCodegen",
  )