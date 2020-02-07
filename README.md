[![Stable releaases in the Maven store](https://img.shields.io/maven-metadata/v/https/repo1.maven.org/maven2/com/github/mercurievv/sbt-openapi-generator-plugin/maven-metadata.xml.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.github.mercurievv%22%20AND%20a%3A%22sbt-openapi-generator-plugin%22)
![Scala CI](https://github.com/MercurieVV/sbt-openapi-generator-plugin/workflows/Scala%20CI/badge.svg)
# sbt-openapi-generator-plugin
SBT Plugin to generate code from openapi/swagger specs

SBT
project/plugins.sbt
```
addSbtPlugin("com.github.mercurievv" % "sbt-openapi-generator-plugin" % "1.0.0")
```
build.sbt
```
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    language := "scala",
  )
```
