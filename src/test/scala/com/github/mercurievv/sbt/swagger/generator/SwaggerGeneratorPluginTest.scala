package com.github.mercurievv.sbt.swagger.generator

import com.github.mercurievv.sbt.swagger.generator.SwaggerGeneratorPlugin.autoImport.{inputFile, language, output}
import io.swagger.v3.oas.models.OpenAPI
import org.openapitools.codegen.{ClientOptInput, DefaultGenerator, Generator}
import org.openapitools.codegen.config.CodegenConfigurator
import sbt.File

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 1/25/2020
 * Time: 9:41 AM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class SwaggerGeneratorPluginTest extends org.scalatest.FunSuite {
  test("run generation"){
    val gen: Generator = new DefaultGenerator()
    val input          = new ClientOptInput().openAPI(new OpenAPI())
    val clientOptInput = new CodegenConfigurator()
      .setInputSpec(new File("swagger.yaml").getPath)
      .setOutputDir(new File("swout").getPath)
      .setGeneratorName("kotlin")
      .toClientOptInput
    println("1")
    gen.opts(clientOptInput)
    println("2")
    try {
      println("3")
      gen.generate()
      println("4")
    } catch {
      case throwable: Throwable => throwable.printStackTrace()
    }
  }
}
