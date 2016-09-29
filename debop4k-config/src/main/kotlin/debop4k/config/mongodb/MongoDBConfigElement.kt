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

package debop4k.config.mongodb

import com.typesafe.config.Config
import debop4k.config.ServerAddressConfigElement
import debop4k.config.loadInt
import debop4k.config.loadString

/**
 * MongoDB 서버 접석을 위한 환경 설정 정보
 * <pre>
 *   <code>
 *     mongo {
 *       database = "local"
 *     }
 *   </code>
 * </pre>
 *
 * @author sunghyouk.bae @gmail.com
 */
open class MongoDBConfigElement(override val config: Config) : ServerAddressConfigElement {

  override val port: Int by lazy { config.loadInt("port", 27017) }

  val database: String by lazy { config.loadString("database", "test")!! }
  val username: String? by lazy { config.loadString("username", null) }
  val password: String? by lazy { config.loadString("password", null) }

}