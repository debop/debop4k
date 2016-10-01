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

package debop4k.mongodb.spring.boot.autoconfigure

import com.mongodb.ServerAddress
import com.mongodb.WriteConcern
import debop4k.mongodb.spring.boot.autoconfigure.MongodbProperties.Companion
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * MongodbProperties
 * @author sunghyouk.bae@gmail.com
 */
@ConfigurationProperties(prefix = Companion.PREFIX)
class MongodbProperties {

  companion object {
    const val PREFIX = "debop4k.mongodb"
  }

  var host: String? = ServerAddress.defaultHost()
  var port: Int? = ServerAddress.defaultPort()
  var databaseName: String? = "test"
  var username: String? = null
  var password: String? = null

  var options: Options? = Options()

  class Options {
    @JvmField val DEFAULT_WRITE_CONCERN = "ACKNOWLEDGED"

    var connectionTimeout: Int? = 1000 * 10
    var minConnectionPerHost: Int? = 0
    var maxConnectionPerHost: Int? = 100
    var threadsAllowedToBlockForConnectionMultiplier: Int? = 32
    var socketKeepAlive: Boolean? = false
    var sslEnabled: Boolean? = false
    var writeConcernName: String? = DEFAULT_WRITE_CONCERN

    val writeConcern: WriteConcern
      get() {
        if (writeConcernName.isNullOrBlank()) {
          writeConcernName = DEFAULT_WRITE_CONCERN
        }
        try {
          val wc = WriteConcern.valueOf(writeConcernName)
          return wc ?: WriteConcern.ACKNOWLEDGED
        } catch(ignored: Exception) {
          return WriteConcern.ACKNOWLEDGED
        }
      }
  }
}