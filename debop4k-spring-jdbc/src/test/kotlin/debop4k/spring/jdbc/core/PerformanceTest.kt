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

package debop4k.spring.jdbc.core

import debop4k.core.uninitialized
import debop4k.spring.jdbc.AbstractJdbcTest
import debop4k.spring.jdbc.TestBean
import debop4k.spring.jdbc.config.PerfDataConfiguration
import debop4k.spring.util.stopWatch
import debop4k.spring.util.task
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import java.sql.ResultSet


@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(PerfDataConfiguration::class))
@Transactional
open class PerformanceTest : AbstractJdbcTest() {

  @Autowired val template: JdbcTemplate = uninitialized()

//  data class User(val name: String, val age: Int)

  @Test fun configuration() {
    assertThat(template).isNotNull()
  }

  @Test fun performance() {
    val watch = stopWatch {

      for (i in 1 .. 10) {
        task("With DSL as parameter: $i run") {
          val result = template.query(select, mapperFunction)
          assertThat(result).hasSize(1000)
        }

        task("With DSL in line: $i run") {
          val result = template.query(select) { rs, i ->
            rs.extract {
              TestBean(int["id"], string["description"], date["create_date"])
            }
          }
          assertThat(result).hasSize(1000)
        }

        task("Without DSL: $i run") {
          val result = template.query(select) { rs, i ->
            TestBean(rs.getInt("id"), rs.getString("description")!!, rs.getDate("create_date")!!)
          }
          assertThat(result).hasSize(1000)
        }

        task("Without SAM: $i run") {
          val result = template.query(select, object : RowMapper<TestBean> {
            override fun mapRow(rs: ResultSet?, rowNum: Int): TestBean? {
              return TestBean(rs!!.getInt("id"),
                              rs.getString("description")!!,
                              rs.getDate("create_date")!!)
            }

          })
          assertThat(result).hasSize(1000)
        }
      }
    }

    println("Performance result = ${watch.prettyPrint()}")
  }
}