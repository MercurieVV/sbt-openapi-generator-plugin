---
layout: home
position: 1
---

# SBT Openapi codegen plugin

[![Build status](https://github.com/MercurieVV/sbt-openapi-generator-plugin/workflows/build/badge.svg?branch=master)](https://github.com/MercurieVV/sbt-openapi-generator-plugin/actions?query=branch%3Amaster+workflow%3Abuild) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.mercurievv/sbt-openapi-generator-plugin-core_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.mercurievv/sbt-openapi-generator-plugin-core_2.13)

SBT Plugin to generate code from swagger/openapi specs

## Usage

In `project/plugins.sbt` need to initialize plugin

```scala
addSbtPlugin("com.github.mercurievv" % "sbt-openapi-generator-plugin" % <version>)
```

In `build.sbt` setup plugin properties:
```scala
import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin
lazy val root = (project in file("."))
  .enablePlugins(SwaggerGeneratorPlugin)
  .settings(
    scalaVersion := "2.12.8",
    version := "1.0.0",
    language := "scala",
  )
```
Plugin properties:
```
inputFile
output
config
language
auth
additionalProperties
apiPackage
artifactVersion
gitRepoId
gitUserId
httpUserAgent
ignoreFileOverride
importMappings
instantiationTypes
invokerPackage
languageSpecificPrimitives
library
modelNamePrefix
modelNameSuffix
modelPackage
releaseNote
reservedWordsMappings
skipOverwrite
templateDir
typeMappings
verbose
```
Props description can be found on Openapi codegen page: https://github.com/OpenAPITools/openapi-generator#3---usage