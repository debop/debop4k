package debop4k.data.exposed.examples.mysql

import debop4k.data.exposed.examples.DatabaseTestBase
import debop4k.data.exposed.examples.TestDB
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.junit.Test

class MysqlTests : DatabaseTestBase() {

  @Test
  fun testEmbeddedConnection() {
    withDb(TestDB.MYSQL) {
      val result = TransactionManager.current().exec("SELECT VERSION();") { it.next(); it.getString(1) }
      assertThat(result).isNotEmpty()
      println("MySQL Version = $result")
    }
  }
}