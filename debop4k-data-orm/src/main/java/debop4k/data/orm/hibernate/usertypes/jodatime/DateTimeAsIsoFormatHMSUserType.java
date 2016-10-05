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

import debop4k.core.kodatimes.KodaTimex;
import debop4k.data.orm.hibernate.usertypes.BaseUserType;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Joda-Time 라이브러리의 {@link DateTime} 수형을 Database에 ISO8601 포맷 중 초단위까지만 저장합니다.
 * 로컬 시각 그대로 저장할 때 사용합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class DateTimeAsIsoFormatHMSUserType extends BaseUserType {

  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.STRING.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return DateTime.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SessionImplementor session,
                            Object owner) throws HibernateException, SQLException {
    String value = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names[0], session, owner);

    return KodaTimex.asIsoFormatDateHMS(value);
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SessionImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StandardBasicTypes.STRING.nullSafeSet(st, null, index, session);
    } else {
      String isoStr = KodaTimex.asIsoFormatDateHMSString((DateTime) value);
      StandardBasicTypes.STRING.nullSafeSet(st, isoStr, index, session);
    }
  }
}
