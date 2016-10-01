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

package debop4k.timeperiod.timeranges;

import debop4k.core.kodatimes.KodaTimes;
import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.ITimeCalendar;
import debop4k.timeperiod.TimeCalendar;
import debop4k.timeperiod.TimeRange;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;


public class CalendarTimeRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(CalendarTimeRangeTest.class);

  @Test
  public void calendarTest() {
    ITimeCalendar calendar = new TimeCalendar();
    CalendarTimeRange timeRange = new CalendarTimeRange(TimeRange.AnyTime, calendar);

    assertThat(timeRange.getCalendar()).isEqualTo(calendar);
    assertThat(timeRange.isAnyTime()).isTrue();
  }

  @Test(expected = AssertionError.class)
  public void momentTest() {
    DateTime today = KodaTimes.today();
    CalendarTimeRange.of(today, today);
  }
}
