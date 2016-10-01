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

package debop4k.data.mybatis.spring.boot.autoconfigure

import debop4k.core.loggerOf
import org.apache.ibatis.io.VFS
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import java.net.URI
import java.net.URL

/**
 * MyBatis 의 [VFS] 를 상속받아 SpringBoot 용으로 구현
 * @author sunghyouk.bae@gmail.com
 */
class SpringBootVFS : VFS() {

  private val log = loggerOf(javaClass)

  private val resourceResolver: ResourcePatternResolver = PathMatchingResourcePatternResolver(javaClass.classLoader)

  override fun isValid(): Boolean = true

  override fun list(url: URL?, path: String?): List<String> {
    val resources = resourceResolver.getResources("classpath:$path/**/*.class")
    val resourcePaths = mutableListOf<String>()
    resources.forEach {
      resourcePaths.add(preseveSubpackageName(it.uri, path!!))
    }
    return resourcePaths
  }

  private fun preseveSubpackageName(uri: URI, rootPath: String): String {
    val uriStr = uri.toString()
    val startIndex = uriStr.indexOf(rootPath)
    return uriStr.substring(startIndex)
  }

}