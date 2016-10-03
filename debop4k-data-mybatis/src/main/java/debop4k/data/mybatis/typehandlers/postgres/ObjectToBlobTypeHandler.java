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

import debop4k.core.io.IOStreamx;
import debop4k.core.io.serializers.Serializer;
import debop4k.core.io.serializers.Serializers;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;

/**
 * 객체를 DB에 바이트 배열로 저장하도록 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedJdbcTypes(JdbcType.BLOB)
@MappedTypes(Object.class)
public class ObjectToBlobTypeHandler extends BaseTypeHandler<Object> {

  Serializer serializer = Serializers.SNAPPY_FST_JAVA6;

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
    byte[] bytes = serializer.serialize(parameter);
    Blob blob = ps.getConnection().createBlob();
    blob.setBytes(0, bytes);
    ps.setBlob(i, blob);
  }

  @Override
  public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return fromByteArray(rs.getBlob(columnName));
  }

  @Override
  public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return fromByteArray(rs.getBlob(columnIndex));
  }

  @Override
  public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return fromByteArray(cs.getBlob(columnIndex));
  }

  private Object fromByteArray(Blob blob) throws SQLException {
    if (blob == null)
      return null;

    byte[] bytes = IOStreamx.toByteArray(blob.getBinaryStream());
    return serializer.deserialize(bytes);
  }
}
