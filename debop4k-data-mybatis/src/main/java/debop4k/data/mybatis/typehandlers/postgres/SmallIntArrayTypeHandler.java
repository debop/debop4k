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
 * Integer 배열을 PostgreSQL small int 배열로 저장하도록 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(Integer[].class)
public class SmallIntArrayTypeHandler extends BaseTypeHandler<Integer[]> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {
    Array array = ps.getConnection().createArrayOf("smallint", parameter);
    ps.setArray(i, array);
  }

  @Override
  public Integer[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return asIntegerArray(rs.getArray(columnName));
  }

  @Override
  public Integer[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return asIntegerArray(rs.getArray(columnIndex));
  }

  @Override
  public Integer[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return asIntegerArray(cs.getArray(columnIndex));
  }

  private Integer[] asIntegerArray(Array array) throws SQLException {
    if (array != null) {
      return (Integer[]) array.getArray();
    }

    return null;
  }
}