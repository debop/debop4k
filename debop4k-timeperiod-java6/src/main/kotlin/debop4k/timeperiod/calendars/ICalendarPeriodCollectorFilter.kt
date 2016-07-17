package debop4k.timeperiod.calendars

import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.DayRangeInMonth
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.timeranges.MonthRangeInYear
import org.eclipse.collections.impl.list.mutable.FastList

/**
 * Created by debop
 */
interface ICalendarPeriodCollectorFilter : ICalendarVisitorFilter {

  val collectingMonths: FastList<MonthRangeInYear>

  val collectingDays: FastList<DayRangeInMonth>

  val collectingHours: FastList<HourRangeInDay>

  val collectingDayHours: FastList<DayOfWeekHourRange>

}