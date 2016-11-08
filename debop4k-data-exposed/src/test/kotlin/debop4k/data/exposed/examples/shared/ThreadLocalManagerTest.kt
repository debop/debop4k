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
 */

package debop4k.data.exposed.examples.shared

import debop4k.core.uninitialized
import debop4k.data.exposed.examples.DatabaseTestBase
import debop4k.data.exposed.examples.TestDB
import debop4k.data.exposed.examples.TestDB.MYSQL
import debop4k.data.exposed.examples.shared.DMLData.Cities
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.TransactionManager.Companion
import org.junit.Test
import kotlin.concurrent.thread

class ThreadLocalManagerTest : DatabaseTestBase() {

  @Test
  fun testReconnection() {
    var secondThreadTm: TransactionManager? = uninitialized()
    var isMySQL = false

    withDb(MYSQL) {
      isMySQL = true
      SchemaUtils.create(Cities)
      val firstThreadTm = TransactionManager.currentThreadManager.get()

      thread {
        withDb(TestDB.MYSQL) {
          DMLData.Cities.selectAll().toList()
          secondThreadTm = Companion.currentThreadManager.get()
          assertThat(secondThreadTm).isNotEqualTo(firstThreadTm)
        }
      }.join()
      assertThat(TransactionManager.currentThreadManager.get()).isEqualTo(firstThreadTm)
    }
    if (isMySQL) {
      assertThat(TransactionManager.currentThreadManager.get()).isEqualTo(secondThreadTm)
    }
  }
}