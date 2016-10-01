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
import org.apache.ibatis.session.Configuration
import org.apache.ibatis.session.ExecutorType
import org.eclipse.collections.impl.list.mutable.FastList
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.io.IOException

/**
 * MyBatis 용 Spring Boot 환경설정 정보
 * @author debop sunghyouk.bae@gmail.com
 */
@ConfigurationProperties(prefix = MyBatisProperties.PREFIX)
open class MyBatisProperties {

  companion object {
    const val PREFIX = "debop4k.mybatis"
  }

  private val log = loggerOf(javaClass)

  var configLocation: String? = null
  var mapperLocations: Array<String>? = null
  var typeAliasPackage: String? = null
  var typeHandlersPackage: String? = null

  var checkConfiguration: Boolean = false

  /**
   * Execution mode for [org.mybatis.spring.SqlSessionTemplate]
   */
  var executorType: ExecutorType? = null

  @NestedConfigurationProperty
  private val configuration: Configuration? = null

  open fun resolveMapperLocations(): Array<Resource> {
    log.debug("Resolve MyBatis Mapper Locations...")

    val resourceResolver = PathMatchingResourcePatternResolver()
    val resources = FastList.newList<Resource>()

    mapperLocations?.forEach {
      try {
        val mappers = resourceResolver.getResources(it)
        resources.addAll(FastList.newListWith(*mappers))
      } catch (ignored: IOException) {
        log.error("해당 리소스 경로를 찾지 못했습니다. mapperLocation={}", it, ignored)
      }
    }

    log.debug("Resolve MyBatis Mapper Locations")
    return resources.toTypedArray(Resource::class.java)
  }
}