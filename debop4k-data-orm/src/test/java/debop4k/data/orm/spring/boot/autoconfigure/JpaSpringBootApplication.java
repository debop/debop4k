package debop4k.data.orm.spring.boot.autoconfigure;

import debop4k.redisson.spring.boot.autoconfigure.RedissonAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootApplication(
    scanBasePackageClasses = {JpaAutoConfiguration.class},
    exclude = {FlywayAutoConfiguration.class}
)
@Import({RedissonAutoConfiguration.class})
public class JpaSpringBootApplication {

  public static void main(String[] args) {
    SpringApplication.run(JpaSpringBootApplication.class, args);
  }
}
