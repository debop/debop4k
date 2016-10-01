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
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static debop4k.core.kodatimes.KodaTimes.asDate;
import static org.assertj.core.api.Assertions.assertThat;


public class QuarterRangeCollectionTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(QuarterRangeCollectionTest.class);

  @Test
  public void yearBaseMonthTest() {

    DateTime moment = asDate(2009, 2, 15);
    int year = Times.yearOf(moment.getYear(), moment.getMonthOfYear());
    QuarterRangeCollection quarterRanges = new QuarterRangeCollection(moment, 3);

    assertThat(quarterRanges.getStart()).isEqualTo(asDate(year, 1, 1));
  }

  @Test
  public void singleQuarterTest() {
    final int startYear = 2004;
    final Quarter startQuarter = Quarter.SECOND;

    QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, 1);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(1);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(startQuarter);

    List<QuarterRange> quarters = quarterRanges.quarters();
    assertThat(quarters.size()).isEqualTo(1);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.SECOND))).isTrue();
  }

  @Test
  public void firstCalendarHalfyears() {

    final int startYear = 2004;
    final Quarter startQuarter = Quarter.FIRST;
    final int quarterCount = 5;

    QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, quarterCount);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(Quarter.FIRST);

    List<QuarterRange> quarters = quarterRanges.quarters();

    assertThat(quarters.size()).isEqualTo(quarterCount);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.FIRST))).isTrue();
    assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.SECOND))).isTrue();
    assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.THIRD))).isTrue();
    assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2004, Quarter.FOURTH))).isTrue();
    assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.FIRST))).isTrue();
  }

  @Test
  public void secondCalendarHalfyears() {
    final int startYear = 2004;
    final Quarter startQuarter = Quarter.SECOND;
    final int quarterCount = 5;

    QuarterRangeCollection quarterRanges = new QuarterRangeCollection(startYear, startQuarter, quarterCount);

    assertThat(quarterRanges.getQuarterCount()).isEqualTo(quarterCount);
    assertThat(quarterRanges.getQuarterOfStart()).isEqualTo(startQuarter);
    assertThat(quarterRanges.getEndYear()).isEqualTo(startYear + 1);
    assertThat(quarterRanges.getQuarterOfEnd()).isEqualTo(Quarter.SECOND);

    List<QuarterRange> quarters = quarterRanges.quarters();

    assertThat(quarters.size()).isEqualTo(quarterCount);
    assertThat(quarters.get(0).isSamePeriod(new QuarterRange(2004, Quarter.SECOND))).isTrue();
    assertThat(quarters.get(1).isSamePeriod(new QuarterRange(2004, Quarter.THIRD))).isTrue();
    assertThat(quarters.get(2).isSamePeriod(new QuarterRange(2004, Quarter.FOURTH))).isTrue();
    assertThat(quarters.get(3).isSamePeriod(new QuarterRange(2005, Quarter.FIRST))).isTrue();
    assertThat(quarters.get(4).isSamePeriod(new QuarterRange(2005, Quarter.SECOND))).isTrue();
  }
}
