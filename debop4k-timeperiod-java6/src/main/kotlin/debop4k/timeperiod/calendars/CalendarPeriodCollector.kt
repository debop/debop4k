package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.timeranges.*

/**
 * Created by debop
 */
open class CalendarPeriodCollector(
    filter: CalendarPeriodCollectorFilter,
    limits: ITimePeriod,
    seekDir: SeekDirection = SeekDirection.Forward,
    calendar: ITimeCalendar = DefaultTimeCalendar) :
    CalendarVisitor<CalendarPeriodCollectorFilter, CalendarPeriodCollectorContext>(
        filter, limits, seekDir, calendar) {

  val periods: ITimePeriodCollection = TimePeriodCollection()

  fun collectYears(): Unit = collectByScope(CollectKind.Year)
  fun collectMonths(): Unit = collectByScope(CollectKind.Month)
  fun collectDays(): Unit = collectByScope(CollectKind.Day)
  fun collectHours(): Unit = collectByScope(CollectKind.Hour)
  fun collectMinutes(): Unit = collectByScope(CollectKind.Minute)

  private fun collectByScope(scope: CollectKind): Unit {
    val context = CalendarPeriodCollectorContext(scope)
    startPeriodVisit(context)
  }

  override fun enterYears(years: YearRangeCollection, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Year.value
  }

  override fun enterMonths(year: YearRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Month.value
  }

  override fun enterDays(month: MonthRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Day.value
  }

  override fun enterHours(day: DayRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Hour.value
  }

  override fun enterMinutes(hour: HourRange, context: CalendarPeriodCollectorContext): Boolean {
    return context.scope.value > CollectKind.Minute.value
  }

  override fun onVisitYears(years: YearRangeCollection, context: CalendarPeriodCollectorContext): Boolean {
    TODO()
  }

  override fun onVisitYear(year: YearRange, context: CalendarPeriodCollectorContext): Boolean {
    TODO()
  }

  override fun onVisitMonth(month: MonthRange, context: CalendarPeriodCollectorContext): Boolean {
    TODO()
  }

  override fun onVisitDay(day: DayRange, context: CalendarPeriodCollectorContext): Boolean {
    TODO()
  }
}