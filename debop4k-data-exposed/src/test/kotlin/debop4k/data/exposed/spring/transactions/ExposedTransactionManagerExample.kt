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

package debop4k.data.exposed.spring.transactions

import debop4k.core.uninitialized
import debop4k.data.exposed.AbstractExposedTest
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.jetbrains.exposed.sql.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.annotation.Repeat
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.inject.Inject


@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(ExposedTransactionManagerConfiguration::class))
@Transactional
open class ExposedTransactionManagerExample : AbstractExposedTest() {

  @Inject val ctx: ApplicationContext = uninitialized()

  object t1 : Table() {
    val c1 = varchar("c1", Int.MIN_VALUE.toString().length)
  }

  @Test
  @Repeat(5)
  fun testConnection() {
    val pm = ctx.getBean(PlatformTransactionManager::class.java)
    if (pm !is SpringTransactionManager)
      error("Wrong txManager instance: ${pm.javaClass}")

    SchemaUtils.create(t1)

    t1.insert {
      it[t1.c1] = "112"
    }

    assertThat(t1.selectAll().count()).isEqualTo(1)
  }

  @Test
  @Repeat(5)
  fun testConnection2() {
    val rnd = Random().nextInt().toString()
    t1.insert {
      it[t1.c1] = rnd
    }
    assertThat(t1.selectAll().single()[t1.c1]).isEqualTo(rnd)
  }


}