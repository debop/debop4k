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

import debop4k.mongodb.DATABASE_NAME
import debop4k.mongodb.bulk.BulkEntityRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

/**
 * Created by debop
 */
@Configuration
@EnableMongoRepositories(basePackageClasses = arrayOf(BulkEntityRepository::class))
open class KotlinMongoConfiguration : AbstractMongoConfig() {

  open protected override fun getDatabaseName(): String {
    return DATABASE_NAME
  }

  open protected override fun getMappingBasePackage(): String {
    return "debop4k.mongodb"
  }
}
