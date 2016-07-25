/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.spring.jdbc

import debop4k.core.uninitialized
import debop4k.spring.jdbc.core.extract
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.test.fail

abstract class AbstractJdbcTest {

  protected val log: Logger = LoggerFactory.getLogger(javaClass)


  val select = "SELECT * FROM test_bean"

  val selectId = "select id from test_bean "
  val selectIdByDescription = "$selectId where description = ?"
  val python = "python"
  val description = "description"

  val mapperFunction = { rs: ResultSet, i: Int ->
    rs.extract {
      TestBean(int["id"]!!,
               string["description"],
               date["create_date"])
    }
  }

  val action = { ps: PreparedStatement ->
    val rs = ps.executeQuery()
    rsFunction(rs)
  }

  val rsFunction = { rs: ResultSet ->
    rs.extract {
      next()
      int["id"]
    }
  }

  @Autowired private var template: JdbcTemplate = uninitialized()

  protected fun count(): Int {
    var cnt = 0
    template.query("select count(*) from test_bean") { rs ->
      rs.extract {
        cnt = int[1]!!
      }
    }
    return cnt
  }

  protected fun validateEmptyResultSet(body: () -> Unit) {
    try {
      body()
      fail("Function $body don't throw a exception")
    } catch(e: EmptyResultDataAccessException) {
      // expected
    }
  }
}