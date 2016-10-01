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

package debop4k.data.mybatis.typehandlers;

import debop4k.core.cryptography.Cryptographyx;
import debop4k.core.cryptography.SymmetricEncryptor;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 일반 문자열을 암호화하여 저장하는 TypeHandler 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedTypes({String.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptedStringTypeHandler extends BaseTypeHandler<String> {

  private static final SymmetricEncryptor rc2 = Cryptographyx.getRC2();

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
    ps.setString(i, rc2.encryptString(parameter));
  }

  @Override
  public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return asPlainText(rs.getString(columnName));
  }

  @Override
  public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return asPlainText(rs.getString(columnIndex));
  }

  @Override
  public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return asPlainText(cs.getString(columnIndex));
  }

  private String asPlainText(String encrypted) {
    return rc2.decryptString(encrypted);
  }
}
