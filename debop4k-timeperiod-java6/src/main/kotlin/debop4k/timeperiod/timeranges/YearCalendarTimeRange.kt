package debop4k.timeperiod.timeranges

import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.TimeCalendar
import debop4k.timeperiod.TimePeriod

/**
 * Created by debop
 */
open class YearCalendarTimeRange(period: ITimePeriod = TimePeriod.AnyTime,
                                 calendar: ITimeCalendar = TimeCalendar.DEFAULT)
: CalendarTimeRange(period, calendar) {

  private val baseMonth = 1

  val baseYear: Int get() = startYear

}