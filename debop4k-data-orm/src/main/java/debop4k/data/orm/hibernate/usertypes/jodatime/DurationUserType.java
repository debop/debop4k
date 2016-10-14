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
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.Duration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class DurationUserType extends BaseUserType {
  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.LONG.sqlType()};
  }

  @Override
  public Class returnedClass() {
    return Duration.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SharedSessionContractImplementor session,
                            Object owner) throws HibernateException, SQLException {
    Long millis = (Long) StandardBasicTypes.LONG.nullSafeGet(rs, names[0], session, owner);
    return (millis != null) ? Duration.millis(millis) : null;
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SharedSessionContractImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      StandardBasicTypes.LONG.nullSafeSet(st, null, index, session);
    } else {
      StandardBasicTypes.LONG.nullSafeSet(st, ((Duration) value).getMillis(), index, session);
    }
  }
}
