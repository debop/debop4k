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
import org.joda.time.DateTime;

import java.sql.*;

/**
 * Joda DateTime 을 DB에 JDBC Timestamp 수형으로 저장하도록 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedTypes(DateTime.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class JodaDateTimeToTimestampTypeHandler extends BaseTypeHandler<DateTime> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, DateTime parameter, JdbcType jdbcType) throws SQLException {
    ps.setTimestamp(i, new Timestamp(parameter.getMillis()));
  }

  @Override
  public DateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    Timestamp ts = rs.getTimestamp(columnName);
    return asDateTime(ts);
  }

  @Override
  public DateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    Timestamp ts = rs.getTimestamp(columnIndex);
    return asDateTime(ts);
  }

  @Override
  public DateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    Timestamp ts = cs.getTimestamp(columnIndex);
    return asDateTime(ts);
  }

  private DateTime asDateTime(Timestamp ts) {
    if (ts != null)
      return new DateTime(ts.getTime());

    return null;
  }
}
