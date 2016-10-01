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
import debop4k.timeperiod.models.Halfyear;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HalfyearRangeCollectionTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(HalfyearRangeCollectionTest.class);

  @Test
  public void yearBaseMonthTest() {

    DateTime moment = KodaTimes.asDate(2009, 2, 15);
    int year = Times.yearOf(moment.getYear(), moment.getMonthOfYear());
    HalfyearRangeCollection halfyears = new HalfyearRangeCollection(moment, 3);

    assertThat(halfyears.getStart()).isEqualTo(KodaTimes.asDate(year, 1, 1));
  }

  @Test
  public void singleHalfyearTest() {
    final int startYear = 2004;
    final Halfyear startHalfyear = Halfyear.Second;

    HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, 1);

    assertThat(halfyears.getHalfyearCount()).isEqualTo(1);
    assertThat(halfyears.getHalfyearOfStart()).isEqualTo(startHalfyear);
    assertThat(halfyears.getEndYear()).isEqualTo(startYear);
    assertThat(halfyears.getHalfyearOfEnd()).isEqualTo(startHalfyear);

    List<HalfyearRange> halfyearList = halfyears.halfyears();
    assertThat(halfyearList.size()).isEqualTo(1);
    assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
  }

  @Test
  public void firstCalendarHalfyears() {
    final int startYear = 2004;
    final Halfyear startHalfyear = Halfyear.First;
    final int halfyearCount = 3;

    HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, halfyearCount);

    assertThat(halfyears.getHalfyearCount()).isEqualTo(halfyearCount);
    assertThat(halfyears.getHalfyearOfStart()).isEqualTo(startHalfyear);
    assertThat(halfyears.getEndYear()).isEqualTo(startYear + 1);
    assertThat(halfyears.getHalfyearOfEnd()).isEqualTo(Halfyear.First);

    List<HalfyearRange> halfyearList = halfyears.halfyears();

    assertThat(halfyearList.size()).isEqualTo(halfyearCount);
    assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.First))).isTrue();
    assertThat(halfyearList.get(1).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
    assertThat(halfyearList.get(2).isSamePeriod(new HalfyearRange(2005, Halfyear.First))).isTrue();
  }

  @Test
  public void secondCalendarHalfyears() {
    final int startYear = 2004;
    final Halfyear startHalfyear = Halfyear.Second;
    final int halfyearCount = 3;

    HalfyearRangeCollection halfyears = new HalfyearRangeCollection(startYear, startHalfyear, halfyearCount);

    assertThat(halfyears.getHalfyearCount()).isEqualTo(halfyearCount);
    assertThat(halfyears.getHalfyearOfStart()).isEqualTo(startHalfyear);
    assertThat(halfyears.getEndYear()).isEqualTo(startYear + 1);
    assertThat(halfyears.getHalfyearOfEnd()).isEqualTo(Halfyear.Second);

    List<HalfyearRange> halfyearList = halfyears.halfyears();

    assertThat(halfyearList.size()).isEqualTo(halfyearCount);
    assertThat(halfyearList.get(0).isSamePeriod(new HalfyearRange(2004, Halfyear.Second))).isTrue();
    assertThat(halfyearList.get(1).isSamePeriod(new HalfyearRange(2005, Halfyear.First))).isTrue();
    assertThat(halfyearList.get(2).isSamePeriod(new HalfyearRange(2005, Halfyear.Second))).isTrue();
  }

}
