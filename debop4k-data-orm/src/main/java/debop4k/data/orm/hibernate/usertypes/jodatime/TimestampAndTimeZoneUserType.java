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
import org.joda.time.DateTimeZone;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Joda-Time 의 DateTime의 정보를 {@link Timestamp}와 {@link DateTimeZone} 정보로 분리하여 저장하도록 합니다.
 * 로드 시에는 해당 TimeZone으로 설정된 {@link DateTime} 을 반환합니다.
 * <p>
 * Timestamp 와 TimeZone 으로 분리해서 저장하고, 로드 시에는 통합합니다.
 * <pre>
 * `@Columns`( columns = { @Column(name = "startTime"), @Column(name = "startTimeZone") })
 * `@Type`( `type` = "debop4s.data.orm.hibernate.usertype.jodatime.TimestampAndTimeZone")
 *  private DateTime startTZ;
 *
 * `@Columns`( columns = { @Column(name = "endTime"), @Column(name = "endTimeZone") })
 * `@Type`( `type` = "debop4s.data.orm.hibernate.usertype.jodatime.TimestampAndTimeZone")
 *  private DateTime endTZ;
 * </pre>
 *
 * @author sunghyouk.bae@gmail.com
 */
public class TimestampAndTimeZoneUserType extends BaseUserType {

  @Override
  public int[] sqlTypes() {
    return new int[]{StandardBasicTypes.TIMESTAMP.sqlType(), StandardBasicTypes.STRING.sqlType()};
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
    Timestamp timestamp = (Timestamp) StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names[0], session, owner);
    String zoneId = (String) StandardBasicTypes.STRING.nullSafeGet(rs, names[1], session, owner);

    if (timestamp != null) {
      return (zoneId != null) ? new DateTime(timestamp, DateTimeZone.forID(zoneId)) : new DateTime(timestamp, DateTimeZone.UTC);
    } else {
      return null;
    }
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
    DateTime time = (DateTime) value;
    if (time == null) {
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index, session);
      StandardBasicTypes.STRING.nullSafeSet(st, null, index + 1, session);
    } else {
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, new Timestamp(time.getMillis()), index, session);
      StandardBasicTypes.STRING.nullSafeSet(st, time.getZone().getID(), index + 1, session);
    }
  }
}
