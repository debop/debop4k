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

import com.mongodb.*
import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.mongodb.spring.boot.autoconfigure.MongodbProperties.Options
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import javax.inject.Inject

/**
 * MongoDB 환경설정을 Spring Boot AutoConfiguration 을 이용하여 수행할 수 있도록 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Configuration
@EnableConfigurationProperties(MongodbProperties::class)
abstract class MongodbAutoConfiguration : AbstractMongoConfiguration() {

  private val log = loggerOf(javaClass)

  @Inject val mongoProps: MongodbProperties = uninitialized()

  override fun getDatabaseName(): String {
    return mongoProps.databaseName ?: "test"
  }

  override fun mongo(): Mongo {
    val address = ServerAddress(mongoProps.host!!, mongoProps.port!!)
    return MongoClient(address, getMongoOptions())
  }

  fun getMongoOptions(): MongoClientOptions {
    val options = mongoProps.options ?: Options()
    log.debug("mongo options={}", options)

    return MongoClientOptions.builder()
        .minConnectionsPerHost(options.minConnectionPerHost!!)
        .connectionsPerHost(options.maxConnectionPerHost!!)
        .threadsAllowedToBlockForConnectionMultiplier(options.threadsAllowedToBlockForConnectionMultiplier!!)
        .socketKeepAlive(options.socketKeepAlive!!)
        .writeConcern(options.writeConcern)
        .connectTimeout(options.connectionTimeout!!)
        .build()
  }
}