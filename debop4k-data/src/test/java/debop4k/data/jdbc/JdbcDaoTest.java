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

package debop4k.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {JdbcDaoConfiguration.class})
public class JdbcDaoTest {

  @Inject private JdbcDao dao;
  @Inject private JdbcTemplate template;

  private static final String SELECT_ACTORS = "SELECT * FROM Actors;";

  private static final RowMapper<Actor> ACTOR_ROW_MAPPER = new RowMapper<Actor>() {
    @Override
    public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
      int id = rs.getInt("id");
      String firstname = rs.getString("firstname");
      String lastname = rs.getString("lastname");

      return Actor.of(id, firstname, lastname);
    }
  };

  @Test
  public void assertConfiguration() {
    assertThat(dao).isNotNull();
  }

  @Test
  public void loadActorsByRowMapper() {
    RowMapper<Actor> mapper = new RowMapper<Actor>() {
      @Override
      public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String firstname = rs.getString("firstname");
        String lastname = rs.getString("lastname");

        return Actor.of(id, firstname, lastname);
      }
    };
    List<Actor> actors = template.query(SELECT_ACTORS, mapper);
    assertThat(actors).isNotEmpty().hasSize(5);
  }

  @Test
  public void loadActorsByRowMapperLambdaExpr() {
    List<Actor> actors = template.query(SELECT_ACTORS, ACTOR_ROW_MAPPER);
    assertThat(actors).isNotEmpty().hasSize(5);
  }

  @Test
  public void loadActorsByJdbcDao() {
    List<Actor> actors = dao.query(SELECT_ACTORS, ACTOR_ROW_MAPPER);
    assertThat(actors).isNotEmpty().hasSize(5);
  }

  @Test
  public void getCountByJdbcDao() {
    int count = template.queryForObject("SELECT count(*) FROM Actors", Integer.class);
    assertThat(count).isGreaterThan(0);
  }

}
