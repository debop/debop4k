package debop4k.spring.jdbc

import debop4k.core.uninitialized
import debop4k.spring.jdbc.core.extract
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlin.test.fail

abstract class AbstractJdbcTest {

  protected val log = LoggerFactory.getLogger(javaClass)


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