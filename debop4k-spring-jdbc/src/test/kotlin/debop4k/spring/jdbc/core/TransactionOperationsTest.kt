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

package debop4k.spring.jdbc.core

import debop4k.core.uninitialized
import debop4k.spring.jdbc.AbstractJdbcTest
import debop4k.spring.jdbc.config.DataConfiguration
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import javax.inject.Inject

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(DataConfiguration::class))
open class TransactionOperationsTest : AbstractJdbcTest() {

  @Inject val tx: TransactionTemplate = uninitialized()
  @Inject val jdbc: JdbcTemplate = uninitialized()

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  open fun textExecuteInTransaction() {
    tx.execute { status: TransactionStatus ->
      jdbc.update("DELETE FROM test_bean")
      status.setRollbackOnly()
    }
    Assertions.assertThat(count()).isGreaterThan(0)
  }
}