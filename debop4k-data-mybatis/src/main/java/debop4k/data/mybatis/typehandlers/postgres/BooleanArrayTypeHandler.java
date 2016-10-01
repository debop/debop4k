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

package debop4k.data.mybatis.typehandlers.postgres;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

/**
 * Boolean 배열을 PostgreSQL boolean 배열로 변환합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(Boolean[].class)
public class BooleanArrayTypeHandler extends BaseTypeHandler<Boolean[]> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Boolean[] parameter, JdbcType jdbcType) throws SQLException {
    Array array = ps.getConnection().createArrayOf("boolean", parameter);
    ps.setArray(i, array);
  }

  @Override
  public Boolean[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return asBooleanArray(rs.getArray(columnName));
  }

  @Override
  public Boolean[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return asBooleanArray(rs.getArray(columnIndex));
  }

  @Override
  public Boolean[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return asBooleanArray(cs.getArray(columnIndex));
  }

  private Boolean[] asBooleanArray(Array array) throws SQLException {
    if (array != null) {
      return (Boolean[]) array.getArray();
    }
    return null;
  }
}
