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
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.Halfyear;
import debop4k.timeperiod.models.Quarter;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class DurationsTest extends AbstractTimePeriodTest {

  private static final int currentYear = DateTime.now().getYear();
  private static final Locale currentLocale = Locale.getDefault();
  private static final Logger log = org.slf4j.LoggerFactory.getLogger(DurationsTest.class);

  @Test
  public void yearTest() {
    assertThat(Durations.yearOf(currentYear)).isEqualTo(new Duration(Times.startTimeOfYear(currentYear), Times.startTimeOfYear(currentYear + 1)));
    assertThat(Durations.yearOf(currentYear + 1)).isEqualTo(new Duration(Times.startTimeOfYear(currentYear + 1), Times.startTimeOfYear(currentYear + 2)));
    assertThat(Durations.yearOf(currentYear - 1)).isEqualTo(new Duration(Times.startTimeOfYear(currentYear - 1), Times.startTimeOfYear(currentYear)));


    assertThat(Durations.yearOf(currentYear)).isEqualTo(Durations.dayOf(Times.daysOfYear(currentYear)));
    assertThat(Durations.yearOf(currentYear + 1)).isEqualTo(Durations.dayOf(Times.daysOfYear(currentYear + 1)));
    assertThat(Durations.yearOf(currentYear - 1)).isEqualTo(Durations.dayOf(Times.daysOfYear(currentYear - 1)));
  }

  @Test
  public void halfyearTest() {
    for (Halfyear halfyear : Halfyear.values()) {

      int[] months = halfyear.getMonths();
      Duration duration = Durations.Zero;

      for (int month : months) {
        int daysInMonth = Times.daysInMonth(currentYear, month);
        duration = duration.plus(daysInMonth * TimeSpec.MillisPerDay);
      }
      assertThat(Durations.halfyearOf(currentYear, halfyear)).isEqualTo(duration);
    }
  }

  @Test
  public void quarterTest() {
    for (Quarter quarter : Quarter.values()) {
      int[] months = quarter.getMonths();
      Duration duration = Durations.Zero;

      for (int month : months) {
        int daysInMonth = Times.daysInMonth(currentYear, month);
        duration = duration.plus(daysInMonth * TimeSpec.MillisPerDay);
      }
      assertThat(Durations.quarterOf(currentYear, quarter)).isEqualTo(duration);
    }
  }

  @Test
  public void monthTest() {
    for (int i = 1; i <= TimeSpec.MonthsPerYear; i++) {
      assertThat(Durations.monthOf(currentYear, i)).isEqualTo(Duration.millis(Times.daysInMonth(currentYear, i) * TimeSpec.MillisPerDay));
    }
  }

  @Test
  public void weekTest() {
    assertThat(Durations.Week).isEqualTo(Duration.millis(TimeSpec.DaysPerWeek * TimeSpec.MillisPerDay));

    assertThat(Durations.weekOf(0)).isEqualTo(Durations.Zero);
    assertThat(Durations.weekOf(1).getMillis()).isEqualTo(TimeSpec.DaysPerWeek * TimeSpec.MillisPerDay);
    assertThat(Durations.weekOf(2).getMillis()).isEqualTo(TimeSpec.DaysPerWeek * TimeSpec.MillisPerDay * 2);

    assertThat(Durations.weekOf(-1).getMillis()).isEqualTo(TimeSpec.DaysPerWeek * TimeSpec.MillisPerDay * -1);
    assertThat(Durations.weekOf(-2).getMillis()).isEqualTo(TimeSpec.DaysPerWeek * TimeSpec.MillisPerDay * -2);
  }

  @Test
  public void dayTest() {
    assertThat(Durations.Day).isEqualTo(Duration.millis(TimeSpec.MillisPerDay));

    assertThat(Durations.dayOf(0)).isEqualTo(Durations.Zero);
    assertThat(Durations.dayOf(1)).isEqualTo(Duration.millis(TimeSpec.MillisPerDay));
    assertThat(Durations.dayOf(2)).isEqualTo(Duration.millis(TimeSpec.MillisPerDay * 2));
    assertThat(Durations.dayOf(-2)).isEqualTo(Duration.millis(TimeSpec.MillisPerDay * -2));

    assertThat(Durations.dayOf(1, 23)).isEqualTo(Duration.standardDays(1).plus(Duration.standardHours(23)));
    assertThat(Durations.dayOf(1, 23, 22)).isEqualTo(Duration.standardDays(1).plus(Duration.standardHours(23)).plus(Duration.standardMinutes(22)));
    assertThat(Durations.dayOf(1, 23, 22, 55)).isEqualTo(Duration.standardDays(1)
                                                                 .plus(Duration.standardHours(23))
                                                                 .plus(Duration.standardMinutes(22))
                                                                 .plus(Duration.standardSeconds(55)));
  }

  @Test
  public void hourTest() {
    assertThat(Durations.Hour).isEqualTo(Duration.standardHours(1));

    assertThat(Durations.hourOf(0)).isEqualTo(Duration.standardHours(0));
    assertThat(Durations.hourOf(1)).isEqualTo(Duration.standardHours(1));
    assertThat(Durations.hourOf(2)).isEqualTo(Duration.standardHours(2));
    assertThat(Durations.hourOf(-1)).isEqualTo(Duration.standardHours(-1));
    assertThat(Durations.hourOf(-2)).isEqualTo(Duration.standardHours(-2));

    assertThat(Durations.hourOf(23, 22)).isEqualTo(Duration.standardHours(23).plus(Duration.standardMinutes(22)));
    assertThat(Durations.hourOf(23, 22, 55)).isEqualTo(Duration.standardHours(23).plus(Duration.standardMinutes(22)).plus(Duration.standardSeconds(55)));
  }

  @Test
  public void minuteTest() {
    assertThat(Durations.Minute).isEqualTo(Duration.standardMinutes(1));

    assertThat(Durations.minuteOf(0)).isEqualTo(Duration.standardMinutes(0));
    assertThat(Durations.minuteOf(1)).isEqualTo(Duration.standardMinutes(1));
    assertThat(Durations.minuteOf(2)).isEqualTo(Duration.standardMinutes(2));
    assertThat(Durations.minuteOf(-1)).isEqualTo(Duration.standardMinutes(-1));
    assertThat(Durations.minuteOf(-2)).isEqualTo(Duration.standardMinutes(-2));

    assertThat(Durations.minuteOf(22, 55)).isEqualTo(Duration.standardMinutes(22).plus(Duration.standardSeconds(55)));
    assertThat(Durations.minuteOf(22, 55, 496)).isEqualTo(Duration.standardMinutes(22).plus(Duration.standardSeconds(55)).plus(496));
  }

  @Test
  public void secondTest() {
    assertThat(Durations.Second).isEqualTo(Duration.standardSeconds(1));

    assertThat(Durations.secondOf(0)).isEqualTo(Duration.standardSeconds(0));
    assertThat(Durations.secondOf(1)).isEqualTo(Duration.standardSeconds(1));
    assertThat(Durations.secondOf(2)).isEqualTo(Duration.standardSeconds(2));
    assertThat(Durations.secondOf(-1)).isEqualTo(Duration.standardSeconds(-1));
    assertThat(Durations.secondOf(-2)).isEqualTo(Duration.standardSeconds(-2));

    assertThat(Durations.secondOf(55, 496)).isEqualTo(Duration.standardSeconds(55).plus(496));
  }

  @Test
  public void millisecondTest() {
    assertThat(Durations.Millisecond).isEqualTo(Durations.millisOf(1));

    assertThat(Durations.millisOf(0)).isEqualTo(Durations.millisOf(0));
    assertThat(Durations.millisOf(1)).isEqualTo(Durations.millisOf(1));
    assertThat(Durations.millisOf(2)).isEqualTo(Durations.millisOf(2));
    assertThat(Durations.millisOf(-1)).isEqualTo(Durations.millisOf(-1));
    assertThat(Durations.millisOf(-2)).isEqualTo(Durations.millisOf(-2));
  }
}
