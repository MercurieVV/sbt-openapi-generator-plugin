/*
 * Copyright (c) 2020 the SBT Openapi codegen plugin contributors.
 * See the project homepage at: https://mercurievv.github.io/sbt-openapi-generator-plugin/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mercurievv.sbt.openapi.generator.plugin

import java.util
import java.util.{Map, ServiceLoader}

import _root_.io.swagger.v3.oas.models.OpenAPI
import org.openapitools.codegen.config.CodegenConfigurator
import org.openapitools.codegen._
import sbt.Keys._
import sbt._

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
    lazy val generateSwagger = taskKey[File]("Generates files which includes all files from generator")

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
      val clientOptInput = new CodegenConfigurator() {
        import org.apache.commons.lang3.StringUtils.isNotEmpty
        import org.openapitools.codegen.api.TemplatingEngineAdapter
        import org.openapitools.codegen.config.{Context, GeneratorSettings, WorkflowSettings}
        override def toClientOptInput: ClientOptInput = {
          val context: Context[_]                  = toContext
          val workflowSettings: WorkflowSettings   = context.getWorkflowSettings
          val generatorSettings: GeneratorSettings = context.getGeneratorSettings
          // We load the config via generatorSettings.getGeneratorName() because this is guaranteed to be set
          // regardless of entrypoint (CLI sets properties on this type, config deserialization sets on generatorSettings).
          val config: CodegenConfig = forName(generatorSettings.getGeneratorName, classOf[CodegenConfig])
          if (isNotEmpty(generatorSettings.getLibrary)) config.setLibrary(generatorSettings.getLibrary)
          // TODO: Work toward CodegenConfig having a "WorkflowSettings" property, or better a "Workflow" object which itself has a "WorkflowSettings" property.
          config.setInputSpec(workflowSettings.getInputSpec)
          config.setOutputDir(workflowSettings.getOutputDir)
          config.setSkipOverwrite(workflowSettings.isSkipOverwrite)
          config.setIgnoreFilePathOverride(workflowSettings.getIgnoreFileOverride)
          config.setRemoveOperationIdPrefix(workflowSettings.isRemoveOperationIdPrefix)
          config.setEnablePostProcessFile(workflowSettings.isEnablePostProcessFile)
          config.setEnableMinimalUpdate(workflowSettings.isEnableMinimalUpdate)
          config.setStrictSpecBehavior(workflowSettings.isStrictSpecBehavior)
          val templatingEngine: TemplatingEngineAdapter = TemplatingEngineLoader.byIdentifier(workflowSettings.getTemplatingEngineName)
          config.setTemplatingEngine(templatingEngine)
          // TODO: Work toward CodegenConfig having a "GeneratorSettings" property.
          config.instantiationTypes.putAll(generatorSettings.getInstantiationTypes)
          config.typeMapping.putAll(generatorSettings.getTypeMappings)
          config.importMapping.putAll(generatorSettings.getImportMappings)
          config.languageSpecificPrimitives.addAll(generatorSettings.getLanguageSpecificPrimitives)
          config.reservedWordsMappings.putAll(generatorSettings.getReservedWordMappings)
          config.additionalProperties.putAll(generatorSettings.getAdditionalProperties)
          val serverVariables: util.Map[String, String] = generatorSettings.getServerVariables
          if (!serverVariables.isEmpty) { // This is currently experimental due to vagueness in the specification
            //            LOGGER.warn("user-defined server variable support is experimental.")
            config.serverVariableOverrides.putAll(serverVariables)
          }
          // any other additional properties?
          val templateDir: String = workflowSettings.getTemplateDir
          if (templateDir != null) config.additionalProperties.put(CodegenConstants.TEMPLATE_DIR, workflowSettings.getTemplateDir)
          val input: ClientOptInput = new ClientOptInput().config(config)
          input.openAPI(context.getSpecDocument.asInstanceOf[OpenAPI])
        }

        def forName(name: String, aClass: Class[CodegenConfig]): CodegenConfig = {
          println(name)
          import scala.collection.JavaConverters._
          val services = ServiceLoader.load(aClass, aClass.getClassLoader).asScala
          val superclassServices = ServiceLoader.load(aClass, cloader).asScala
          val configsFound = (services ++ superclassServices).toSet
          configsFound.find(_.getName == name)
            .orElse(Option(cloader.loadClass(name).newInstance().asInstanceOf[CodegenConfig]))
            .orElse(Option(Class.forName(name).getDeclaredConstructor().newInstance().asInstanceOf[CodegenConfig])) match {
            case None => throw new GeneratorNotFoundException("Can't load config class with name '".concat(name) + "'\nAvailable:\n" + configsFound.mkString("\n"))
            case Some(value) => value
          }


          // else try to load directly
          /*
                try {
                  // Try the context classloader first. But, during macro compilation, it's probably wrong, so fallback to this
                  // classloader.
                  Some(Thread.currentThread().getContextClassLoader.loadClass(name))
                } catch {
                  case _: ClassNotFoundException => super.loadClass(name)
                }
                try Class.forName(name).getDeclaredConstructor().newInstance().asInstanceOf[CodegenConfig]
                catch {
                  case e: Exception =>
                    throw new GeneratorNotFoundException("Can't load config class with name '".concat(name) + "'\nAvailable:\n" + availableConfigs.toString, e)
                }
          */
        }

        val cloader = new ClassLoader(this.getClass.getClassLoader) {
          override def loadClass(name: String): Class[_] = {
            try {
              // Try the context classloader first. But, during macro compilation, it's probably wrong, so fallback to this
              // classloader.
              Thread.currentThread().getContextClassLoader.loadClass(name)
            } catch {
              case _: ClassNotFoundException => super.loadClass(name)
            }
          }
        }
      }
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

  class CustomClassloadingCodegenConfigurator extends CodegenConfigurator {
    import org.apache.commons.lang3.StringUtils.isNotEmpty
    import org.openapitools.codegen.api.TemplatingEngineAdapter
    import org.openapitools.codegen.config.{Context, GeneratorSettings, WorkflowSettings}
    override def toClientOptInput: ClientOptInput = {
      val context: Context[_]                  = toContext
      val workflowSettings: WorkflowSettings   = context.getWorkflowSettings
      val generatorSettings: GeneratorSettings = context.getGeneratorSettings
      // We load the config via generatorSettings.getGeneratorName() because this is guaranteed to be set
      // regardless of entrypoint (CLI sets properties on this type, config deserialization sets on generatorSettings).
      val config: CodegenConfig = forName(generatorSettings.getGeneratorName, classOf[CodegenConfig])
      if (isNotEmpty(generatorSettings.getLibrary)) config.setLibrary(generatorSettings.getLibrary)
      // TODO: Work toward CodegenConfig having a "WorkflowSettings" property, or better a "Workflow" object which itself has a "WorkflowSettings" property.
      config.setInputSpec(workflowSettings.getInputSpec)
      config.setOutputDir(workflowSettings.getOutputDir)
      config.setSkipOverwrite(workflowSettings.isSkipOverwrite)
      config.setIgnoreFilePathOverride(workflowSettings.getIgnoreFileOverride)
      config.setRemoveOperationIdPrefix(workflowSettings.isRemoveOperationIdPrefix)
      config.setEnablePostProcessFile(workflowSettings.isEnablePostProcessFile)
      config.setEnableMinimalUpdate(workflowSettings.isEnableMinimalUpdate)
      config.setStrictSpecBehavior(workflowSettings.isStrictSpecBehavior)
      val templatingEngine: TemplatingEngineAdapter = TemplatingEngineLoader.byIdentifier(workflowSettings.getTemplatingEngineName)
      config.setTemplatingEngine(templatingEngine)
      // TODO: Work toward CodegenConfig having a "GeneratorSettings" property.
      config.instantiationTypes.putAll(generatorSettings.getInstantiationTypes)
      config.typeMapping.putAll(generatorSettings.getTypeMappings)
      config.importMapping.putAll(generatorSettings.getImportMappings)
      config.languageSpecificPrimitives.addAll(generatorSettings.getLanguageSpecificPrimitives)
      config.reservedWordsMappings.putAll(generatorSettings.getReservedWordMappings)
      config.additionalProperties.putAll(generatorSettings.getAdditionalProperties)
      val serverVariables: util.Map[String, String] = generatorSettings.getServerVariables
      if (!serverVariables.isEmpty) { // This is currently experimental due to vagueness in the specification
        //            LOGGER.warn("user-defined server variable support is experimental.")
        config.serverVariableOverrides.putAll(serverVariables)
      }
      // any other additional properties?
      val templateDir: String = workflowSettings.getTemplateDir
      if (templateDir != null) config.additionalProperties.put(CodegenConstants.TEMPLATE_DIR, workflowSettings.getTemplateDir)
      val input: ClientOptInput = new ClientOptInput().config(config)
      input.openAPI(context.getSpecDocument.asInstanceOf[OpenAPI])
    }

    def forName(name: String, aClass: Class[CodegenConfig]): CodegenConfig = {
      println(name)
      import scala.collection.JavaConverters._
      val services = ServiceLoader.load(aClass, aClass.getClassLoader).asScala
      val superclassServices = ServiceLoader.load(aClass, cloader).asScala
      val configsFound = (services ++ superclassServices).toSet
      configsFound.find(_.getName == name)
        .orElse(Option(cloader.loadClass(name).asInstanceOf[CodegenConfig]))
        .orElse(Option(Class.forName(name).getDeclaredConstructor().newInstance().asInstanceOf[CodegenConfig])) match {
        case None => throw new GeneratorNotFoundException("Can't load config class with name '".concat(name) + "'\nAvailable:\n" + configsFound.mkString("\n"))
        case Some(value) => value
      }


      // else try to load directly
/*
      try {
        // Try the context classloader first. But, during macro compilation, it's probably wrong, so fallback to this
        // classloader.
        Some(Thread.currentThread().getContextClassLoader.loadClass(name))
      } catch {
        case _: ClassNotFoundException => super.loadClass(name)
      }
      try Class.forName(name).getDeclaredConstructor().newInstance().asInstanceOf[CodegenConfig]
      catch {
        case e: Exception =>
          throw new GeneratorNotFoundException("Can't load config class with name '".concat(name) + "'\nAvailable:\n" + availableConfigs.toString, e)
      }
*/
    }

  }
  val cloader = new ClassLoader(this.getClass.getClassLoader) {
    override def loadClass(name: String): Class[_] = {
      try {
        // Try the context classloader first. But, during macro compilation, it's probably wrong, so fallback to this
        // classloader.
        Thread.currentThread().getContextClassLoader.loadClass(name)
      } catch {
        case _: ClassNotFoundException => super.loadClass(name)
      }
    }
  }
}
