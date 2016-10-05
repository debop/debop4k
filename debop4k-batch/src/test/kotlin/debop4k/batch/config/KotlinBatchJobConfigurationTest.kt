package debop4k.batch.config

import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.annotation.Resource
import javax.inject.Inject
import javax.sql.DataSource

/**
 * KotlinBatchJobConfigurationTest
 * @author sunghyouk.bae@gmail.com
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(BatchJobConfiguration::class))
open class KotlinBatchJobConfigurationTest {

  @Resource(name = "jobDataSource")
  val jobDataSource: DataSource = uninitialized()

  @Inject val jobRepository: JobRepository = uninitialized()
  @Inject val jobExplorer: JobExplorer = uninitialized()

  @Inject val jobLauncher: JobLauncher = uninitialized()

  @Test
  fun testConfiguration() {
    assertThat(jobDataSource).isNotNull()
    assertThat(jobRepository).isNotNull()
    assertThat(jobExplorer).isNotNull()

    assertThat(jobLauncher).isNotNull()
  }
}