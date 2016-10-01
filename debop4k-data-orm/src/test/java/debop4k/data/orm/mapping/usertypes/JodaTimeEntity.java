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

package debop4k.data.orm.mapping.usertypes;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import debop4k.timeperiod.ITimePeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class JodaTimeEntity extends IntEntity {

  @Column(name = "jodaStartDateTime")
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeUserType")
  private DateTime start;

  @Column(name = "jodaEndDateTime")
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeUserType")
  private DateTime end;

  @Columns(columns = {@Column(name = "startTime"), @Column(name = "startTimeZone", length = 32)})
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.TimestampAndTimeZoneUserType")
  private DateTime startTZ;

  @Columns(columns = {@Column(name = "endTime"), @Column(name = "endTimeZone", length = 32)})
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.TimestampAndTimeZoneUserType")
  private DateTime endTZ;

  @Columns(columns = {@Column(name = "periodStart1"), @Column(name = "periodEnd1")})
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.TimePeriodAsTimestampUserType")
  private ITimePeriod period1;

  @Columns(columns = {@Column(name = "periodStart2"), @Column(name = "periodEnd2")})
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.TimePeriodAsTimestampUserType")
  private ITimePeriod period2;

  @Column(name = "timeAsString", length = 32)
  @Type(type = "debop4k.data.orm.hibernate.usertypes.jodatime.DateTimeAsIsoFormatStringUserType")
  private DateTime timeAsString;


  @Override
  public int hashCode() {
    return Hashx.compute(start, end, startTZ, endTZ, period1, period2, timeAsString);
  }

  private static final long serialVersionUID = 3604156129989196598L;
}
