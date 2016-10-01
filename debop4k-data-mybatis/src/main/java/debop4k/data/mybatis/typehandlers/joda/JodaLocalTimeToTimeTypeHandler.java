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

package debop4k.data.mybatis.typehandlers.joda;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.LocalTime;

import java.sql.*;

/**
 * Joda LocalTime 수형을 JDBC Time 수형으로 저장하도록 해주는 Handler 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedTypes(LocalTime.class)
@MappedJdbcTypes(JdbcType.TIME)
public class JodaLocalTimeToTimeTypeHandler extends BaseTypeHandler<LocalTime> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType jdbcType) throws SQLException {
    ps.setTime(i, new Time(parameter.getMillisOfDay()));
  }

  @Override
  public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return asLocalTime(rs.getTime(columnName));
  }

  @Override
  public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return asLocalTime(rs.getTime(columnIndex));
  }

  @Override
  public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return asLocalTime(cs.getTime(columnIndex));
  }

  private LocalTime asLocalTime(Time time) {
    if (time != null) {
      return new LocalTime(time.getTime());
    }
    return null;
  }
}
