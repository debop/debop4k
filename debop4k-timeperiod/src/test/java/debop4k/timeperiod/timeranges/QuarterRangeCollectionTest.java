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
import debop4k.timeperiod.models.Quarter;
import debop4k.timeperiod.utils.Times;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class QuarterRangeCollectionTest extends AbstractTimePeriodTest {

  @Test
  public void yearBaseMonthTest() {

    DateTime moment = Times.asDate(2009, 2, 15);
    int year = Times.yearOf(moment.getYear(), moment.getMonthOfYear());
    QuarterRangeCollection quarterRanges = QuarterRangeCollection.of(moment, 3);

    assertThat(quarterRanges.getStart()).isEqualTo(Times.asDate(year, 1, 1));
  }

  @Test
  public void singleQuarterTest() {
    final int startYear = 2004;
    final Quarter startQuarter = Quarter.Second;

    QuarterRangeCollection quarterRanges = QuarterRangeCollection.of(startYear, startQuarter, 1);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(1);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(startQuarter);

    List<QuarterRange> quarters = quarterRanges.quarterStream();
    assertThat(quarters.size()).isEqualTo(1);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
  }

  @Test
  public void firstCalendarHalfyears() {

    final int startYear = 2004;
    final Quarter startQuarter = Quarter.First;
    final int quarterCount = 5;

    QuarterRangeCollection quarterRanges = QuarterRangeCollection.of(startYear, startQuarter, quarterCount);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(Quarter.First);

    List<QuarterRange> quarters = quarterRanges.quarterStream();

    assertThat(quarters.size()).isEqualTo(quarterCount);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.First))).isTrue();
    assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
    assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.Third))).isTrue();
    assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2004, Quarter.Fourth))).isTrue();
    assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.First))).isTrue();
  }

  @Test
  public void secondCalendarHalfyears() {
    final int startYear = 2004;
    final Quarter startQuarter = Quarter.Second;
    final int quarterCount = 5;

    QuarterRangeCollection quarterRanges = QuarterRangeCollection.of(startYear, startQuarter, quarterCount);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(Quarter.Second);

    List<QuarterRange> quarters = quarterRanges.quarterStream();

    assertThat(quarters.size()).isEqualTo(quarterCount);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.Second))).isTrue();
    assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.Third))).isTrue();
    assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.Fourth))).isTrue();
    assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2005, Quarter.First))).isTrue();
    assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.Second))).isTrue();
  }
}
