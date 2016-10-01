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

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * UUID 수형을 PostgreSQL UUID 수형으로 변환합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes(UUID.class)
public class UUIDTypeHandler extends BaseTypeHandler<UUID> {
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
    ps.setObject(i, parameter, jdbcType.TYPE_CODE);
  }

  @Override
  public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return (UUID) rs.getObject(columnName);
  }

  @Override
  public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return (UUID) rs.getObject(columnIndex);
  }

  @Override
  public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return (UUID) cs.getObject(columnIndex);
  }
}
