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

import debop4k.core.cryptography.StringDigester;
import debop4k.data.orm.hibernate.usertypes.BaseUserType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hash Algorithm 을 이용하여, 문자열을 암호화하여 저장하도록 해주는 UserType 입니다.
 * 한번 암호화된 문자열을 null 이 아닌 다른 값으로 변경하면 안됩니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public abstract class HashStringUserType extends BaseUserType {

  abstract StringDigester digester();

  public String digest(String plainText) {
    return digester().digest(plainText);
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.STRING.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return String.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SessionImplementor session,
                            Object owner) throws HibernateException, SQLException {
    return StringType.INSTANCE.nullSafeGet(rs, names[0], session, owner);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SessionImplementor session) throws HibernateException, SQLException {
    String digestedText = (value == null) ? null : digest((String) value);
    StringType.INSTANCE.nullSafeSet(st, digestedText, index, session);
  }
}
