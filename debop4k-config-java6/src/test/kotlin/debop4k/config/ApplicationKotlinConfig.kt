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

package debop4k.config

import com.typesafe.config.Config
import debop4k.config.database.DatabaseConfigSupport
import debop4k.config.email.EmailConfigSupport
import debop4k.config.hibernate.HibernateConfigSupport
import debop4k.config.mongodb.MongoDBConfigSupport
import debop4k.config.redis.RedisConfigSupport
import debop4k.config.redis.RedissonConfigSupport

/**
 * ApplicationKotlinConfig
 * @author debop sunghyouk.bae@gmail.com
 */
data class ApplicationKotlinConfig(override val config: Config) :
    DatabaseConfigSupport,
    RedisConfigSupport,
    RedissonConfigSupport,
    MongoDBConfigSupport,
    HibernateConfigSupport,
    EmailConfigSupport