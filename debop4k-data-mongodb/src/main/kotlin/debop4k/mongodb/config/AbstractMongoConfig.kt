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

package debop4k.mongodb.config

import com.mongodb.*
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

/**
 * MongoDB 접속을 위한 Spring Framework의 JavaConfig 용 환경설정 정보
 *
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
abstract class AbstractMongoConfig : AbstractMongoConfiguration() {

  @Throws(Exception::class)
  open override fun mongo(): Mongo {
    return MongoClient(ServerAddress.defaultHost(), mongoOptions())
  }

  open fun mongoOptions(): MongoClientOptions {
    return MongoClientOptions.builder()
        .minConnectionsPerHost(2)
        .connectionsPerHost(32)
        .threadsAllowedToBlockForConnectionMultiplier(32)
        .socketKeepAlive(true)
        .writeConcern(getWriteConcern())
        .build()
  }

  open protected fun getWriteConcern(): WriteConcern {
    return WriteConcern.ACKNOWLEDGED
  }
}