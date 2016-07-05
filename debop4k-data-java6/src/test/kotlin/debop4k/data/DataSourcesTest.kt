package debop4k.data

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.sql.SQLException
import javax.sql.DataSource

/**
 * @author sunghyouk.bae@gmail.com
 */
class DataSourcesTest : AbstractDataTest() {

  private fun connectionTest(ds: DataSource) {
    for (i in 0 until 50) {
      ds.connection.let { conn ->
        try {
          assertThat(conn).isNotNull()
          val ps = conn.prepareStatement("SELECT 1")
          try {
            assertThat(ps).isNotNull()
            assertThat(ps.execute()).isTrue()
          } finally {
            ps.close()
          }
        } catch(e: SQLException) {
          throw RuntimeException("connection error", e)
        } finally {
          conn.close()
        }
      }
    }
  }

  @Test fun embeddedH2DataSource() {
    val ds = DataSources.ofEmbeddedH2()
    assertThat(ds).isNotNull()
    connectionTest(ds)
  }

  @Test fun embeddedHSqlDataSource() {
    val ds = DataSources.ofEmbeddedHsql()
    assertThat(ds).isNotNull()

    val conn = ds.connection
    try {
      assertThat(conn).isNotNull()
    } finally {
      conn.close()
    }
  }

  @Test fun createDirectly() {
    val setting = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_H2,
                                  jdbcUrl = "jdbc:h2:mem:direct",
                                  username = "sa",
                                  maxPoolSize = 10,
                                  minIdleSize = 4)
    val ds = DataSources.of(setting)
    assertThat(ds).isNotNull()
    connectionTest(ds)
  }

  @Test fun createForPostgres() {
    val setting = DatabaseSetting(driverClassName = JdbcDrivers.DRIVER_CLASS_POSTGRESQL,
                                  jdbcUrl = "jdbc:postgresql://localhost/hibernate",
                                  username = "root",
                                  password = "root")
    val ds = DataSources.of(setting)
    assertThat(ds).isNotNull()

    val conn = ds.connection
    try {
      assertThat(conn).isNotNull()
    } finally {
      conn.close()
    }
  }
}