/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package debop4k.web.tomcat

import debop4k.core.loggerOf
import org.apache.catalina.Context
import org.apache.catalina.connector.Connector
import org.apache.catalina.core.*
import org.apache.catalina.mbeans.GlobalResourcesLifecycleListener
import org.apache.catalina.startup.Tomcat
import org.springframework.boot.CommandLineRunner
import java.io.File

/**
 * TomcatLauncher
 * @author sunghyouk.bae@gmail.com
 */
open class TomcatLauncher : CommandLineRunner {

  private val log = loggerOf(javaClass)

  val port: Int get() = 8080
  val contextPath: String get() = "/"
  val resourceBase: String get() = "src/main/webapp"
  val protocol: String get() = "org.apache.coyote.http11.Http11Protocol"

  @Suppress("UNUSED_PARAMETER")
  protected fun initContext(ctx: Context) {
    // override method to additional initialization.
  }

  override fun run(vararg args: String?) {
    val tomcat = Tomcat().apply {
      server.addLifecycleListener(AprLifecycleListener())
      server.addLifecycleListener(JreMemoryLeakPreventionListener())
      server.addLifecycleListener(GlobalResourcesLifecycleListener())
      server.addLifecycleListener(ThreadLocalLeakPreventionListener())
    }

    val connector = Connector(protocol)
    connector.port = port
    connector.uriEncoding = Charsets.UTF_8.toString()
    connector.enableLookups = false

    tomcat.service.addConnector(connector)
    tomcat.connector = connector

    val context = tomcat.addWebapp(contextPath, File(resourceBase).absolutePath)
    initContext(context)

    log.info("Start Tomcat Web server... port={}, root={}", port, contextPath)

    tomcat.start()
    tomcat.server.await()
  }
}