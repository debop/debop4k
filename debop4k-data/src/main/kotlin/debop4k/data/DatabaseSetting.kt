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

package debop4k.data

import org.eclipse.collections.api.map.MutableMap
import org.eclipse.collections.impl.factory.Maps
import java.io.Serializable

/**
 * Database 접속을 위한 설정정보를 표현하는 ValueObject 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
data class DatabaseSetting(var host: String = "localhost",
                           var driverClassName: String = "",
                           var jdbcUrl: String = "",
                           var username: String? = null,
                           var password: String? = null,
                           var maxPoolSize: Int = DataSources.MAX_POOL_SIZE,
                           var minIdleSize: Int = DataSources.MIN_IDLE_SIZE,
                           var testQuery: String? = "SELECT 1") : Serializable {

  val props: MutableMap<String, String>? by lazy { Maps.mutable.of<String, String>() }
}

fun DatabaseSetting.isMySQL(): Boolean {
  return driverClassName == JdbcDrivers.DRIVER_CLASS_MYSQL
      || driverClassName == JdbcDrivers.DRIVER_CLASS_MARIADB
}

fun DatabaseSetting.isPostgreSQL(): Boolean {
  return driverClassName == JdbcDrivers.DRIVER_CLASS_POSTGRESQL
}
