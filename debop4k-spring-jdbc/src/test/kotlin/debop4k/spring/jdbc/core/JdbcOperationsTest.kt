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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.sql.*
import javax.inject.Inject
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@ContextConfiguration(classes = arrayOf(DataConfiguration::class))
@Transactional
open class JdbcOperationsTest : AbstractJdbcTest() {

  @Inject val dataSource: DataSource = uninitialized()
  @Inject val template: JdbcTemplate = uninitialized()

  private val select1: String = "$select where id = 1"
  private val selectIdPython = "$selectId where description = 'python'"
  private val selectGreaterThan = "$select where id > ?"
  private val selectById = "$select where id = ?"
  private val insert = "insert into test_bean(description) values(?)"

  private val statementCreator: (Connection) -> PreparedStatement = { conn ->
    val st = conn.prepareStatement(selectIdByDescription)
    st.arguments {
      string[1] = python
    }
    st
  }


  @Test fun testInitialized() {
    assertThat(dataSource).isNotNull()
    assertThat(template).isNotNull()
    log.debug("Database setup is ok!!!")
  }

  @Test fun selectIdByDescription() {
    template.execute { connection: Connection ->
      val ps = connection.prepareStatement(selectIdByDescription)
      ps.arguments {
        string[1] = "python"
      }
      val resultSet = ps.executeQuery()
      resultSet.extract {
        assertThat(next()).isTrue()
        assertThat(int["id"]).isEqualTo(1)
        close()
      }
    }
  }

  @Test fun selectOne() {
    template.execute { statement: Statement ->
      val rs = statement.executeQuery(select1)
      rs.extract {
        assertThat(next()).isTrue()
        assertThat(string["description"]).isEqualTo("python")
        close()
      }
    }
  }

  @Test fun executeWithAction() {
    assertThat(template.execute(statementCreator, action)).isEqualTo(1)
    assertThat(template.execute(selectIdPython, action)).isEqualTo(1)
  }

  @Test fun queryByResultSetExtractor() {
    assertThat(template.query<String>(select1, { rs: ResultSet ->
      rs.extract {
        next()
        string["description"]
      }
    })).isEqualTo("python")
  }

  @Test fun queryByRowCallbackHandler() {
    // next() 를 사용하면 안된다.
    template.query(select1, { rs: ResultSet ->
      rs.extract {
        assertThat(string["description"]).isEqualTo("python")
      }
    })
  }

  @Test fun queryByMapperFunction() {
    assertThat(template.query(select, mapperFunction)).hasSize(5)
  }

  @Test fun queryByStatementCreator() {
    assertThat(template.query(statementCreator, rsFunction)).isEqualTo(1)
  }

  @Test fun queryByStatementWithArguments() {
    val id = template.query(selectIdByDescription,
                            {
                              it.arguments {
                                string[1] = "python"
                              }
                            },
                            rsFunction)
    assertThat(id).isEqualTo(1)
  }

  @Test fun queryByVairousMethods() {
    assertThat(template.query(selectIdByDescription,
                              arrayOf("python"),
                              intArrayOf(Types.VARCHAR),
                              rsFunction))
        .isEqualTo(1)

    assertThat(template.query(selectIdByDescription,
                              arrayOf("python"),
                              rsFunction))
        .isEqualTo(1)

    assertThat(template.queryEx(selectIdByDescription, "python") { rs -> rsFunction(rs) }).isEqualTo(1)

    assertThat(template.query({ conn: Connection ->
                                conn.prepareStatement(select)
                              },
                              mapperFunction)).hasSize(5)

    assertThat(template.query(selectGreaterThan,
                              { stmt: PreparedStatement ->
                                stmt.arguments {
                                  int[1] = 1
                                }
                              },
                              mapperFunction)).hasSize(4)

    assertThat(template.query(selectGreaterThan,
                              arrayOf(1),
                              intArrayOf(Types.INTEGER),
                              mapperFunction))
        .hasSize(4)

    assertThat(template.query(selectGreaterThan,
                              arrayOf(1),
                              mapperFunction))
        .hasSize(4)

    assertThat(template.queryEx(selectGreaterThan, 1) { rs, rowNum ->
      mapperFunction(rs, rowNum)
    }).hasSize(4)
  }

  @Test fun testQueryForObject() {
    assertThat(template.queryForObject(select1, mapperFunction).description).isEqualTo("python")
    assertThat(template.queryForObject(selectById,
                                       arrayOf(1),
                                       intArrayOf(Types.INTEGER),
                                       mapperFunction)
                   .description)
        .isEqualTo("python")
    assertThat(template.queryForObject(selectById,
                                       arrayOf(1),
                                       mapperFunction)
                   .description)
        .isEqualTo("python")

    assertThat(template.queryExForObject(selectById, 1) { rs, rowNum -> mapperFunction(rs, rowNum) }?.description)
        .isEqualTo("python")
  }

  @Test fun testUpdate() {
    assertThat(template.update { conn: Connection ->
      val ps = conn.prepareStatement("update test_bean set create_date = ?")
      ps.arguments {
        date[1] = Date(System.currentTimeMillis())
      }
      ps
    }).isEqualTo(5)

    assertThat(template.update({ conn: Connection ->
                                 val ps = conn.prepareStatement(insert)
                                 ps.arguments {
                                   string[1] = "Haxe"
                                 }
                                 ps
                               },
                               GeneratedKeyHolder())).isEqualTo(1)

    assertThat(template.update("update test_bean set create_date = ?") { ps ->
      ps.arguments {
        date[1] = Date(System.currentTimeMillis())
      }
    }).isEqualTo(6)
  }

  @Test fun testBatchUpdate() {
    template.batchUpdate(insert, listOf("clojure", "haxe", "objective-c", "erlang"), 4) { ps, t ->
      ps.arguments {
        string[1] = t
      }
    }
    assertThat(count()).isEqualTo(9)
  }
}