package debop4k.data.spring

import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import javax.inject.Inject
import javax.sql.DataSource

@ActiveProfiles(profiles = arrayOf("dev"))
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(DataSpringBootApplication::class))
class DataSpringBootAutoConfigurationTest {

  @Inject val dataSource: DataSource = uninitialized()

  @Test fun testConfiguration() {
    assertThat(dataSource).isNotNull()
  }
}