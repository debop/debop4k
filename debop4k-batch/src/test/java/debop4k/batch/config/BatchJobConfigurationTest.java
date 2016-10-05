package debop4k.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BatchJobConfigurationTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BatchJobConfiguration.class})
public class BatchJobConfigurationTest {

  //  @Resource(name="jobDataSource")
  @Autowired @Qualifier("jobDataSource") private DataSource jobDataSource;
  @Autowired JobRepository jobRepository;
  @Autowired JobExplorer jobExplorer;
  @Autowired JobLauncher jobLauncher;

  @Test
  public void testConfiguration() {
    assertThat(jobDataSource).isNotNull();
    assertThat(jobRepository).isNotNull();
    assertThat(jobExplorer).isNotNull();
    assertThat(jobLauncher).isNotNull();
  }
}
