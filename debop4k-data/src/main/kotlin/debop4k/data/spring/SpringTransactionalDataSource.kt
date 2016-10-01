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

package debop4k.data.spring

import org.springframework.jdbc.datasource.AbstractDataSource
import org.springframework.jdbc.datasource.DataSourceUtils
import java.sql.Connection
import javax.sql.DataSource

/**
 * Spring framework 의 Transactional annotation 을 이용하여 Transaction 하에서 DB 작업이 가능하도록 해 줍니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
class SpringTransactionalDataSource : AbstractDataSource() {

  public var inner: DataSource? = null

  override fun getConnection(): Connection {
    val conn = DataSourceUtils.getConnection(inner)
    if (!DataSourceUtils.isConnectionTransactional(conn, inner)) {
      throw IllegalStateException("Connection is not transactional")
    }
    return conn
  }

  override fun getConnection(username: String?, password: String?): Connection {
    throw UnsupportedOperationException("Not supported operation")
  }
}