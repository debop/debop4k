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
import debop4k.timeperiod.models.YearWeek;
import debop4k.timeperiod.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.joda.time.DateTime;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class WeekRangeCollectionTest extends AbstractTimePeriodTest {

  @Test
  public void singleMonths() {
    final int startYear = 2004;
    final int startWeek = 22;

    WeekRangeCollection weekRanges = WeekRangeCollection.of(startYear, startWeek, 1);
    assertThat(weekRanges.getWeekCount()).isEqualTo(1);

    assertThat(weekRanges.getStartYear()).isEqualTo(startYear);
    assertThat(weekRanges.getEndYear()).isEqualTo(startYear);
    assertThat(weekRanges.getStartWeekOfWeekyear()).isEqualTo(startWeek);
    assertThat(weekRanges.getEndWeekOfWeekyear()).isEqualTo(startWeek);

    MutableList<WeekRange> weeks = weekRanges.weekStream();
    assertThat(weeks.size()).isEqualTo(1);
    assertThat(weeks.get(0).isSamePeriod(new WeekRange(startYear, startWeek))).isTrue();
  }

  @Test
  public void calenarWeeks() {
    final int startYear = 2004;
    final int startWeek = 22;
    final int weekCount = 5;

    WeekRangeCollection weekRanges = WeekRangeCollection.of(startYear, startWeek, weekCount);

    assertThat(weekRanges.getWeekCount()).isEqualTo(weekCount);
    assertThat(weekRanges.getStartYear()).isEqualTo(startYear);
    assertThat(weekRanges.getStartWeekOfWeekyear()).isEqualTo(startWeek);
    assertThat(weekRanges.getEndYear()).isEqualTo(startYear);
    assertThat(weekRanges.getEndWeekOfWeekyear()).isEqualTo((startWeek + weekCount - 1));
  }

  @Test
  public void weeksCountsTest() {
    int[] weekCounts = new int[]{1, 6, 48, 180, 365};

    final DateTime now = Times.now();
    final DateTime today = Times.today();

    for (int weekCount : weekCounts) {
      final WeekRangeCollection weekRanges = WeekRangeCollection.of(now, weekCount);

      final DateTime startTime = weekRanges.getCalendar().mapStart(Times.startTimeOfWeek(today));
      final DateTime endTime = weekRanges.getCalendar().mapEnd(startTime.plusWeeks(weekCount));

      assertThat(weekRanges.getStart()).isEqualTo(startTime);
      assertThat(weekRanges.getEnd()).isEqualTo(endTime);

      final MutableList<WeekRange> items = weekRanges.weekStream();

      for (int w = 0; w < weekCount; w++) {
        final WeekRange item = items.get(w);
        assertThat(item.getStart()).isEqualTo(startTime.plusWeeks(w));
        assertThat(item.getEnd()).isEqualTo(weekRanges.getCalendar().mapEnd(startTime.plusWeeks(w + 1)));

        assertThat(item.unmappedStart()).isEqualTo(startTime.plusWeeks(w));
        assertThat(item.unmappedEnd()).isEqualTo(startTime.plusWeeks(w + 1));

        assertThat(item.isSamePeriod(new WeekRange(weekRanges.getStart().plusWeeks(w)))).isTrue();

        YearWeek yw = Times.addWeeks(YearWeek.of(now), w);
        assertThat(item.isSamePeriod(new WeekRange(yw))).isTrue();
      }
    }
  }
}
