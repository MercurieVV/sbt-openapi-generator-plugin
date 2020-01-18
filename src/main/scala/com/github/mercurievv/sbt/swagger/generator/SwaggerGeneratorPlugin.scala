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

  object autoImport {

    val inputFile = settingKey[File]("inputFile")
    val output    = settingKey[File]("output")
    val config    = settingKey[File]("config")
    val generateSwagger   = taskKey[Unit]("Generates files which includes all files from generator")

/*
    val language                   = settingKey[String]("language")
    val auth                       = settingKey[String]("auth")
    val additionalProperties       = settingKey[Map[String, String]]("additionalProperties")
    val apiPackage                 = settingKey[String]("apiPackage")
    val artifactVersion            = settingKey[String]("artifactVersion")
    val gitRepoId                  = settingKey[String]("gitRepoId")
    val gitUserId                  = settingKey[String]("gitUserId")
    val httpUserAgent              = settingKey[String]("httpUserAgent")
    val ignoreFileOverride         = settingKey[String]("ignoreFileOverride")
    val importMappings             = settingKey[Map[String, String]]("importMappings")
    val instantiationTypes         = settingKey[String]("instantiationTypes")
    val invokerPackage             = settingKey[String]("invokerPackage")
    val languageSpecificPrimitives = settingKey[String]("languageSpecificPrimitives")
    val library                    = settingKey[String]("library")
    val modelNamePrefix            = settingKey[String]("modelNamePrefix")
    val modelNameSuffix            = settingKey[String]("modelNameSuffix")
    val modelPackage               = settingKey[String]("modelPackage")
    val releaseNote                = settingKey[String]("releaseNote")
    val reservedWordsMappings      = settingKey[String]("reservedWordsMappings")
    val skipOverwrite              = settingKey[String]("skipOverwrite")
    val templateDir                = settingKey[String]("templateDir")
    val typeMappings               = settingKey[String]("typeMappings")
    val verbose                    = settingKey[String]("verbose")

    val swaggerClean   = taskKey[Unit]("Clean swagger generated packages")
    val swaggerCodeGen = taskKey[Seq[File]]("Generate code/documentation with swagger")

    val swaggerModelCodeGen = taskKey[Seq[File]]("Generate swagger models and JSON converters")

    val swaggerClientCodeGen = taskKey[Seq[File]]("Generate swagger client class with WS calls to specific routes")
*/
  }

  import autoImport._
  override val requires: Plugins = plugins.JvmPlugin
  override def trigger           = noTrigger

  override lazy val buildSettings = Seq(
//    greeting := "Hi!",
    generateSwagger := generateSwaggerTask.value,
      inputFile := new File(""),
      output := new File("")
  )

  lazy val generateSwaggerTask =
    Def.task {
      val gen: Generator = new DefaultGenerator()
      val input          = new ClientOptInput().openAPI(new OpenAPI())
      val clientOptInput = new CodegenConfigurator().setInputSpec(inputFile.value.getPath).toClientOptInput
      gen.opts(clientOptInput)
      gen.generate()
      new File(output.value, ".")
//      println("aaa")
//      new File(".")
    }
}
