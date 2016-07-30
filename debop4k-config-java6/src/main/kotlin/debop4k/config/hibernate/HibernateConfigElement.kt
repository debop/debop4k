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

package debop4k.config.hibernate

import com.typesafe.config.Config
import debop4k.config.ConfigSupport
import debop4k.config.asProperties
import debop4k.config.loadBool
import debop4k.config.loadString
import java.util.*

/**
 * HibernateConfigElement
 * @author debop sunghyouk.bae@gmail.com
 */
open class HibernateConfigElement(override val config: Config) : ConfigSupport {

  val hbm2ddl: String by lazy { config.loadString("hbm2ddl", "none")!! }

  val showSql: Boolean by lazy { config.loadBool("showSql", false) }

  val useSecondCache: Boolean
      by lazy { config.loadBool("useSecondCache", false) }

  val cacheProviderConfig: String
      by lazy { config.loadString("cacheProviderConfig", "")!! }

  val properties: Properties by lazy { config.asProperties() }
}