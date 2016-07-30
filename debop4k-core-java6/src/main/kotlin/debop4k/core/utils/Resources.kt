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

package debop4k.core.utils

import debop4k.core.io.stream.toByteArray
import debop4k.core.io.stream.toString
import debop4k.core.java.collections.emptyByteArray
import debop4k.core.shouldNotBeNullOrBlank
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.nio.charset.Charset

object Resources {

  private val log by lazy { LoggerFactory.getLogger(javaClass) }

  @JvmOverloads
  fun classPathResourceStream(path: String, classLoader: ClassLoader = Resources::class.java.classLoader): InputStream? {
    log.debug("리소스 파일을 읽습니다. path={}", path)
    path.shouldNotBeNullOrBlank("path")

    val url = if (path.startsWith("/")) path.drop(1) else path
    return javaClass.classLoader.getResourceAsStream(url)
  }


  @JvmOverloads
  fun getString(path: String,
                charset: Charset = Charsets.UTF_8,
                classLoader: ClassLoader = Resources::class.java.classLoader): String {
    return classPathResourceStream(path)?.use { input ->
      input.toString(charset)
    } ?: ""
  }

  @JvmOverloads
  fun getBytes(path: String,
               charset: Charset = Charsets.UTF_8,
               classLoader: ClassLoader = Resources::class.java.classLoader): ByteArray {
    return classPathResourceStream(path)?.use { input ->
      input.toByteArray()
    } ?: emptyByteArray
  }

}