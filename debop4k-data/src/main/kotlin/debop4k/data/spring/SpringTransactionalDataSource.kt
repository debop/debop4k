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