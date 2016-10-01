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

package debop4k.data.orm.springdata.examples.custom;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * UserRepositoryImplJdbc
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 9.
 */
@Slf4j
@Profile("jdbc")
@Component("userRepositoryImpl")
public class UserRepositoryImplJdbc extends JdbcDaoSupport implements UserRepositoryCustom {

  private static final String COMPLICATED_SQL = "SELECT * FROM User";

  @Inject
  public UserRepositoryImplJdbc(DataSource dataSource) {
    setDataSource(dataSource);
  }

  @Override
  public List<User> customBatchOperation() {
    log.debug("JDBC 로 일반 SQL 문을 실행합니다.");

//    return getJdbcTemplate().query(COMPLICATED_SQL, rs -> {
//      List<User> users = FastList.newList();
//      while (rs.next()) {
//        User user = new User(rs.getInt("id"));
//        user.setUsername(rs.getString("username"));
//        user.setFirstname(rs.getString("firstname"));
//        user.setLastname(rs.getString("lastname"));
//        users.add(user);
//      }
//      return users;
//    });

    RowMapper<User> rowMapper = new RowMapper<User>() {
      @Override
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        return user;
      }
    };
    return getJdbcTemplate().query(COMPLICATED_SQL, rowMapper);
  }
}
