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

import debop4k.core.kodatimes.KodaTimes;
import debop4k.data.orm.hibernate.usertypes.BaseCompositeUserType;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimePeriod;
import debop4k.timeperiod.TimeRange;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.joda.time.DateTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * {@link ITimePeriod} 정보중 시작일자와 완료일자를 Timestamp 값으로 저장하도록 합니다.
 * <p>
 * <pre><code>
 * @Columns( columns = { @Column(name = "startTimestamp"), @Column(name = "endTimestamp") } )
 * @hba.Type( `type` = "debop4s.data.orm.hibernate.usertype.jodatime.TimePeriodAsTimestamp")
 * private ITimePeriod period;
 * </code></pre>
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class TimePeriodAsTimestampUserType extends BaseCompositeUserType {

  protected ITimePeriod asTimePeriod(Object value) {
    if (value != null && value instanceof ITimePeriod) {
      return (ITimePeriod) value;
    }
    return null;
  }

  @Override
  public String[] getPropertyNames() {
    return new String[]{"startTimestamp", "endTimestamp"};
  }

  @Override
  public Type[] getPropertyTypes() {
    return new Type[]{TimestampType.INSTANCE, TimestampType.INSTANCE};
  }

  @Override
  public Object getPropertyValue(Object component, int property) throws HibernateException {
    ITimePeriod period = asTimePeriod(component);
    if (period != null) {
      return (property == 0) ? period.getStart() : period.getEnd();
    }
    return null;
  }

  @Override
  public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
    ITimePeriod period = asTimePeriod(component);
    if (period != null) {
      switch (property) {
        case 0:
          period.setup((DateTime) value, period.getEnd());
        case 1:
          period.setup(period.getStart(), (DateTime) value);
      }
    }
  }

  @Override
  public Class returnedClass() {
    return TimeRange.class;
  }

  @Override
  public Object nullSafeGet(ResultSet rs,
                            String[] names,
                            SessionImplementor session,
                            Object owner) throws HibernateException, SQLException {
    Timestamp start = (Timestamp) StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names[0], session, owner);
    Timestamp end = (Timestamp) StandardBasicTypes.TIMESTAMP.nullSafeGet(rs, names[1], session, owner);

    return new TimeRange(KodaTimes.toDateTime(start), KodaTimes.toDateTime(end));
  }

  @Override
  public void nullSafeSet(PreparedStatement st,
                          Object value,
                          int index,
                          SessionImplementor session) throws HibernateException, SQLException {

    ITimePeriod period = asTimePeriod(value);
    if (period == null) {
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index, session);
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, null, index + 1, session);
    } else {
      Timestamp start = period.hasStart() ? new Timestamp(period.getStart().getMillis()) : null;
      Timestamp end = period.hasEnd() ? new Timestamp(period.getEnd().getMillis()) : null;
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, start, index, session);
      StandardBasicTypes.TIMESTAMP.nullSafeSet(st, end, index + 1, session);
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    return (value != null) ? new TimePeriod(asTimePeriod(value)) : null;
  }
}
