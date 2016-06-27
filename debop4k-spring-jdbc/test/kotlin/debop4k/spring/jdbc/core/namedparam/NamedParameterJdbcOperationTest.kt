package debop4k.spring.jdbc.core.namedparam

import debop4k.core.utils.uninitialized
import debop4k.spring.jdbc.AbstractJdbcTest
import debop4k.spring.jdbc.TestBean
import debop4k.spring.jdbc.config.DataConfiguration
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional
import javax.inject.Inject

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(classes = arrayOf(DataConfiguration::class))
@Transactional
open class NamedParameterJdbcOperationTest : AbstractJdbcTest() {

  @Inject var template: NamedParameterJdbcTemplate = uninitialized()

  private val id = "id"
  private val selectByIdGreaterThan = "$select where id > :id"
  private val descriptionToPythonMap = mapOf("description" to "python")
  private val selectIdByNamedDescription = "$selectId where description = :description"
  private val selectByNamedId = "$select where id = :id"
  private val parameterSource = TestBean(description = "python").toSqlParameterSource()

  @Test fun configuration() {
    assertThat(template).isNotNull()
  }

  @Test fun testExecute() {
    assertThat(template.execute(selectIdByNamedDescription, parameterSource, action)).isEqualTo(1)
    assertThat(template.execute(selectIdByNamedDescription, descriptionToPythonMap, action)).isEqualTo(1)
  }

  @Test fun testQuery() {
    assertThat(template.query(selectIdByNamedDescription, parameterSource, rsFunction)).isEqualTo(1)
    assertThat(template.query(selectIdByNamedDescription, descriptionToPythonMap, rsFunction)).isEqualTo(1)

    assertThat(template.query(selectByIdGreaterThan,
                              TestBean(id = 1).toSqlParameterSource(),
                              mapperFunction)).hasSize(4)

    assertThat(template.query(selectByIdGreaterThan, mapOf(id to 1), mapperFunction)).hasSize(4)
  }

  @Test fun testQueryForObject() {
    assertThat(template.queryForObject(selectByNamedId,
                                       TestBean(id = 1).toSqlParameterSource(),
                                       mapperFunction).description)
        .isEqualTo("python")

    validateEmptyResultSet {
      assertThat(template.queryForObject(selectByNamedId,
                                         TestBean(id = -1).toSqlParameterSource(),
                                         mapperFunction))
    }
  }
}