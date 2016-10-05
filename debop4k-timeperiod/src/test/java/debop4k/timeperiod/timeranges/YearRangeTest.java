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

import debop4k.timeperiod.AbstractTimePeriodTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import static debop4k.core.kodatimes.KodaTimex.asDate;
import static debop4k.core.kodatimes.KodaTimex.now;
import static debop4k.timeperiod.TimeCalendar.EMPTY_OFFSET;
import static debop4k.timeperiod.utils.Times.currentYear;
import static debop4k.timeperiod.utils.Times.startTimeOfYear;
import static org.assertj.core.api.Assertions.assertThat;


public class YearRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(YearRangeTest.class);

  @Test
  public void initValuesTest() {
    DateTime now = now();
    DateTime thisYear = startTimeOfYear(now);
    DateTime nextYear = thisYear.plusYears(1);

    YearRange yearRange = new YearRange(now, EMPTY_OFFSET);

    assertThat(yearRange.getStart().getYear()).isEqualTo(thisYear.getYear());
    assertThat(yearRange.getStart()).isEqualTo(thisYear);
    assertThat(yearRange.getEnd()).isEqualTo(nextYear);
  }

  @Test
  public void startYear() {
    int currentYear = currentYear().getYear();

    assertThat(new YearRange(asDate(2008, 7, 28)).getYear()).isEqualTo(2008);
  }

  @Test
  public void yearIndex() {
    int yearIndex = 1994;
    YearRange yearRange = new YearRange(yearIndex, EMPTY_OFFSET);
    assertThat(yearRange.isReadonly()).isTrue();
    assertThat(yearRange.getStart()).isEqualTo(startTimeOfYear(yearIndex));
    assertThat(yearRange.getEnd()).isEqualTo(startTimeOfYear(yearIndex + 1));
  }

  @Test
  public void addYearsTest() {

    final DateTime now = now();
    final DateTime startYear = startTimeOfYear(now);
    final YearRange yearRange = new YearRange(now);


    assertThat(yearRange.getPrevYear().getStart()).isEqualTo(startYear.plusYears(-1));
    assertThat(yearRange.getNextYear().getStart()).isEqualTo(startYear.plusYears(1));

    assertThat(yearRange.addYears(0)).isEqualTo(yearRange);

    for (int y = -60; y < 120; y++) {
      assertThat(yearRange.addYears(y).getStart())
          .isEqualTo(startYear.plusYears(y));
    }
  }
}
