package debop4k.timeperiod.calendars

import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.DayRangeInMonth
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.timeranges.MonthRangeInYear
import org.eclipse.collections.impl.list.mutable.FastList

/**
 * Created by debop
 */
open class CalendarPeriodCollectorFilter : CalendarVisitorFilter(), ICalendarPeriodCollectorFilter {

  private val _collectingMonths: FastList<MonthRangeInYear> = FastList.newList()
  private val _collectingDays: FastList<DayRangeInMonth> = FastList.newList()
  private val _collectingHours: FastList<HourRangeInDay> = FastList.newList()
  private val _collectingDayHours: FastList<DayOfWeekHourRange> = FastList.newList()

  override val collectingMonths: FastList<MonthRangeInYear>
    get() = _collectingMonths
  override val collectingDays: FastList<DayRangeInMonth>
    get() = _collectingDays

  override val collectingHours: FastList<HourRangeInDay>
    get() = _collectingHours

  override val collectingDayHours: FastList<DayOfWeekHourRange>
    get() = _collectingDayHours

  override fun clear() {
    super.clear()
    _collectingMonths.clear()
    _collectingDays.clear()
    _collectingHours.clear()
    _collectingDayHours.clear()
  }
}