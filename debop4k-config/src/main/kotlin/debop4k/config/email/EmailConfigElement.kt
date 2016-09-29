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

package debop4k.config.email

import com.typesafe.config.Config
import debop4k.config.*
import java.util.*

/**
 * EmailConfigElement
 * @author debop sunghyouk.bae@gmail.com
 */
open class EmailConfigElement(override val config: Config) : ServerAddressConfigElement {

  override val port: Int by lazy { config.loadInt("port", 25) }

  val encoding: String by lazy { config.loadString("encoding", "UTF-8")!! }
  val sender: String by lazy { config.loadString("sender", "sender@test.com")!! }

  val protocol by lazy { config.loadString(PATH_PROTOCOL, "smtp")!! }
  val auth by lazy { config.loadBool(PATH_AUTH, true) }
  val startTlsEnable by lazy { config.loadBool(PATH_START_TLS_ENABLE, true) }
  val sslTrust by lazy { config.loadString(PATH_SSL_TRUST, this.host) }

  val properties: Properties
    get() = config.asProperties()

  companion object {
    const val PATH_PROTOCOL = "mail.transport.protocol"
    const val PATH_AUTH = "mail.smtp.auth"
    const val PATH_START_TLS_ENABLE = "mail.smtp.starttls.enable"
    const val PATH_SSL_TRUST = "mail.smtp.ssl.trust"
  }
}