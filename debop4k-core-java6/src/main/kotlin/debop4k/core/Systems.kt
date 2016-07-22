/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
package debop4k.core

object Systems {

  val RuntimePackage: Package by lazy { Runtime::class.java.`package` }


  /** JVM 버전 */
  val JVM_VERSION: String by lazy {
    RuntimePackage.specificationVersion
  }

  /** JVM 구현 버전 */
  val JVM_IMPLEMENTATION_VERSION: String by lazy {
    RuntimePackage.implementationVersion
  }

  /** JVM 벤더 */
  val JVM_VENDOR: String by lazy {
    RuntimePackage.specificationVendor
  }

  /** JVM 구현 벤더  */
  val JVM_IMPLEMENTATION_VENDOR: String by lazy {
    RuntimePackage.implementationVendor
  }

  /** Java 6 인가 */
  val isJava6: Boolean by lazy { JVM_VERSION.toDouble() == 1.6 }

  /** Java 7 인가 */
  val isJava7: Boolean by lazy { JVM_VERSION.toDouble() == 1.7 }

  /** Java 8 인가 */
  val isJava8: Boolean by lazy { JVM_VERSION.toDouble() == 1.8 }

  val JAVA_HOME: String by lazy { System.getProperty("java.home") }

  val LINE_SEPARATOR: String by lazy { System.getProperty("line.separator") }

  val FILE_SEPARATOR: String by lazy { System.getProperty("file.separator") }

  val PATH_SEPARATOR: String by lazy { System.getProperty("path.separator") }

  val USER_NAME: String by lazy { System.getProperty("user.name") }

  val USER_HOME: String by lazy { System.getProperty("user.hone") }

  val USER_DIR: String by lazy { System.getProperty("user.dir") }

  val TEMP_DIR: String by lazy { System.getProperty("java.io.tmpdir") }

  val JAVA_CLASS_VERSION: String by lazy { System.getProperty("java.class.version") }
}