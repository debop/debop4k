package debop4k.data.spring

import org.springframework.jdbc.datasource.AbstractDataSource
import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.Connection
import javax.sql.DataSource

/**
 * @author sunghyouk.bae@gmail.com
 */
open class SpringTransactionalDataSource : AbstractDataSource() {

  private var inner: DataSource? = null

  override fun getConnection(): Connection? {
    val connection = DataSourceUtils.getConnection(inner)
    if (!DataSourceUtils.isConnectionTransactional(connection, inner)) {
      throw IllegalStateException("Connection is not transactional")
    }
    return connection
  }

  override fun getConnection(username: String?, password: String?): Connection? {
    throw UnsupportedOperationException("지원하지 않습니다.")
  }
}