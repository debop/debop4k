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

package debop4k.timeperiod.base;

import debop4k.timeperiod.AbstractTimePeriodTest;
import debop4k.timeperiod.TimeRange;
import debop4k.timeperiod.TimeSpec;
import debop4k.timeperiod.models.PeriodRelation;
import debop4k.timeperiod.samples.TimeRangePeriodRelationTestData;
import debop4k.timeperiod.utils.Durations;
import debop4k.timeperiod.utils.Periods;
import debop4k.timeperiod.utils.Times;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeRangeTest extends AbstractTimePeriodTest {

  private static final Logger log = org.slf4j.LoggerFactory.getLogger(TimeRangeTest.class);
  private final Duration duration = new Duration(Durations.hourOf(1));
  private final Duration offset = Durations.Second;

  private DateTime start = DateTime.now();
  private DateTime end = start.plus(duration);
  private TimeRangePeriodRelationTestData testData = new TimeRangePeriodRelationTestData(start, end, offset);

  @Test
  public void anytimeTest() throws Exception {
    assertThat(TimeRange.AnyTime.getStart()).isEqualTo(TimeSpec.MinPeriodTime);
    assertThat(TimeRange.AnyTime.getEnd()).isEqualTo(TimeSpec.MaxPeriodTime);

    assertThat(TimeRange.AnyTime.isAnyTime()).isTrue();
    assertThat(TimeRange.AnyTime.isReadonly()).isTrue();

    assertThat(TimeRange.AnyTime.hasPeriod()).isFalse();
    assertThat(TimeRange.AnyTime.hasStart()).isFalse();
    assertThat(TimeRange.AnyTime.hasEnd()).isFalse();
    assertThat(TimeRange.AnyTime.isMoment()).isFalse();
  }

  @Test
  public void defaultContructorTest() throws Exception {
    TimeRange range = new TimeRange();

    assertThat(range).isNotEqualTo(TimeRange.AnyTime);
    assertThat(Periods.relation(range, TimeRange.AnyTime)).isEqualTo(PeriodRelation.ExactMatch);

    assertThat(range.isAnyTime()).isTrue();
    assertThat(range.isReadonly()).isFalse();

    assertThat(range.hasPeriod()).isFalse();
    assertThat(range.hasStart()).isFalse();
    assertThat(range.hasEnd()).isFalse();
    assertThat(range.isMoment()).isFalse();
  }

  @Test
  public void momentTest() throws Exception {
    DateTime moment = Times.now();
    TimeRange range = new TimeRange(moment);

    assertThat(range.hasStart()).isTrue();
    assertThat(range.hasEnd()).isTrue();
    assertThat(range.getDuration()).isEqualTo(TimeSpec.MinDuration);

    assertThat(range.isAnyTime()).isFalse();
    assertThat(range.isMoment()).isTrue();
    assertThat(range.hasPeriod()).isTrue();
  }

  @Test
  public void momentByPeriod() {
    TimeRange range = new TimeRange(Times.now(), Duration.ZERO);
    assertThat(range.isMoment()).isTrue();
  }

  @Test
  public void nonMomentTest() {
    TimeRange range = new TimeRange(Times.now(), TimeSpec.MinPositiveDuration);
    assertThat(range.isMoment()).isFalse();
    assertThat(range.getDuration()).isEqualTo(TimeSpec.MinPositiveDuration);
  }

  @Test
  public void hasStartTest() {
    // 현재부터 ~
    TimeRange range = new TimeRange(Times.now(), (DateTime) null);
    assertThat(range.hasStart()).isTrue();
    assertThat(range.hasEnd()).isFalse();
  }

  @Test
  public void hasEndTest() {
    //  ~ 현재까지
    TimeRange range = new TimeRange(null, Times.now());
    assertThat(range.hasStart()).isFalse();
    assertThat(range.hasEnd()).isTrue();
  }

  @Test
  public void startEndTest() {
    TimeRange range = new TimeRange(start, end);

    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.getEnd()).isEqualTo(end);
    assertThat(range.getDuration()).isEqualTo(duration);

    assertThat(range.hasPeriod()).isTrue();
    assertThat(range.isAnyTime()).isFalse();
    assertThat(range.isMoment()).isFalse();
    assertThat(range.isReadonly()).isFalse();
  }

  @Test
  public void startEndSwapTest() {
    TimeRange range = new TimeRange(end, start);
    assertTimeRangeCreator(range);
  }

  @Test
  public void startAndDurationTest() {
    TimeRange range = new TimeRange(start, duration);
    assertTimeRangeCreator(range);
  }

  private void assertTimeRangeCreator(TimeRange range) {
    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.getEnd()).isEqualTo(end);
    assertThat(range.getDuration()).isEqualTo(duration);

    assertThat(range.hasPeriod()).isTrue();
    assertThat(range.isAnyTime()).isFalse();
    assertThat(range.isMoment()).isFalse();
    assertThat(range.isReadonly()).isFalse();
  }

  @Test
  public void startAndNegateDurationTest() {
    TimeRange range = new TimeRange(start, Durations.negate(duration));

    assertThat(range.getStart()).isEqualTo(start.minus(duration));
    assertThat(range.getEnd()).isEqualTo(end.minus(duration));
    assertThat(range.getDuration()).isEqualTo(duration);

    assertThat(range.hasPeriod()).isTrue();
    assertThat(range.isAnyTime()).isFalse();
    assertThat(range.isMoment()).isFalse();
    assertThat(range.isReadonly()).isFalse();
  }

  @Test
  public void copyConstructorTest() {
    TimeRange source = new TimeRange(start, start.plusHours(1), true);
    TimeRange copy = new TimeRange(source);

    assertThat(copy.getStart()).isEqualTo(source.getStart());
    assertThat(copy.getEnd()).isEqualTo(source.getEnd());
    assertThat(copy.getDuration()).isEqualTo(source.getDuration());

    assertThat(copy.isReadonly()).isEqualTo(source.isReadonly());

    assertThat(copy.hasPeriod()).isTrue();
    assertThat(copy.isAnyTime()).isFalse();
    assertThat(copy.isMoment()).isFalse();
  }

  @Test
  public void startTest() {
    TimeRange range = new TimeRange(start, start.plusHours(1));
    assertThat(range.getStart()).isEqualTo(start);

    DateTime chanedStart = start.plusHours(1);
    range.setStart(chanedStart);
    assertThat(range.getStart()).isEqualTo(chanedStart);
  }

  @Test(expected = AssertionError.class)
  public void startReadonlyTest() {
    TimeRange range = new TimeRange(Times.now(), Durations.hourOf(1), true);
    range.setStart(range.getStart().minusHours(2));
  }

  @Test(expected = AssertionError.class)
  public void startOutOfRangeTest() {
    TimeRange range = new TimeRange(Times.now(), Durations.hourOf(1), false);
    range.setStart(range.getStart().plusHours(2));
  }

  @Test
  public void endTest() throws Exception {
    TimeRange range = new TimeRange(end.minusHours(1), end);
    assertThat(range.getEnd()).isEqualTo(end);

    DateTime changedEnd = end.plusHours(1);
    range.setEnd(changedEnd);
    assertThat(range.getEnd()).isEqualTo(changedEnd);
  }

  @Test(expected = AssertionError.class)
  public void endReadonlyTest() {
    TimeRange range = new TimeRange(Times.now(), Durations.hourOf(1), true);
    range.setEnd(range.getEnd().plusHours(1));
  }

  @Test(expected = AssertionError.class)
  public void endOutOfRangeTest() {
    TimeRange range = new TimeRange(Times.now(), Durations.hourOf(1), false);
    range.setEnd(range.getEnd().minusHours(2));
  }

  @Test
  public void hasInsideDateTimeTest() {
    TimeRange range = new TimeRange(start, end);

    assertThat(range.getEnd()).isEqualTo(end);

    assertThat(range.hasInside(start.minus(duration))).isFalse();
    assertThat(range.hasInside(start)).isTrue();
    assertThat(range.hasInside(start.plus(duration))).isTrue();

    assertThat(range.hasInside(end.minus(duration))).isTrue();
    assertThat(range.hasInside(end)).isTrue();
    assertThat(range.hasInside(end.plus(duration))).isFalse();
  }

  @Test
  public void hasInsidePeriodTest() {
    TimeRange range = new TimeRange(start, end);

    assertThat(range.getEnd()).isEqualTo(end);

    // before
    TimeRange before1 = new TimeRange(start.minusHours(2), start.minusHours(1));
    TimeRange before2 = new TimeRange(start.minusMillis(1), end);
    TimeRange before3 = new TimeRange(start.minusMillis(1), start);

    assertThat(range.hasInside(before1)).isFalse();
    assertThat(range.hasInside(before2)).isFalse();
    assertThat(range.hasInside(before3)).isFalse();

    // after
    TimeRange after1 = new TimeRange(start.plusHours(1), end.plusHours(1));
    TimeRange after2 = new TimeRange(start, end.plusMillis(1));
    TimeRange after3 = new TimeRange(end, end.plusMillis(1));

    assertThat(range.hasInside(after1)).isFalse();
    assertThat(range.hasInside(after2)).isFalse();
    assertThat(range.hasInside(after3)).isFalse();

    // inside
    assertThat(range.hasInside(range)).isTrue();

    TimeRange inside1 = new TimeRange(start.plusMillis(1), end);
    TimeRange inside2 = new TimeRange(start.plusMillis(1), end.minusMillis(1));
    TimeRange inside3 = new TimeRange(start, end.minusMillis(1));

    assertThat(range.hasInside(inside1)).isTrue();
    assertThat(range.hasInside(inside2)).isTrue();
    assertThat(range.hasInside(inside3)).isTrue();
  }

  @Test
  public void copyTest() {
    TimeRange readonlyTimeRange = new TimeRange(start, end);
    assertThat(readonlyTimeRange.copy()).isEqualTo(readonlyTimeRange);
    assertThat(readonlyTimeRange.copy(Duration.ZERO)).isEqualTo(readonlyTimeRange);

    TimeRange range = new TimeRange(start, end);

    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.getEnd()).isEqualTo(end);

    TimeRange noMove = (TimeRange) range.copy(Durations.Zero);

    assertThat(noMove.getStart()).isEqualTo(range.getStart());
    assertThat(noMove.getEnd()).isEqualTo(range.getEnd());
    assertThat(noMove.getDuration()).isEqualTo(range.getDuration());
    assertThat(noMove).isEqualTo(noMove);

    Duration forwardOffset = Durations.hourOf(2, 30, 15);
    TimeRange forward = (TimeRange) range.copy(forwardOffset);

    assertThat(forward.getStart()).isEqualTo(start.plus(forwardOffset));
    assertThat(forward.getEnd()).isEqualTo(end.plus(forwardOffset));
    assertThat(forward.getDuration()).isEqualTo(duration);

    Duration backwardOffset = Durations.hourOf(-1, 10, 30);
    TimeRange backward = (TimeRange) range.copy(backwardOffset);

    assertThat(backward.getStart()).isEqualTo(start.plus(backwardOffset));
    assertThat(backward.getEnd()).isEqualTo(end.plus(backwardOffset));
    assertThat(backward.getDuration()).isEqualTo(duration);
  }

  @Test
  public void moveTest() {
    TimeRange moveZero = new TimeRange(start, end);
    moveZero.move(Durations.Zero);
    assertThat(moveZero.getStart()).isEqualTo(start);
    assertThat(moveZero.getEnd()).isEqualTo(end);
    assertThat(moveZero.getDuration()).isEqualTo(duration);

    TimeRange forward = new TimeRange(start, end);
    Duration forwardOffset = Durations.hourOf(2, 30, 15);
    forward.move(forwardOffset);

    assertThat(forward.getStart()).isEqualTo(start.plus(forwardOffset));
    assertThat(forward.getEnd()).isEqualTo(end.plus(forwardOffset));
    assertThat(forward.getDuration()).isEqualTo(duration);

    TimeRange backward = new TimeRange(start, end);
    Duration backwardOffset = Durations.hourOf(-1, 10, 30);
    backward.move(backwardOffset);

    assertThat(backward.getStart()).isEqualTo(start.plus(backwardOffset));
    assertThat(backward.getEnd()).isEqualTo(end.plus(backwardOffset));
    assertThat(backward.getDuration()).isEqualTo(duration);
  }

  @Test
  public void expandStartToTest() {
    TimeRange range = new TimeRange(start, end);

    range.expandStartTo(start.plusMillis(1));
    assertThat(range.getStart()).isEqualTo(start);

    range.expandStartTo(start.minusMillis(1));
    assertThat(range.getStart()).isEqualTo(start.minusMillis(1));
  }

  @Test
  public void expandEndToTest() {
    TimeRange range = new TimeRange(start, end);

    range.expandEndTo(end.minusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end);

    range.expandEndTo(end.plusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end.plusMillis(1));
  }

  @Test
  public void expandToDateTimeTest() {
    TimeRange range = new TimeRange(start, end);

    // start
    range.expandTo(start.plusMillis(1));
    assertThat(range.getStart()).isEqualTo(start);

    range.expandTo(start.minusMillis(1));
    assertThat(range.getStart()).isEqualTo(start.minusMillis(1));

    // endInclusive
    range.expandTo(end.minusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end);

    range.expandTo(end.plusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end.plusMillis(1));
  }

  @Test
  public void expandToPeriodTest() {
    TimeRange range = new TimeRange(start, end);

    // no expansion
    range.expandTo(new TimeRange(start.plusMillis(1), end.minusMillis(1)));
    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.getEnd()).isEqualTo(end);

    // start
    DateTime changedStart = start.minusMinutes(1);
    range.expandTo(new TimeRange(changedStart, end));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(end);

    // endInclusive
    DateTime changedEnd = end.plusMinutes(1);
    range.expandTo(new TimeRange(changedStart, changedEnd));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(changedEnd);

    // start/endInclusive
    changedStart = changedStart.minusMinutes(1);
    changedEnd = changedEnd.plusMinutes(1);
    range.expandTo(new TimeRange(changedStart, changedEnd));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(changedEnd);
  }

  @Test
  public void shrinkStartToTest() {
    TimeRange range = new TimeRange(start, end);

    range.shrinkStartTo(start.minusMillis(1));
    assertThat(range.getStart()).isEqualTo(start);

    range.shrinkStartTo(start.plusMillis(1));
    assertThat(range.getStart()).isEqualTo(start.plusMillis(1));
  }

  @Test
  public void shrinkEndToTest() {
    TimeRange range = new TimeRange(start, end);

    range.shrinkEndTo(end.plusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end);

    range.shrinkEndTo(end.minusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end.minusMillis(1));
  }

  @Test
  public void shrinkToDateTimeTest() {
    TimeRange range = new TimeRange(start, end);

    // start
    range.shrinkTo(start.minusMillis(1));
    assertThat(range.getStart()).isEqualTo(start);

    range.shrinkTo(start.plusMillis(1));
    assertThat(range.getStart()).isEqualTo(start.plusMillis(1));

    range = new TimeRange(start, end);

    // endInclusive
    range.shrinkTo(end.plusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end);

    range.shrinkTo(end.minusMillis(1));
    assertThat(range.getEnd()).isEqualTo(end.minusMillis(1));
  }

  @Test
  public void shrinkToPeriodTest() {
    TimeRange range = new TimeRange(start, end);

    // no expansion
    range.shrinkTo(new TimeRange(start.minusMillis(1), end.plusMillis(1)));
    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.getEnd()).isEqualTo(end);

    // start
    DateTime changedStart = start.plusMinutes(1);
    range.shrinkTo(new TimeRange(changedStart, end));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(end);

    // endInclusive
    DateTime changedEnd = end.minusMinutes(1);
    range.shrinkTo(new TimeRange(changedStart, changedEnd));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(changedEnd);

    // start/endInclusive
    changedStart = changedStart.plusMinutes(1);
    changedEnd = changedEnd.minusMinutes(1);
    range.shrinkTo(new TimeRange(changedStart, changedEnd));
    assertThat(range.getStart()).isEqualTo(changedStart);
    assertThat(range.getEnd()).isEqualTo(changedEnd);
  }

  @Test
  public void isSamePeriodTest() {
    TimeRange range1 = new TimeRange(start, end);
    TimeRange range2 = new TimeRange(start, end);

    assertThat(range1.isSamePeriod(range1)).isTrue();
    assertThat(range2.isSamePeriod(range2)).isTrue();

    assertThat(range1.isSamePeriod(range2)).isTrue();
    assertThat(range2.isSamePeriod(range1)).isTrue();

    assertThat(range1.isSamePeriod(TimeRange.AnyTime)).isFalse();
    assertThat(range2.isSamePeriod(TimeRange.AnyTime)).isFalse();

    range1.move(Durations.Millisecond);
    assertThat(range1.isSamePeriod(range2)).isFalse();
    assertThat(range2.isSamePeriod(range1)).isFalse();

    range1.move(Durations.millisOf(-1));
    assertThat(range1.isSamePeriod(range2)).isTrue();
    assertThat(range2.isSamePeriod(range1)).isTrue();
  }

  @Test
  public void hasInsideTest() {

    assertThat(testData.getReference().hasInside(testData.getBefore())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getStartTouching())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getStartInside())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getInsideStartTouching())).isFalse();

    assertThat(testData.getReference().hasInside(testData.getEnclosingStartTouching())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEnclosing())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEnclosingEndTouching())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getExactMatch())).isTrue();

    assertThat(testData.getReference().hasInside(testData.getInside())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getInsideEndTouching())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getEndTouching())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getAfter())).isFalse();
  }

  @Test
  public void intersectsWithTest() {

    assertThat(testData.getReference().intersectsWith(testData.getBefore())).isFalse();
    assertThat(testData.getReference().intersectsWith(testData.getStartTouching())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getStartInside())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getInsideStartTouching())).isTrue();

    assertThat(testData.getReference().intersectsWith(testData.getEnclosingStartTouching())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getEnclosing())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getEnclosingEndTouching())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getExactMatch())).isTrue();

    assertThat(testData.getReference().intersectsWith(testData.getInside())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getInsideEndTouching())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getEndTouching())).isTrue();
    assertThat(testData.getReference().intersectsWith(testData.getAfter())).isFalse();
  }

  @Test
  public void overlapsWithTest() {

    assertThat(testData.getReference().overlapsWith(testData.getBefore())).isFalse();
    assertThat(testData.getReference().overlapsWith(testData.getStartTouching())).isFalse();
    assertThat(testData.getReference().overlapsWith(testData.getStartInside())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getInsideStartTouching())).isTrue();

    assertThat(testData.getReference().overlapsWith(testData.getEnclosingStartTouching())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getEnclosing())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getEnclosingEndTouching())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getExactMatch())).isTrue();

    assertThat(testData.getReference().overlapsWith(testData.getInside())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getInsideEndTouching())).isTrue();
    assertThat(testData.getReference().overlapsWith(testData.getEndTouching())).isFalse();
    assertThat(testData.getReference().overlapsWith(testData.getAfter())).isFalse();
  }

  @Test
  public void intersectsWithDateTimeTest() {
    TimeRange range = new TimeRange(start, end);

    // before
    assertThat(range.intersectsWith(new TimeRange(start.minusHours(2), start.minusHours(1)))).isFalse();
    assertThat(range.intersectsWith(new TimeRange(start.minusHours(1), start))).isTrue();
    assertThat(range.intersectsWith(new TimeRange(start.minusHours(1), start.plusMillis(1)))).isTrue();

    // after
    assertThat(range.intersectsWith(new TimeRange(end.plusHours(1), end.plusHours(2)))).isFalse();
    assertThat(range.intersectsWith(new TimeRange(end, end.plusMillis(1)))).isTrue();
    assertThat(range.intersectsWith(new TimeRange(end.minusMillis(1), end.plusMillis(1)))).isTrue();

    // intersect
    assertThat(range.intersectsWith(range)).isTrue();
    assertThat(range.intersectsWith(new TimeRange(start.minusMillis(1), end.plusHours(2)))).isTrue();
    assertThat(range.intersectsWith(new TimeRange(start.minusMillis(1), start.plusMillis(1)))).isTrue();
    assertThat(range.intersectsWith(new TimeRange(end.minusMillis(1), end.plusMillis(1)))).isTrue();
  }

  @Test
  public void getIntersectionTest() {
    TimeRange range = new TimeRange(start, end);

    // before
    assertThat(range.intersection(new TimeRange(start.minusHours(2), start.minusHours(1)))).isNull();
    assertThat(range.intersection(new TimeRange(start.minusMillis(1), start))).isEqualTo(new TimeRange(start));
    assertThat(range.intersection(new TimeRange(start.minusHours(1), start.plusMillis(1)))).isEqualTo(new TimeRange(start, start.plusMillis(1)));

    // after
    assertThat(range.intersection(new TimeRange(end.plusHours(1), end.plusHours(2)))).isNull();
    assertThat(range.intersection(new TimeRange(end, end.plusMillis(1)))).isEqualTo(new TimeRange(end));
    assertThat(range.intersection(new TimeRange(end.minusMillis(1), end.plusMillis(1)))).isEqualTo(new TimeRange(end.minusMillis(1), end));

    // intersect
    assertThat(range.intersection(range)).isEqualTo(range);
    assertThat(range.intersection(new TimeRange(start.minusMillis(1), end.plusMillis(1)))).isEqualTo(range);
    assertThat(range.intersection(new TimeRange(start.plusMillis(1), end.minusMillis(1)))).isEqualTo(new TimeRange(start.plusMillis(1), end.minusMillis(1)));
  }

  @Test
  public void getRelationTest() {
    assertThat(testData.getReference().relation(testData.getBefore())).isEqualTo(PeriodRelation.Before);
    assertThat(testData.getReference().relation(testData.getStartTouching())).isEqualTo(PeriodRelation.StartTouching);
    assertThat(testData.getReference().relation(testData.getStartInside())).isEqualTo(PeriodRelation.StartInside);
    assertThat(testData.getReference().relation(testData.getInsideStartTouching())).isEqualTo(PeriodRelation.InsideStartTouching);
    assertThat(testData.getReference().relation(testData.getEnclosing())).isEqualTo(PeriodRelation.Enclosing);
    assertThat(testData.getReference().relation(testData.getExactMatch())).isEqualTo(PeriodRelation.ExactMatch);
    assertThat(testData.getReference().relation(testData.getInside())).isEqualTo(PeriodRelation.Inside);
    assertThat(testData.getReference().relation(testData.getInsideEndTouching())).isEqualTo(PeriodRelation.InsideEndTouching);
    assertThat(testData.getReference().relation(testData.getEndInside())).isEqualTo(PeriodRelation.EndInside);
    assertThat(testData.getReference().relation(testData.getEndTouching())).isEqualTo(PeriodRelation.EndTouching);
    assertThat(testData.getReference().relation(testData.getAfter())).isEqualTo(PeriodRelation.After);

    // reference
    assertThat(testData.getReference().getStart()).isEqualTo(start);
    assertThat(testData.getReference().getEnd()).isEqualTo(end);
    assertThat(testData.getReference().isReadonly()).isTrue();

    // after
    assertThat(testData.getAfter().isReadonly()).isTrue();
    assertThat(testData.getAfter().getStart().compareTo(start)).isLessThan(0);
    assertThat(testData.getAfter().getEnd().compareTo(start)).isLessThan(0);

    assertThat(testData.getReference().hasInside(testData.getAfter().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getAfter().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getAfter())).isEqualTo(PeriodRelation.After);

    // start touching
    assertThat(testData.getStartTouching().isReadonly()).isTrue();
    assertThat(testData.getStartTouching().getStart().getMillis()).isLessThan(start.getMillis());
    assertThat(testData.getStartTouching().getEnd()).isEqualTo(start);

    assertThat(testData.getReference().hasInside(testData.getStartTouching().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getStartTouching().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getStartTouching())).isEqualTo(PeriodRelation.StartTouching);

    // start inside
    assertThat(testData.getStartInside().isReadonly()).isTrue();
    assertThat(testData.getStartInside().getStart().getMillis()).isLessThan(start.getMillis());
    assertThat(testData.getStartInside().getEnd().getMillis()).isLessThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getStartInside().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getStartInside().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getStartInside())).isEqualTo(PeriodRelation.StartInside);

    // inside start touching
    assertThat(testData.getInsideStartTouching().isReadonly()).isTrue();
    assertThat(testData.getInsideStartTouching().getStart().getMillis()).isEqualTo(start.getMillis());
    assertThat(testData.getInsideStartTouching().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getInsideStartTouching().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getInsideStartTouching().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getInsideStartTouching())).isEqualTo(PeriodRelation.InsideStartTouching);

    // enclosing start touching
    assertThat(testData.getInsideStartTouching().isReadonly()).isTrue();
    assertThat(testData.getInsideStartTouching().getStart().getMillis()).isEqualTo(start.getMillis());
    assertThat(testData.getInsideStartTouching().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getInsideStartTouching().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getInsideStartTouching().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getInsideStartTouching())).isEqualTo(PeriodRelation.InsideStartTouching);

    // enclosing
    assertThat(testData.getEnclosing().isReadonly()).isTrue();
    assertThat(testData.getEnclosing().getStart().getMillis()).isGreaterThan(start.getMillis());
    assertThat(testData.getEnclosing().getEnd().getMillis()).isLessThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getEnclosing().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEnclosing().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getEnclosing())).isEqualTo(PeriodRelation.Enclosing);

    // enclosing endInclusive touching
    assertThat(testData.getEnclosingEndTouching().isReadonly()).isTrue();
    assertThat(testData.getEnclosingEndTouching().getStart().getMillis()).isGreaterThan(start.getMillis());
    assertThat(testData.getEnclosingEndTouching().getEnd().getMillis()).isEqualTo(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getEnclosingEndTouching().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEnclosingEndTouching().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getEnclosingEndTouching())).isEqualTo(PeriodRelation.EnclosingEndTouching);

    // exact match
    assertThat(testData.getExactMatch().isReadonly()).isTrue();
    assertThat(testData.getExactMatch().getStart().getMillis()).isEqualTo(start.getMillis());
    assertThat(testData.getExactMatch().getEnd().getMillis()).isEqualTo(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getExactMatch().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getExactMatch().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getExactMatch())).isEqualTo(PeriodRelation.ExactMatch);

    // inside
    assertThat(testData.getInside().isReadonly()).isTrue();
    assertThat(testData.getInside().getStart().getMillis()).isLessThan(start.getMillis());
    assertThat(testData.getInside().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getInside().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getInside().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getInside())).isEqualTo(PeriodRelation.Inside);

    // inside endInclusive touching
    assertThat(testData.getInsideEndTouching().isReadonly()).isTrue();
    assertThat(testData.getInsideEndTouching().getStart().getMillis()).isLessThan(start.getMillis());
    assertThat(testData.getInsideEndTouching().getEnd().getMillis()).isEqualTo(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getInsideEndTouching().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getInsideEndTouching().getEnd())).isTrue();
    assertThat(testData.getReference().relation(testData.getInsideEndTouching())).isEqualTo(PeriodRelation.InsideEndTouching);

    // endInclusive inside
    assertThat(testData.getEndInside().isReadonly()).isTrue();
    assertThat(testData.getEndInside().getStart().getMillis()).isGreaterThan(start.getMillis());
    assertThat(testData.getEndInside().getStart().getMillis()).isLessThan(end.getMillis());
    assertThat(testData.getEndInside().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getEndInside().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEndInside().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getEndInside())).isEqualTo(PeriodRelation.EndInside);

    // endInclusive touching
    assertThat(testData.getEndTouching().isReadonly()).isTrue();
    assertThat(testData.getEndTouching().getStart().getMillis()).isEqualTo(end.getMillis());
    assertThat(testData.getEndTouching().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getEndTouching().getStart())).isTrue();
    assertThat(testData.getReference().hasInside(testData.getEndTouching().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getEndTouching())).isEqualTo(PeriodRelation.EndTouching);

    // before
    assertThat(testData.getBefore().isReadonly()).isTrue();
    assertThat(testData.getBefore().getStart().getMillis()).isGreaterThan(end.getMillis());
    assertThat(testData.getBefore().getEnd().getMillis()).isGreaterThan(end.getMillis());

    assertThat(testData.getReference().hasInside(testData.getBefore().getStart())).isFalse();
    assertThat(testData.getReference().hasInside(testData.getBefore().getEnd())).isFalse();
    assertThat(testData.getReference().relation(testData.getBefore())).isEqualTo(PeriodRelation.Before);
  }

  @Test
  public void resetTest() {
    TimeRange range = new TimeRange(start, end);

    assertThat(range.getStart()).isEqualTo(start);
    assertThat(range.hasStart()).isTrue();
    assertThat(range.getEnd()).isEqualTo(end);
    assertThat(range.hasEnd()).isTrue();

    range.reset();

    assertThat(range.getStart()).isEqualTo(TimeSpec.MinPeriodTime);
    assertThat(range.hasStart()).isFalse();
    assertThat(range.getEnd()).isEqualTo(TimeSpec.MaxPeriodTime);
    assertThat(range.hasEnd()).isFalse();
  }

  @Test
  public void equalsTest() {
    TimeRange range1 = new TimeRange(start, end);
    TimeRange range2 = new TimeRange(start, end);
    TimeRange range3 = new TimeRange(start.plusMillis(-1), end.plusMillis(1));
    TimeRange range4 = new TimeRange(start, end, true);

    assertThat(range1).isEqualTo(range2);
    assertThat(range1).isNotEqualTo(range3);
    assertThat(range2).isEqualTo(range1);
    assertThat(range2).isNotEqualTo(range3);

    assertThat(range1).isNotEqualTo(range4);
  }
}
