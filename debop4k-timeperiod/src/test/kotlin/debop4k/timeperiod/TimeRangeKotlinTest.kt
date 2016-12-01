package debop4k.timeperiod

import debop4k.timeperiod.models.PeriodRelation
import debop4k.timeperiod.samples.TimeRangePeriodRelationTestData
import debop4k.timeperiod.utils.*
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.Test


/**
 * TimeRangeKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
class TimeRangeKotlinTest : AbstractTimePeriodKotlinTest() {

  val duration = Duration(Durations.hourOf(1))
  val offset = Durations.Second

  val start = now()
  val end = start + duration
  val testData = TimeRangePeriodRelationTestData(start, end, offset)

  @Test fun testAnyTime() {
    assertThat(TimeRange.AnyTime.start).isEqualTo(MinPeriodTime)
    assertThat(TimeRange.AnyTime.end).isEqualTo(MaxPeriodTime)

    assertThat(TimeRange.AnyTime.isAnyTime).isTrue()
    assertThat(TimeRange.AnyTime.readonly).isTrue()

    assertThat(TimeRange.AnyTime.hasPeriod).isFalse()
    assertThat(TimeRange.AnyTime.hasStart).isFalse()
    assertThat(TimeRange.AnyTime.hasEnd).isFalse()
    assertThat(TimeRange.AnyTime.isMoment).isFalse()
  }

  @Test fun defaultContructorTest() {
    val range = TimeRange()

    assertThat(range).isNotEqualTo(TimeRange.AnyTime)
    assertThat(range.relationWith(TimeRange.AnyTime)).isEqualTo(PeriodRelation.ExactMatch)

    assertThat(range.isAnyTime).isTrue()
    assertThat(range.readonly).isFalse()

    assertThat(range.hasPeriod).isFalse()
    assertThat(range.hasStart).isFalse()
    assertThat(range.hasEnd).isFalse()
    assertThat(range.isMoment).isFalse()
  }

  @Test fun momentTest() {
    val moment = now()
    val range = TimeRange(moment)

    assertThat(range.hasStart).isTrue()
    assertThat(range.hasEnd).isTrue()
    assertThat(range.duration).isEqualTo(MinDuration)

    assertThat(range.isAnyTime).isFalse()
    assertThat(range.isMoment).isTrue()
    assertThat(range.hasPeriod).isTrue()
  }

  @Test fun momentByPeriod() {
    val range = TimeRange(now(), Duration.ZERO)
    assertThat(range.isMoment).isTrue()
  }

  @Test fun nonMomentTest() {
    val range = TimeRange(now(), MinPositiveDuration)
    assertThat(range.isMoment).isFalse()
    assertThat(range.duration).isEqualTo(MinPositiveDuration)
  }

  @Test fun hasStartTest() {
    // 현재부터 ~
    val range = TimeRange(now(), null as DateTime?)
    assertThat(range.hasStart).isTrue()
    assertThat(range.hasEnd).isFalse()
  }

  @Test
  fun hasEndTest() {
    //  ~ 현재까지
    val range = TimeRange(null as DateTime?, now())
    assertThat(range.hasStart).isFalse()
    assertThat(range.hasEnd).isTrue()
  }

  @Test
  fun startEndTest() {
    val range = TimeRange(start, end)

    assertThat(range.start).isEqualTo(start)
    assertThat(range.end).isEqualTo(end)
    assertThat(range.duration).isEqualTo(duration)

    assertThat(range.hasPeriod).isTrue()
    assertThat(range.isAnyTime).isFalse()
    assertThat(range.isMoment).isFalse()
    assertThat(range.readonly).isFalse()
  }

  @Test
  fun startEndSwapTest() {
    val range = TimeRange(end, start)
    assertTimeRangeCreator(range)
  }

  @Test
  fun startAndDurationTest() {
    val range = TimeRange(start, duration)
    assertTimeRangeCreator(range)
  }

  private fun assertTimeRangeCreator(range: TimeRange) {
    assertThat(range.start).isEqualTo(start)
    assertThat(range.end).isEqualTo(end)
    assertThat(range.duration).isEqualTo(duration)

    assertThat(range.hasPeriod).isTrue()
    assertThat(range.isAnyTime).isFalse()
    assertThat(range.isMoment).isFalse()
    assertThat(range.readonly).isFalse()
  }

  @Test
  fun startAndNegateDurationTest() {
    val range = TimeRange(start, Durations.negate(duration))

    assertThat(range.start).isEqualTo(start.minus(duration))
    assertThat(range.end).isEqualTo(end.minus(duration))
    assertThat(range.duration).isEqualTo(duration)

    assertThat(range.hasPeriod).isTrue()
    assertThat(range.isAnyTime).isFalse()
    assertThat(range.isMoment).isFalse()
    assertThat(range.readonly).isFalse()
  }

  @Test
  fun copyConstructorTest() {
    val source = TimeRange(start, start.plusHours(1), true)
    val copy = TimeRange(source)

    assertThat(copy.start).isEqualTo(source.start)
    assertThat(copy.end).isEqualTo(source.end)
    assertThat(copy.duration).isEqualTo(source.duration)

    assertThat(copy.readonly).isEqualTo(source.readonly)

    assertThat(copy.hasPeriod).isTrue()
    assertThat(copy.isAnyTime).isFalse()
    assertThat(copy.isMoment).isFalse()
  }

  @Test
  fun startTest() {
    val range = TimeRange(start, start.plusHours(1))
    assertThat(range.start).isEqualTo(start)

    val chanedStart = start.plusHours(1)
    range.start = chanedStart
    assertThat(range.start).isEqualTo(chanedStart)
  }

  @Test(expected = AssertionError::class)
  fun startReadonlyTest() {
    val range = TimeRange(now(), Durations.hourOf(1), true)
    range.start = range.start.minusHours(2)
  }

  @Test(expected = AssertionError::class)
  fun startOutOfRangeTest() {
    val range = TimeRange(now(), Durations.hourOf(1), false)
    range.start = range.start.plusHours(2)
  }

  @Test
  @Throws(Exception::class)
  fun endTest() {
    val range = TimeRange(end.minusHours(1), end)
    assertThat(range.end).isEqualTo(end)

    val changedEnd = end.plusHours(1)
    range.end = changedEnd
    assertThat(range.end).isEqualTo(changedEnd)
  }

  @Test(expected = AssertionError::class)
  fun endReadonlyTest() {
    val range = TimeRange(now(), Durations.hourOf(1), true)
    range.end = range.end.plusHours(1)
  }

  @Test(expected = AssertionError::class)
  fun endOutOfRangeTest() {
    val range = TimeRange(now(), Durations.hourOf(1), false)
    range.end = range.end.minusHours(2)
  }

  @Test
  fun hasInsideDateTimeTest() {
    val range = TimeRange(start, end)

    assertThat(range.end).isEqualTo(end)

    assertThat(range.hasInside(start.minus(duration))).isFalse()
    assertThat(range.hasInside(start)).isTrue()
    assertThat(range.hasInside(start.plus(duration))).isTrue()

    assertThat(range.hasInside(end.minus(duration))).isTrue()
    assertThat(range.hasInside(end)).isTrue()
    assertThat(range.hasInside(end.plus(duration))).isFalse()
  }

  @Test
  fun hasInsidePeriodTest() {
    val range = TimeRange(start, end)

    assertThat(range.end).isEqualTo(end)

    // before
    val before1 = TimeRange(start.minusHours(2), start.minusHours(1))
    val before2 = TimeRange(start.minusMillis(1), end)
    val before3 = TimeRange(start.minusMillis(1), start)

    assertThat(range.hasInside(before1)).isFalse()
    assertThat(range.hasInside(before2)).isFalse()
    assertThat(range.hasInside(before3)).isFalse()

    // after
    val after1 = TimeRange(start.plusHours(1), end.plusHours(1))
    val after2 = TimeRange(start, end.plusMillis(1))
    val after3 = TimeRange(end, end.plusMillis(1))

    assertThat(range.hasInside(after1)).isFalse()
    assertThat(range.hasInside(after2)).isFalse()
    assertThat(range.hasInside(after3)).isFalse()

    // inside
    assertThat(range.hasInside(range)).isTrue()

    val inside1 = TimeRange(start.plusMillis(1), end)
    val inside2 = TimeRange(start.plusMillis(1), end.minusMillis(1))
    val inside3 = TimeRange(start, end.minusMillis(1))

    assertThat(range.hasInside(inside1)).isTrue()
    assertThat(range.hasInside(inside2)).isTrue()
    assertThat(range.hasInside(inside3)).isTrue()
  }

  @Test
  fun copyTest() {
    val readonlyTimeRange = TimeRange(start, end)
    assertThat(readonlyTimeRange.copy()).isEqualTo(readonlyTimeRange)
    assertThat(readonlyTimeRange.copy(Duration.ZERO)).isEqualTo(readonlyTimeRange)

    val range = TimeRange(start, end)

    assertThat(range.start).isEqualTo(start)
    assertThat(range.end).isEqualTo(end)

    val noMove = range.copy(Durations.Zero) as TimeRange

    assertThat(noMove.start).isEqualTo(range.start)
    assertThat(noMove.end).isEqualTo(range.end)
    assertThat(noMove.duration).isEqualTo(range.duration)
    assertThat(noMove).isEqualTo(noMove)

    val forwardOffset = Durations.hourOf(2, 30, 15)
    val forward = range.copy(forwardOffset) as TimeRange

    assertThat(forward.start).isEqualTo(start.plus(forwardOffset))
    assertThat(forward.end).isEqualTo(end.plus(forwardOffset))
    assertThat(forward.duration).isEqualTo(duration)

    val backwardOffset = Durations.hourOf(-1, 10, 30)
    val backward = range.copy(backwardOffset) as TimeRange

    assertThat(backward.start).isEqualTo(start.plus(backwardOffset))
    assertThat(backward.end).isEqualTo(end.plus(backwardOffset))
    assertThat(backward.duration).isEqualTo(duration)
  }

  @Test
  fun moveTest() {
    val moveZero = TimeRange(start, end)
    moveZero.move(Durations.Zero)
    assertThat(moveZero.start).isEqualTo(start)
    assertThat(moveZero.end).isEqualTo(end)
    assertThat(moveZero.duration).isEqualTo(duration)

    val forward = TimeRange(start, end)
    val forwardOffset = Durations.hourOf(2, 30, 15)
    forward.move(forwardOffset)

    assertThat(forward.start).isEqualTo(start.plus(forwardOffset))
    assertThat(forward.end).isEqualTo(end.plus(forwardOffset))
    assertThat(forward.duration).isEqualTo(duration)

    val backward = TimeRange(start, end)
    val backwardOffset = Durations.hourOf(-1, 10, 30)
    backward.move(backwardOffset)

    assertThat(backward.start).isEqualTo(start.plus(backwardOffset))
    assertThat(backward.end).isEqualTo(end.plus(backwardOffset))
    assertThat(backward.duration).isEqualTo(duration)
  }

  @Test
  fun expandStartToTest() {
    val range = TimeRange(start, end)

    range.expandStartTo(start.plusMillis(1))
    assertThat(range.start).isEqualTo(start)

    range.expandStartTo(start.minusMillis(1))
    assertThat(range.start).isEqualTo(start.minusMillis(1))
  }

  @Test
  fun expandEndToTest() {
    val range = TimeRange(start, end)

    range.expandEndTo(end.minusMillis(1))
    assertThat(range.end).isEqualTo(end)

    range.expandEndTo(end.plusMillis(1))
    assertThat(range.end).isEqualTo(end.plusMillis(1))
  }

  @Test
  fun expandToDateTimeTest() {
    val range = TimeRange(start, end)

    // start
    range.expandTo(start.plusMillis(1))
    assertThat(range.start).isEqualTo(start)

    range.expandTo(start.minusMillis(1))
    assertThat(range.start).isEqualTo(start.minusMillis(1))

    // endInclusive
    range.expandTo(end.minusMillis(1))
    assertThat(range.end).isEqualTo(end)

    range.expandTo(end.plusMillis(1))
    assertThat(range.end).isEqualTo(end.plusMillis(1))
  }

  @Test
  fun expandToPeriodTest() {
    val range = TimeRange(start, end)

    // no expansion
    range.expandTo(TimeRange(start.plusMillis(1), end.minusMillis(1)))
    assertThat(range.start).isEqualTo(start)
    assertThat(range.end).isEqualTo(end)

    // start
    var changedStart = start.minusMinutes(1)
    range.expandTo(TimeRange(changedStart, end))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(end)

    // endInclusive
    var changedEnd = end.plusMinutes(1)
    range.expandTo(TimeRange(changedStart, changedEnd))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(changedEnd)

    // start/endInclusive
    changedStart = changedStart.minusMinutes(1)
    changedEnd = changedEnd.plusMinutes(1)
    range.expandTo(TimeRange(changedStart, changedEnd))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(changedEnd)
  }

  @Test
  fun shrinkStartToTest() {
    val range = TimeRange(start, end)

    range.shrinkStartTo(start.minusMillis(1))
    assertThat(range.start).isEqualTo(start)

    range.shrinkStartTo(start.plusMillis(1))
    assertThat(range.start).isEqualTo(start.plusMillis(1))
  }

  @Test
  fun shrinkEndToTest() {
    val range = TimeRange(start, end)

    range.shrinkEndTo(end.plusMillis(1))
    assertThat(range.end).isEqualTo(end)

    range.shrinkEndTo(end.minusMillis(1))
    assertThat(range.end).isEqualTo(end.minusMillis(1))
  }

  @Test
  fun shrinkToDateTimeTest() {
    var range = TimeRange(start, end)

    // start
    range.shrinkTo(start.minusMillis(1))
    assertThat(range.start).isEqualTo(start)

    range.shrinkTo(start.plusMillis(1))
    assertThat(range.start).isEqualTo(start.plusMillis(1))

    range = TimeRange(start, end)

    // endInclusive
    range.shrinkTo(end.plusMillis(1))
    assertThat(range.end).isEqualTo(end)

    range.shrinkTo(end.minusMillis(1))
    assertThat(range.end).isEqualTo(end.minusMillis(1))
  }

  @Test
  fun shrinkToPeriodTest() {
    val range = TimeRange(start, end)

    // no expansion
    range.shrinkTo(TimeRange(start.minusMillis(1), end.plusMillis(1)))
    assertThat(range.start).isEqualTo(start)
    assertThat(range.end).isEqualTo(end)

    // start
    var changedStart = start.plusMinutes(1)
    range.shrinkTo(TimeRange(changedStart, end))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(end)

    // endInclusive
    var changedEnd = end.minusMinutes(1)
    range.shrinkTo(TimeRange(changedStart, changedEnd))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(changedEnd)

    // start/endInclusive
    changedStart = changedStart.plusMinutes(1)
    changedEnd = changedEnd.minusMinutes(1)
    range.shrinkTo(TimeRange(changedStart, changedEnd))
    assertThat(range.start).isEqualTo(changedStart)
    assertThat(range.end).isEqualTo(changedEnd)
  }

  @Test
  fun isSamePeriodTest() {
    val range1 = TimeRange(start, end)
    val range2 = TimeRange(start, end)

    assertThat(range1.isSamePeriod(range1)).isTrue()
    assertThat(range2.isSamePeriod(range2)).isTrue()

    assertThat(range1.isSamePeriod(range2)).isTrue()
    assertThat(range2.isSamePeriod(range1)).isTrue()

    assertThat(range1.isSamePeriod(TimeRange.AnyTime)).isFalse()
    assertThat(range2.isSamePeriod(TimeRange.AnyTime)).isFalse()

    range1.move(Durations.Millisecond)
    assertThat(range1.isSamePeriod(range2)).isFalse()
    assertThat(range2.isSamePeriod(range1)).isFalse()

    range1.move(Durations.millisOf(-1))
    assertThat(range1.isSamePeriod(range2)).isTrue()
    assertThat(range2.isSamePeriod(range1)).isTrue()
  }

  @Test
  fun hasInsideTest() {

    assertThat(testData.reference.hasInside(testData.before)).isFalse()
    assertThat(testData.reference.hasInside(testData.startTouching)).isFalse()
    assertThat(testData.reference.hasInside(testData.startInside)).isFalse()
    assertThat(testData.reference.hasInside(testData.insideStartTouching)).isFalse()

    assertThat(testData.reference.hasInside(testData.enclosingStartTouching)).isTrue()
    assertThat(testData.reference.hasInside(testData.enclosing)).isTrue()
    assertThat(testData.reference.hasInside(testData.enclosingEndTouching)).isTrue()
    assertThat(testData.reference.hasInside(testData.exactMatch)).isTrue()

    assertThat(testData.reference.hasInside(testData.inside)).isFalse()
    assertThat(testData.reference.hasInside(testData.insideEndTouching)).isFalse()
    assertThat(testData.reference.hasInside(testData.endTouching)).isFalse()
    assertThat(testData.reference.hasInside(testData.after)).isFalse()
  }

  @Test
  fun intersectsWithTest() {

    assertThat(testData.reference.intersectsWith(testData.before)).isFalse()
    assertThat(testData.reference.intersectsWith(testData.startTouching)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.startInside)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.insideStartTouching)).isTrue()

    assertThat(testData.reference.intersectsWith(testData.enclosingStartTouching)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.enclosing)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.enclosingEndTouching)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.exactMatch)).isTrue()

    assertThat(testData.reference.intersectsWith(testData.inside)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.insideEndTouching)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.endTouching)).isTrue()
    assertThat(testData.reference.intersectsWith(testData.after)).isFalse()
  }

  @Test
  fun overlapsWithTest() {

    assertThat(testData.reference.overlapsWith(testData.before)).isFalse()
    assertThat(testData.reference.overlapsWith(testData.startTouching)).isFalse()
    assertThat(testData.reference.overlapsWith(testData.startInside)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.insideStartTouching)).isTrue()

    assertThat(testData.reference.overlapsWith(testData.enclosingStartTouching)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.enclosing)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.enclosingEndTouching)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.exactMatch)).isTrue()

    assertThat(testData.reference.overlapsWith(testData.inside)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.insideEndTouching)).isTrue()
    assertThat(testData.reference.overlapsWith(testData.endTouching)).isFalse()
    assertThat(testData.reference.overlapsWith(testData.after)).isFalse()
  }

  @Test
  fun intersectsWithDateTimeTest() {
    val range = TimeRange(start, end)

    // before
    assertThat(range.intersectsWith(TimeRange(start.minusHours(2), start.minusHours(1)))).isFalse()
    assertThat(range.intersectsWith(TimeRange(start.minusHours(1), start))).isTrue()
    assertThat(range.intersectsWith(TimeRange(start.minusHours(1), start.plusMillis(1)))).isTrue()

    // after
    assertThat(range.intersectsWith(TimeRange(end.plusHours(1), end.plusHours(2)))).isFalse()
    assertThat(range.intersectsWith(TimeRange(end, end.plusMillis(1)))).isTrue()
    assertThat(range.intersectsWith(TimeRange(end.minusMillis(1), end.plusMillis(1)))).isTrue()

    // intersect
    assertThat(range.intersectsWith(range)).isTrue()
    assertThat(range.intersectsWith(TimeRange(start.minusMillis(1), end.plusHours(2)))).isTrue()
    assertThat(range.intersectsWith(TimeRange(start.minusMillis(1), start.plusMillis(1)))).isTrue()
    assertThat(range.intersectsWith(TimeRange(end.minusMillis(1), end.plusMillis(1)))).isTrue()
  }

  @Test
  fun getIntersectionTest() {
    val range = TimeRange(start, end)

    // before
    assertThat(range.intersection(TimeRange(start.minusHours(2), start.minusHours(1)))).isNull()
    assertThat(range.intersection(TimeRange(start.minusMillis(1), start))).isEqualTo(TimeRange(start))
    assertThat(range.intersection(TimeRange(start.minusHours(1), start.plusMillis(1)))).isEqualTo(TimeRange(start, start.plusMillis(1)))

    // after
    assertThat(range.intersection(TimeRange(end.plusHours(1), end.plusHours(2)))).isNull()
    assertThat(range.intersection(TimeRange(end, end.plusMillis(1)))).isEqualTo(TimeRange(end))
    assertThat(range.intersection(TimeRange(end.minusMillis(1), end.plusMillis(1)))).isEqualTo(TimeRange(end.minusMillis(1), end))

    // intersect
    assertThat(range.intersection(range)).isEqualTo(range)
    assertThat(range.intersection(TimeRange(start.minusMillis(1), end.plusMillis(1)))).isEqualTo(range)
    assertThat(range.intersection(TimeRange(start.plusMillis(1), end.minusMillis(1)))).isEqualTo(TimeRange(start.plusMillis(1), end.minusMillis(1)))
  }

  @Test
  fun getRelationTest() {
    assertThat(testData.reference.relation(testData.before)).isEqualTo(PeriodRelation.Before)
    assertThat(testData.reference.relation(testData.startTouching)).isEqualTo(PeriodRelation.StartTouching)
    assertThat(testData.reference.relation(testData.startInside)).isEqualTo(PeriodRelation.StartInside)
    assertThat(testData.reference.relation(testData.insideStartTouching)).isEqualTo(PeriodRelation.InsideStartTouching)
    assertThat(testData.reference.relation(testData.enclosing)).isEqualTo(PeriodRelation.Enclosing)
    assertThat(testData.reference.relation(testData.exactMatch)).isEqualTo(PeriodRelation.ExactMatch)
    assertThat(testData.reference.relation(testData.inside)).isEqualTo(PeriodRelation.Inside)
    assertThat(testData.reference.relation(testData.insideEndTouching)).isEqualTo(PeriodRelation.InsideEndTouching)
    assertThat(testData.reference.relation(testData.endInside)).isEqualTo(PeriodRelation.EndInside)
    assertThat(testData.reference.relation(testData.endTouching)).isEqualTo(PeriodRelation.EndTouching)
    assertThat(testData.reference.relation(testData.after)).isEqualTo(PeriodRelation.After)

    // reference
    assertThat(testData.reference.start).isEqualTo(start)
    assertThat(testData.reference.end).isEqualTo(end)
    assertThat(testData.reference.readonly).isTrue()

    // after
    assertThat(testData.after.readonly).isTrue()
    assertThat(testData.after.start.compareTo(start)).isLessThan(0)
    assertThat(testData.after.end.compareTo(start)).isLessThan(0)

    assertThat(testData.reference.hasInside(testData.after.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.after.end)).isFalse()
    assertThat(testData.reference.relation(testData.after)).isEqualTo(PeriodRelation.After)

    // start touching
    assertThat(testData.startTouching.readonly).isTrue()
    assertThat(testData.startTouching.start.millis).isLessThan(start.millis)
    assertThat(testData.startTouching.end).isEqualTo(start)

    assertThat(testData.reference.hasInside(testData.startTouching.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.startTouching.end)).isTrue()
    assertThat(testData.reference.relation(testData.startTouching)).isEqualTo(PeriodRelation.StartTouching)

    // start inside
    assertThat(testData.startInside.readonly).isTrue()
    assertThat(testData.startInside.start.millis).isLessThan(start.millis)
    assertThat(testData.startInside.end.millis).isLessThan(end.millis)

    assertThat(testData.reference.hasInside(testData.startInside.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.startInside.end)).isTrue()
    assertThat(testData.reference.relation(testData.startInside)).isEqualTo(PeriodRelation.StartInside)

    // inside start touching
    assertThat(testData.insideStartTouching.readonly).isTrue()
    assertThat(testData.insideStartTouching.start.millis).isEqualTo(start.millis)
    assertThat(testData.insideStartTouching.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.insideStartTouching.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.insideStartTouching.end)).isFalse()
    assertThat(testData.reference.relation(testData.insideStartTouching)).isEqualTo(PeriodRelation.InsideStartTouching)

    // enclosing start touching
    assertThat(testData.insideStartTouching.readonly).isTrue()
    assertThat(testData.insideStartTouching.start.millis).isEqualTo(start.millis)
    assertThat(testData.insideStartTouching.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.insideStartTouching.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.insideStartTouching.end)).isFalse()
    assertThat(testData.reference.relation(testData.insideStartTouching)).isEqualTo(PeriodRelation.InsideStartTouching)

    // enclosing
    assertThat(testData.enclosing.readonly).isTrue()
    assertThat(testData.enclosing.start.millis).isGreaterThan(start.millis)
    assertThat(testData.enclosing.end.millis).isLessThan(end.millis)

    assertThat(testData.reference.hasInside(testData.enclosing.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.enclosing.end)).isTrue()
    assertThat(testData.reference.relation(testData.enclosing)).isEqualTo(PeriodRelation.Enclosing)

    // enclosing endInclusive touching
    assertThat(testData.enclosingEndTouching.readonly).isTrue()
    assertThat(testData.enclosingEndTouching.start.millis).isGreaterThan(start.millis)
    assertThat(testData.enclosingEndTouching.end.millis).isEqualTo(end.millis)

    assertThat(testData.reference.hasInside(testData.enclosingEndTouching.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.enclosingEndTouching.end)).isTrue()
    assertThat(testData.reference.relation(testData.enclosingEndTouching)).isEqualTo(PeriodRelation.EnclosingEndTouching)

    // exact match
    assertThat(testData.exactMatch.readonly).isTrue()
    assertThat(testData.exactMatch.start.millis).isEqualTo(start.millis)
    assertThat(testData.exactMatch.end.millis).isEqualTo(end.millis)

    assertThat(testData.reference.hasInside(testData.exactMatch.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.exactMatch.end)).isTrue()
    assertThat(testData.reference.relation(testData.exactMatch)).isEqualTo(PeriodRelation.ExactMatch)

    // inside
    assertThat(testData.inside.readonly).isTrue()
    assertThat(testData.inside.start.millis).isLessThan(start.millis)
    assertThat(testData.inside.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.inside.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.inside.end)).isFalse()
    assertThat(testData.reference.relation(testData.inside)).isEqualTo(PeriodRelation.Inside)

    // inside endInclusive touching
    assertThat(testData.insideEndTouching.readonly).isTrue()
    assertThat(testData.insideEndTouching.start.millis).isLessThan(start.millis)
    assertThat(testData.insideEndTouching.end.millis).isEqualTo(end.millis)

    assertThat(testData.reference.hasInside(testData.insideEndTouching.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.insideEndTouching.end)).isTrue()
    assertThat(testData.reference.relation(testData.insideEndTouching)).isEqualTo(PeriodRelation.InsideEndTouching)

    // endInclusive inside
    assertThat(testData.endInside.readonly).isTrue()
    assertThat(testData.endInside.start.millis).isGreaterThan(start.millis)
    assertThat(testData.endInside.start.millis).isLessThan(end.millis)
    assertThat(testData.endInside.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.endInside.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.endInside.end)).isFalse()
    assertThat(testData.reference.relation(testData.endInside)).isEqualTo(PeriodRelation.EndInside)

    // endInclusive touching
    assertThat(testData.endTouching.readonly).isTrue()
    assertThat(testData.endTouching.start.millis).isEqualTo(end.millis)
    assertThat(testData.endTouching.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.endTouching.start)).isTrue()
    assertThat(testData.reference.hasInside(testData.endTouching.end)).isFalse()
    assertThat(testData.reference.relation(testData.endTouching)).isEqualTo(PeriodRelation.EndTouching)

    // before
    assertThat(testData.before.readonly).isTrue()
    assertThat(testData.before.start.millis).isGreaterThan(end.millis)
    assertThat(testData.before.end.millis).isGreaterThan(end.millis)

    assertThat(testData.reference.hasInside(testData.before.start)).isFalse()
    assertThat(testData.reference.hasInside(testData.before.end)).isFalse()
    assertThat(testData.reference.relation(testData.before)).isEqualTo(PeriodRelation.Before)
  }

  @Test
  fun resetTest() {
    val range = TimeRange(start, end)

    assertThat(range.start).isEqualTo(start)
    assertThat(range.hasStart).isTrue()
    assertThat(range.end).isEqualTo(end)
    assertThat(range.hasEnd).isTrue()

    range.reset()

    assertThat(range.start).isEqualTo(MinPeriodTime)
    assertThat(range.hasStart).isFalse()
    assertThat(range.end).isEqualTo(MaxPeriodTime)
    assertThat(range.hasEnd).isFalse()
  }

  @Test
  fun equalsTest() {
    val range1 = TimeRange(start, end)
    val range2 = TimeRange(start, end)
    val range3 = TimeRange(start.plusMillis(-1), end.plusMillis(1))
    val range4 = TimeRange(start, end, true)

    assertThat(range1).isEqualTo(range2)
    assertThat(range1).isNotEqualTo(range3)
    assertThat(range2).isEqualTo(range1)
    assertThat(range2).isNotEqualTo(range3)

    assertThat(range1).isNotEqualTo(range4)
  }

}