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

package debop4k.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
 * Typesafe Config 라이브러리를 이용하여, 환경설정 정보를 로드합니다.
 * @author sunghyouk.bae@gmail.com
 */
object ConfigLoader {

  private val log = LoggerFactory.getLogger(ConfigLoader::class.java)

  /**
   * 환경설정 파일을 로드합니다. 환경 설정 파일의 rootPath에 해당하는 node 를
   * 가지는 [Config] 인스턴스를 반환합니다.

   * @param resourceBasename 환경설정 파일 경로
   * @param rootPath         최상위 path
   * @return [Config] 인스턴스
   */
  @JvmStatic
  @JvmOverloads
  fun load(resourceBasename: String, rootPath: String? = null): Config {
    log.debug("환경설정 정보를 로드합니다. resourceBasename={}, rootPath={}", resourceBasename, rootPath)
    val config = ConfigFactory.load(resourceBasename)

    return rootPath?.let { path -> config.getConfig(path) } ?: config
//    if (rootPath.isNullOrBlank()) {
//      return config
//    }
//    return config.getConfig(rootPath)
  }

  /**
   * 환경설정 파일을 로드합니다. 환경 설정 파일의 rootPath에 해당하는 node 를
   * 가지는 [Config] 인스턴스를 반환합니다.

   * @param loader           ClassLoader 인스턴스
   * @param resourceBasename 환경설정 파일 경로
   * @param rootPath         최상위 path
   * @return [Config] 인스턴스
   */
  @JvmStatic
  @JvmOverloads
  fun load(loader: ClassLoader, resourceBasename: String, rootPath: String? = null): Config {
    log.debug("환경설정 정보를 로드합니다. loader={}, resourceBasename={}, rootPath={}",
              loader, resourceBasename, rootPath)
    val config = ConfigFactory.load(loader, resourceBasename)

    return rootPath?.let { path -> config.getConfig(path) } ?: config
//    if (rootPath.isNullOrBlank()) {
//      return config
//    }
//    return config.getConfig(rootPath)
  }
}