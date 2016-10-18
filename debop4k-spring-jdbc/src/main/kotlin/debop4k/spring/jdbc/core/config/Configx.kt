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

@file:JvmName("Configx")

package debop4k.spring.jdbc.core.config

import org.springframework.jdbc.datasource.embedded.*
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2
import javax.sql.DataSource

/**
 * Embeded Database 에 대한 DataSource 를 빌드합니다.
 */
fun EmbeddedDatabaseType.buildDataSource(body: EmbeddedDatabaseTag.() -> Unit): DataSource {
  val tag = EmbeddedDatabaseTag().apply {
    body()
  }
  // val scripts = tag.scripts

  val factory = EmbeddedDatabaseFactory().apply {
    setDatabaseType(this@buildDataSource)
  }

  return factory.database
}

fun embeddedDatabase(type: EmbeddedDatabaseType = H2,
                     body: EmbeddedDatabaseTag.() -> Unit): DataSource {
  val tag = EmbeddedDatabaseTag().apply {
    body()
  }

  val scripts = tag.scripts.map { s -> s.location }

  return EmbeddedDatabaseBuilder()
      .setType(type)
      .generateUniqueName(true)
      .generateUniqueName(true)
      .addScripts(*scripts.toTypedArray<String>())
      .build()
}