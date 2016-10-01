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

package debop4k.data.spring;

import debop4k.data.jdbc.Actor;
import debop4k.data.spring.boot.autoconfigure.HikariDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DataSpringBootAutoConfigurationKotlinTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataSpringBootApplicationKotlin.class)
public class DataSpringBootAutoConfigurationTest {

  @Inject HikariDataSourceProperties properties;
  @Inject DataSource dataSource;

  @Inject NamedParameterJdbcTemplate jdbcTemplate;

  @Test
  public void testAutoConfiguration() {

    assertThat(properties).isNotNull();
    assertThat(dataSource).isNotNull();
    assertThat(jdbcTemplate).isNotNull();

  }

  @Test
  public void testDataSourceInjection() throws Exception {
    Connection conn = null;
    try {
      conn = dataSource.getConnection();
    } finally {
      if (conn != null)
        conn.close();
    }
  }

  @Test
  public void testFlywayMigration() {
    assertThat(jdbcTemplate).isNotNull();

    List<Actor> actors = jdbcTemplate.query("SELECT * FROM Actors", new RowMapper<Actor>() {
      @Override
      public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Actor.of(rs.getInt("id"),
                        rs.getString("firstname"),
                        rs.getString("lastname"));
      }
    });
    assertThat(actors).hasSize(5);
    for (Actor actor : actors) {
      assertThat(actor).isNotNull();
      assertThat(actor.getId()).isGreaterThan(0);
      log.debug("{}", actor);
    }
  }
}
