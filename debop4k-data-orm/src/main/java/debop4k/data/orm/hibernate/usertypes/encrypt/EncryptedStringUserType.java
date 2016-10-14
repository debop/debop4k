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

package debop4k.data.orm.hibernate.usertypes.encrypt;

import debop4k.core.cryptography.Encryptor;
import debop4k.data.orm.hibernate.usertypes.BaseUserType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 문자열을 암호화하여 저장하고, 로딩 시에는 복원을 해주는 UserType 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 27.
 */
public abstract class EncryptedStringUserType extends BaseUserType {

  abstract Encryptor encrytor();

  public String encrypt(String plainText) {
    return encrytor().encryptString(plainText);
  }

  public String decrypt(String encryptedText) {
    return encrytor().decryptString(encryptedText);
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{StringType.INSTANCE.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return String.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SharedSessionContractImplementor session,
                            Object owner) throws HibernateException, SQLException {
    String encryptedText = (String) StringType.INSTANCE.nullSafeGet(rs, names[0], session, owner);
    return decrypt(encryptedText);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StringType.INSTANCE.nullSafeSet(st, null, index, session);
    } else {
      StringType.INSTANCE.nullSafeSet(st, encrypt((String) value), index, session);
    }
  }
}
