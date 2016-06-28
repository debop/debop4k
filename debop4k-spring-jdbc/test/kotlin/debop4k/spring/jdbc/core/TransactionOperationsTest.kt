package debop4k.spring.jdbc.core

import debop4k.spring.jdbc.AbstractJdbcTest
import debop4k.spring.jdbc.config.DataConfiguration
import org.assertj.core.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(DataConfiguration::class))
open class TransactionOperationsTest : AbstractJdbcTest() {

  @Inject lateinit var tx: TransactionTemplate
  @Inject lateinit var jdbc: JdbcTemplate

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  open fun textExecuteInTransaction() {
    tx.execute { status: TransactionStatus ->
      jdbc.update("DELETE FROM test_bean")
      status.setRollbackOnly()
    }
    Assertions.assertThat(count()).isEqualTo(5)
  }
}