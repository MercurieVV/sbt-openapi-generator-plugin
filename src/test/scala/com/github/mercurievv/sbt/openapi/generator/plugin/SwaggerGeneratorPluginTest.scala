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

import com.github.mercurievv.sbt.openapi.generator.plugin.SwaggerGeneratorPlugin.CustomClassloadingCodegenConfigurator
import org.openapitools.codegen.{DefaultGenerator, Generator}
import sbt.File

/**
 * Created with IntelliJ IDEA.
 * User: Victor Mercurievv
 * Date: 1/25/2020
 * Time: 9:41 AM
 * Contacts: email: mercurievvss@gmail.com Skype: 'grobokopytoff' or 'mercurievv'
 */
class SwaggerGeneratorPluginTest extends org.scalatest.funsuite.AnyFunSuite {
  test("run generation"){
    val gen: Generator = new DefaultGenerator()
    val clientOptInput = new CustomClassloadingCodegenConfigurator()
      .setInputSpec(new File("src/sbt-test/sbt-swagger-generator-plugin/simple/swagger.yaml").getPath)
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
