package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.timeranges.*
import org.slf4j.LoggerFactory

/**
 * 특정 기간에 대한 필터링 정보를 기반으로 기간들을 필터링 할 수 있도록 특정 기간을 탐색하는 Visitor입니다.
 * Created by debop
 */
abstract class CalendarVisitor<F : ICalendarVisitorFilter, C : ICalendarVisitorContext>(
    val filter: F,
    val limits: ITimePeriod,
    val seekDirection: SeekDirection = SeekDirection.Forward,
    val calendar: ITimeCalendar = DefaultTimeCalendar) {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    val MAX_PERIOD = TimeRange(MinPeriodTime, MaxPeriodTime.minusYears(1))
  }

  val isForward: Boolean get() = seekDirection == SeekDirection.Forward

  protected fun startPeriodVisit(context: C): Unit {
    startPeriodVisit(limits, context)
  }

  protected fun startPeriodVisit(period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitYears(yearsToVisit: MutableList<YearRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitMonths(monthsToVisit: MutableList<MonthRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitDays(daysToVisit: MutableList<DayRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  private fun visitHours(hoursToVisit: MutableList<HourRange>, period: ITimePeriod, context: C): Unit {
    TODO()
  }

  protected fun startYearVisit(year: YearRange, context: C, seekDirection: SeekDirection): YearRange {
    TODO()
  }

  protected fun startMonthVisit(month: MonthRange, context: C, seekDirection: SeekDirection): MonthRange {
    TODO()
  }

  protected fun startDayVisit(day: DayRange, context: C, seekDirection: SeekDirection): DayRange {
    TODO()
  }

  protected fun startHourVisit(hour: HourRange, context: C, seekDirection: SeekDirection): HourRange {
    TODO()
  }

  protected fun onVisitStart(): Unit {
  }

  protected fun onVisitEnd(): Unit {
  }

  protected fun checkLimits(target: ITimePeriod): Boolean {
    return limits.hasInside(target)
  }

  protected fun checkExcludePeriods(target: ITimePeriod): Boolean {
    return filter.excludePeriods.isEmpty()
        || filter.excludePeriods.overlapPeriods(target).isEmpty
  }

  protected fun enterYears(years: YearRangeCollection, context: C): Boolean = true
  protected fun enterMonths(year: YearRange, context: C): Boolean = true
  protected fun enterDays(month: MonthRange, context: C): Boolean = true
  protected fun enterHours(day: DayRange, context: C): Boolean = true
  protected fun enterMinutes(hour: HourRange, context: C): Boolean = true

  protected fun onVisitYears(years: YearRangeCollection, context: C): Boolean = true
  protected fun onVisitYear(year: YearRange, context: C): Boolean = true
  protected fun onVisitMonth(month: MonthRange, context: C): Boolean = true
  protected fun onVisitDay(day: DayRange, context: C): Boolean = true
  protected fun onVisitHour(hour: HourRange, context: C): Boolean = true
  protected fun onVisitMinute(minute: MinuteRange, context: C): Boolean = true

  protected fun isMatchingYear(yr: YearRange, context: C): Boolean {
    TODO()
  }

  protected fun isMatchingMonth(mr: MonthRange, context: C): Boolean {
    TODO()
  }

  protected fun isMatchingDay(dr: DayRange, context: C): Boolean {
    TODO()
  }

  protected fun isMatchingHour(hr: HourRange, context: C): Boolean {
    TODO()
  }

  protected fun isMatchingMinute(mr: MinuteRange, context: C): Boolean {
    TODO()
  }

}
