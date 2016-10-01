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
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.sql.*;

/**
 * Joda LocalTime 을 JDBC Date 수형으로 저장
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedTypes(LocalDate.class)
@MappedJdbcTypes(JdbcType.DATE)
public class JodaLocalDateToDateTypeHandler extends BaseTypeHandler<LocalDate> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
    ps.setDate(i, new Date(parameter.toDate().getTime()));
  }

  @Override
  public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return asLocalDate(rs.getDate(columnName));
  }

  @Override
  public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return asLocalDate(rs.getDate(columnIndex));
  }

  @Override
  public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return asLocalDate(cs.getDate(columnIndex));
  }

  private LocalDate asLocalDate(Date date) {
    if (date != null)
      return new LocalDate(date.getTime(), DateTimeZone.getDefault());
    return null;
  }
}
