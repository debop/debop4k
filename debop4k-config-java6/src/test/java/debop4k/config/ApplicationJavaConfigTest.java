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

package debop4k.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ApplicationJavaConfigTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ApplicationJavaConfigTest {

  private List<String> steps = Arrays.asList("local", "devel", "test", "prod");

  @Test
  public void loadConfiguration() {
    for (String step : steps) {
      ApplicationJavaConfig appConfig =
          new ApplicationJavaConfig(ConfigLoader.load("config/" + step, "application"));

      assertThat(appConfig.getDatabase()).isNotNull();
      assertThat(appConfig.getDatabase().getDriverClass()).isNotEmpty();
      assertThat(appConfig.getDatabase().getJdbcUrl()).isNotEmpty();

      assertThat(appConfig.getRedis().getHost()).isEqualTo("127.0.0.1");
      assertThat(appConfig.getRedis().getPort()).isEqualTo(6379);
//      assertThat(appConfig.getRedis().getMaster().getPort()).isEqualTo(6379);
//      assertThat(appConfig.getRedis().getMaster().getDatabase()).isEqualTo(0);
//
//      assertThat(appConfig.getRedis().getSlaves().size()).isGreaterThan(0);
//      assertThat(appConfig.getRedis().getSlaves().iterator().next().getHost()).isEqualTo("127.0.0.1");

      assertThat(appConfig.getEmail().getEncoding()).isEqualTo("UTF-8");
      assertThat(appConfig.getEmail().getProperties().getProperty("mail.transport.protocol")).isEqualTo("smtp");

      assertThat(appConfig.getMongodb().getDatabase()).isNotNull();
      assertThat(appConfig.getHibernate().getHbm2ddl()).isNotNull();

      String redissonConfigPath = appConfig.getRedisson().getConfigPath();
      log.debug("redissonConfigPath={}", redissonConfigPath);
      assertThat(redissonConfigPath).isNotEmpty();
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(redissonConfigPath);
      assertThat(inputStream).isNotNull();
    }
  }
}
