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

package debop4k.data;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.zaxxer.hikari.HikariConfig;
import debop4k.config.database.DatabaseConfigElement;
import debop4k.config.database.DatabaseSetting;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static debop4k.data.JdbcDrivers.DRIVER_CLASS_POSTGRESQL;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class DataSourcesTest {

  @SneakyThrows({Exception.class})
  private static void connectionTest(DataSource ds) {
    for (int i = 0; i < 50; i++) {
      Connection conn = ds.getConnection();
      try {
        assertThat(conn).isNotNull();
        PreparedStatement ps = conn.prepareStatement("SELECT 1;");
        try {
          assertThat(ps).isNotNull();
          assertThat(ps.execute()).isTrue();
        } finally {
          ps.close();
        }
      } catch (SQLException e) {
        throw new RuntimeException("connection error.", e);
      } finally {
        conn.close();
      }
    }
  }

  @Test
  public void embeddedH2DataSource() {
    DataSource ds = DataSources.ofEmbeddedH2();
    assertThat(ds).isNotNull();
    connectionTest(ds);
  }

  // NOTE: HSQL는 Multi Thread 에서 In-Memory DB를 지원하지 않습니다.
  @Test
  public void embeddedHSqlDataSource() throws Exception {
    DataSource ds = DataSources.ofEmbeddedHSql();
    assertThat(ds).isNotNull();

    Connection conn = ds.getConnection();
    try {
      assertThat(conn).isNotNull();
    } finally {
      conn.close();
    }
  }

  @Test
  public void createDirectly() {
    DatabaseSetting setting = DatabaseSetting.builder()
                                             .driverClass(JdbcDrivers.DRIVER_CLASS_H2)
                                             .jdbcUrl("jdbc:h2:mem:test")
                                             .username("sa")
                                             .password("")
                                             .maxPoolSize(DataSources.MAX_POOL_SIZE)
                                             .minIdleSize(DataSources.MIN_IDLE_SIZE)
                                             .build();
    DataSource ds = DataSources.of(setting);
    assertThat(ds).isNotNull();
    connectionTest(ds);
  }

  @Test
  public void newDataSourceWithHikariConfigFromProperties() throws Exception {
    Properties props = new Properties();
    props.load(getClass().getClassLoader().getResourceAsStream("hikari-h2.properties"));

    HikariConfig config = new HikariConfig(props);
    DataSource ds = DataSources.withConfig(config);
    assertThat(ds).isNotNull();
    connectionTest(ds);
  }

  @Test
  public void newDataSourceForPostgresFromProperties() throws Exception {
    Properties props = new Properties();
    props.load(getClass().getClassLoader().getResourceAsStream("hikari-postgres.properties"));

    HikariConfig config = new HikariConfig(props);
    DataSource ds = DataSources.withConfig(config);
    assertThat(ds).isNotNull();
    connectionTest(ds);
  }

  @Test
  public void createFromConfig() {
    Config config = ConfigFactory.load("application");
    DatabaseConfigElement dbElement = new DatabaseConfigElement(config.getConfig("database"));

    DataSource ds = DataSources.of(dbElement.getDatabaseSetting());
    assertThat(ds).isNotNull();
    connectionTest(ds);
  }

  @Test
  public void newDataSourceForPostgreSQL() throws Exception {
    DatabaseSetting setting =
        DatabaseSetting.builder()
                       .driverClass(DRIVER_CLASS_POSTGRESQL)
                       .jdbcUrl("jdbc:postgresql://127.0.0.1/hibernate")
                       .username("root")
                       .password("root")
                       .build();

    DataSource ds = DataSources.of(setting);
    assertThat(ds).isNotNull();

    Connection conn = ds.getConnection();
    try {
      assertThat(conn).isNotNull();
    } finally {
      conn.close();
    }
  }
}
