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

package debop4k.data.orm.hibernate.usertypes.jodatime;

import debop4k.data.orm.hibernate.usertypes.BaseUserType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Joda-Time 라이브러리의 {@link DateTime} 수형을 표현하는 UserType 입니다.
 * 저장 시에는 Timestamp 값이 저장되고, 로드 시에는 {@link DateTime}으로 변환됩니다.
 * <p>
 * NOTE: MySql 의 DateTime은 milliseconds 를 지원하지 않습니다.
 * <p>
 * <pre><code>
 *  `@org.hibernate.annotation.Type`(`type`="debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeUserType")
 *   DateTime registDate;
 * </code></pre>
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 26.
 */
public class DateTimeUserType extends BaseUserType {

  private static DateTime asDateTime(Object value) {
    if (value == null)
      return (DateTime) null;

    if (value instanceof DateTime) return (DateTime) value;
    if (value instanceof Date) return new DateTime((Date) value);
    if (value instanceof Long) return new DateTime((Long) value);

    return null;
  }

  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.TIMESTAMP.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return DateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SharedSessionContractImplementor session,
                            Object owner) throws HibernateException, SQLException {
    Object value = StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names[0], session, owner);
    return asDateTime(value);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws HibernateException, SQLException {
    Timestamp timestamp = (value instanceof DateTime)
                          ? new Timestamp(((DateTime) value).getMillis())
                          : null;
    StandardBasicTypes.TIMESTAMP.nullSafeSet(st, timestamp, index, session);
  }
}
