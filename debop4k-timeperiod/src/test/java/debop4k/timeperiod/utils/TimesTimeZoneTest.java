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

package debop4k.timeperiod.utils;

import debop4k.core.kodatimes.KodaTimex;
import debop4k.timeperiod.AbstractTimePeriodTest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;


public class TimesTimeZoneTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimesTimeZoneTest.class);

  @BeforeClass
  public static void beforeClass() {
    DateTimeZone.setDefault(DateTimeZone.UTC);
  }

  @Test
  public void withTimeZone() throws Exception {
    DateTimeZone tz = DateTimeZone.UTC;
    DateTime localTime = DateTime.now();

    DateTime utcTime = localTime.withZone(tz);

    log.debug("localTime=[{}]", localTime);
    log.debug("utcTime  =[{}]", utcTime);
  }

  @Test
  public void timezoneTest() throws Exception {
    DateTime dt = DateTime.now();
    String zoneId = dt.getZone().getID();
    DateTime utc = dt.withZone(DateTimeZone.UTC);

    DateTime local = new DateTime(utc, DateTimeZone.forID(zoneId));

    log.debug("time zone id=[{}]", zoneId);

    assertThat(local).isEqualTo(dt);
  }

  @Test
  public void availableDateTimeZoneTest() throws Exception {

    DateTime utcNow = DateTime.now(DateTimeZone.UTC);

    for (String id : DateTimeZone.getAvailableIDs()) {
      DateTimeZone tz = DateTimeZone.forID(id);
      DateTime tzNow = utcNow.toDateTime(tz);

      int offset = tz.getOffset(tzNow);
      log.debug("ID=[{}], TimeZone=[{}], Current=[{}], Now=[{}], offset=[{}]", id, tz, tzNow, utcNow, offset);
    }

    DateTime tokyo = utcNow.withZone(DateTimeZone.forID("Asia/Tokyo"));
    log.debug("now=[{}], Tokyo=[{}]", utcNow, tokyo);
  }

  @Test
  public void localDateTimeTest() throws Exception {
    DateTime utc = DateTime.now(DateTimeZone.UTC);
    LocalDateTime local = utc.toLocalDateTime();

    assertThat(local.toDateTime(DateTimeZone.UTC)).isEqualTo(utc);
  }

  @Test
  public void dateTimeZoneForOffsetMillis() throws Exception {
    DateTime utcNow = DateTime.now(DateTimeZone.UTC);
    LocalDateTime localNow = utcNow.toLocalDateTime();

    for (String id : DateTimeZone.getAvailableIDs()) {
      DateTimeZone tz = DateTimeZone.forID(id);
      DateTime tzTime = utcNow.toDateTime(tz);

      // TimeZone 별 offset 값
      int offset = tz.getOffset(0);
      log.debug("offset=[{}], TimeZone=[{}]", offset, tz);

      DateTimeZone localZone = DateTimeZone.forOffsetMillis(offset);
      DateTime localTimeZoneTime = utcNow.toDateTime(localZone);
      assertThat(localTimeZoneTime.getMillis()).isEqualTo(tzTime.getMillis());
    }
  }

  @Test
  public void timesGetTimezoneOffsetTest() throws Exception {
    DateTime utcNow = DateTime.now(DateTimeZone.UTC);

    for (String id : DateTimeZone.getAvailableIDs()) {
      DateTimeZone tz = DateTimeZone.forID(id);
      DateTime localNow = utcNow.toDateTime(tz);

      int offset = KodaTimex.timeZoneOffset(id);
      // offset=[32400000], TimeZone=[Asia/Seoul]
      log.debug("offset=[{}], TimeZone=[{}]", offset, tz);

      DateTimeZone localZone = KodaTimex.timeZoneForOffsetMillis(offset);

      // id=[ROK], offset=[32400000], localZone=[+09:00]
      log.debug("id=[{}], offset=[{}], localZone=[{}]", id, offset, localZone.getID());
      DateTime localTimeZoneTime = utcNow.toDateTime(localZone);

      assertThat(localTimeZoneTime.getMillis()).isEqualTo(localNow.getMillis());
    }
  }


}
