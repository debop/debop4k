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

package debop4k.core.utils

import java.util.*

/**
 * System 관련 속성 정보
 *
 * @author debop sunghyouk.bae@gmail.com
 */
object Systems {

  @JvmField val ProcessCount: Int = Runtime.getRuntime().availableProcessors()

  @JvmField val RuntimePackage: Package = Runtime::class.java.`package`

  @JvmStatic val systemProps: Properties by lazy { System.getProperties() }

  /** JVM 버전 */
  @JvmStatic val javaVersion: String by lazy {
    RuntimePackage.specificationVersion
  }

  /** JVM 구현 버전 */
  @JvmStatic val javaImplementationVersion: String by lazy {
    RuntimePackage.implementationVersion
  }

  /** JVM 벤더 */
  @JvmStatic val javaVendor: String by lazy {
    RuntimePackage.specificationVendor
  }

  /** JVM 구현 벤더  */
  @JvmStatic val javaImplementationVendor: String by lazy {
    RuntimePackage.implementationVendor
  }

  /** Java 6 인가 */
  @JvmStatic val isJava6: Boolean by lazy { javaVersion.toDouble() == 1.6 }

  /** Java 7 인가 */
  @JvmStatic val isJava7: Boolean by lazy { javaVersion.toDouble() == 1.7 }

  /** Java 8 인가 */
  @JvmStatic val isJava8: Boolean by lazy { javaVersion.toDouble() == 1.8 }

  @JvmStatic val javaHome: String by lazy { System.getProperty("java.home") }

  @JvmStatic val lineSeparator: String by lazy { System.getProperty("line.separator") }

  @JvmStatic val fileSeparator: String by lazy { System.getProperty("file.separator") }

  @JvmStatic val pathSeparator: String by lazy { System.getProperty("path.separator") }

  @JvmStatic val userName: String by lazy { System.getProperty("user.name") }

  @JvmStatic val userHome: String by lazy { System.getProperty("user.hone") }

  @JvmStatic val userDir: String by lazy { System.getProperty("user.dir") }

  @JvmStatic val tempDir: String by lazy { System.getProperty("java.io.tmpdir") }

  @JvmStatic val javaClassVersion: String by lazy { System.getProperty("java.class.version") }

  const val USER_DIR = "user.dir"
  const val USER_NAME = "user.name"
  const val USER_HOME = "user.home"
  const val JAVA_HOME = "java.home"
  const val TEMP_DIR = "java.io.tmpdir"
  const val OS_NAME = "os.name"
  const val OS_VERSION = "os.version"
  const val JAVA_VERSION = "java.version"
  const val JAVA_SPECIFICATION_VERSION = "java.specification.version"
  const val JAVA_VENDOR = "java.vendor"
  const val JAVA_CLASSPATH = "java.class.path"
  const val PATH_SEPARATOR = "path.separator"
  const val HTTP_PROXY_HOST = "http.proxyHost"
  const val HTTP_PROXY_PORT = "http.proxyPort"
  const val HTTP_PROXY_USER = "http.proxyUser"
  const val HTTP_PROXY_PASSWORD = "http.proxyPassword"
  const val FILE_ENCODING = "file.encoding"
  const val SUN_BOOT_CLASS_PATH = "sun.boot.class.path"
}