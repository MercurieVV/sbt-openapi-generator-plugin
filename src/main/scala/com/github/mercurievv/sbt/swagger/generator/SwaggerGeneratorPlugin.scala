package com.github.mercurievv.sbt.swagger.generator

import sbt._
import Keys._
import org.openapitools.codegen.{ClientOptInput, DefaultGenerator, Generator}
import _root_.io.swagger.v3.oas.models.OpenAPI
import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin.autoImport.inputFile
import org.openapitools.codegen.config.CodegenConfigurator

/**
  * Created with IntelliJ IDEA.
  * User: Victor Mercurievv
  * Date: 12/22/2019
  * Time: 12:37 PM
  * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
  */
object SwaggerGeneratorPlugin extends AutoPlugin {
  /*
//  override lazy val projectSettings = Seq(commands)
  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    tOutput := generateSwagger.value
  )
   */

  trait PluginKeys {

    lazy val inputFile       = settingKey[File]("inputFile")
    lazy val output          = settingKey[File]("output")
    lazy val config          = settingKey[File]("config")
    lazy val generateSwagger = taskKey[Unit]("Generates files which includes all files from generator")

    lazy val language                   = settingKey[String]("language")
    lazy val auth                       = settingKey[String]("auth")
    lazy val additionalProperties       = settingKey[Map[String, String]]("additionalProperties")
    lazy val apiPackage                 = settingKey[String]("apiPackage")
    lazy val artifactVersion            = settingKey[String]("artifactVersion")
    lazy val gitRepoId                  = settingKey[String]("gitRepoId")
    lazy val gitUserId                  = settingKey[String]("gitUserId")
    lazy val httpUserAgent              = settingKey[String]("httpUserAgent")
    lazy val ignoreFileOverride         = settingKey[String]("ignoreFileOverride")
    lazy val importMappings             = settingKey[Map[String, String]]("importMappings")
    lazy val instantiationTypes         = settingKey[String]("instantiationTypes")
    lazy val invokerPackage             = settingKey[String]("invokerPackage")
    lazy val languageSpecificPrimitives = settingKey[String]("languageSpecificPrimitives")
    lazy val library                    = settingKey[String]("library")
    lazy val modelNamePrefix            = settingKey[String]("modelNamePrefix")
    lazy val modelNameSuffix            = settingKey[String]("modelNameSuffix")
    lazy val modelPackage               = settingKey[String]("modelPackage")
    lazy val releaseNote                = settingKey[String]("releaseNote")
    lazy val reservedWordsMappings      = settingKey[String]("reservedWordsMappings")
    lazy val skipOverwrite              = settingKey[String]("skipOverwrite")
    lazy val templateDir                = settingKey[String]("templateDir")
    lazy val typeMappings               = settingKey[String]("typeMappings")
    lazy val verbose                    = settingKey[String]("verbose")

    lazy val swaggerClean   = taskKey[Unit]("Clean swagger generated packages")
    lazy val swaggerCodeGen = taskKey[Seq[File]]("Generate code/documentation with swagger")

    lazy val swaggerModelCodeGen = taskKey[Seq[File]]("Generate swagger models and JSON converters")

    lazy val swaggerClientCodeGen = taskKey[Seq[File]]("Generate swagger client class with WS calls to specific routes")
  }
  object autoImport extends PluginKeys

  import autoImport._
  override val requires: Plugins = plugins.JvmPlugin
  override def trigger           = noTrigger

  override lazy val projectSettings = Seq(
      generateSwagger := generateSwaggerTask.value,
      inputFile := new File("swagger.yaml"),
      output := target.value / "swagger"
  )

  lazy val generateSwaggerTask =
    Def.task {
      val gen: Generator = new DefaultGenerator()
      val clientOptInput = new CodegenConfigurator()
        .setInputSpec(inputFile.value.getPath)
        .setOutputDir(output.value.getPath)
        .setGeneratorName(language.value)
        .toClientOptInput
      gen.opts(clientOptInput)
      try {
        gen.generate()
      } catch {
        case throwable: Throwable => throwable.printStackTrace()
      }
      output.value
    }
}
