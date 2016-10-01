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

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimePeriod;
import debop4k.timeperiod.TimeRange;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class TimesTest extends AbstractTimePeriodTest {

  @Test
  public void asStringTest() {
    ITimePeriod period = new TimeRange(testDate, testNow);
    String periodString = asString(period);

    log.debug("periodString=[{}]", periodString);
    assertThat(periodString).isNotEmpty();
  }

  @Test
  public void toDateTimeTest() {
    String dateString = testDate.toString();
    log.debug("dateString=[{}]", dateString);

    DateTime parsedTime = toDateTime(dateString);

    assertThat(parsedTime.isEqual(testDate)).isTrue();

    parsedTime = toDateTime("", testNow);
    assertThat(parsedTime).isEqualTo(testNow);
  }

  @Test
  public void parseString() {
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
    DateTime dateTime = formatter.parseDateTime("20131010");
    assertThat(dateTime).isEqualTo(asDate(2013, 10, 10));
  }

  @Test
  public void testAsIsoFormatDateTime() {
    DateTime now = now();
    DateTime converted = asIsoFormatDateTime(asIsoFormatDateTimeString(now));
    assertThat(converted.compareTo(now)).isEqualTo(0);

    now = now.withZone(DateTimeZone.forID("EST"));
    converted = asIsoFormatDateTime(asIsoFormatDateTimeString(now));
    assertThat(converted.compareTo(now)).isEqualTo(0);
    log.debug("converted={}, now={}", converted, now);
  }
}
