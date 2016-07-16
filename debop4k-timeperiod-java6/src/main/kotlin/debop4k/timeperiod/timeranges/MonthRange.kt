package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.startTimeOfMonth
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MonthRange(startTime: DateTime = today(),
                      calendar: ITimeCalendar = DefaultTimeCalendar) :
    MonthTimeRange(startTime, 1, calendar) {

  constructor(year: Int, monthOfYear: Int, calendar: ITimeCalendar = DefaultTimeCalendar) :
  this(startTimeOfMonth(year, monthOfYear), calendar)

  val year: Int get() = startYear
  val monthOfYear: Int get() = startMonthOfYear

  fun addMonths(months: Int): MonthRange {
    return MonthRange(start.plusMonths(months), calendar)
  }

  val nextMonth: MonthRange get() = addMonths(1)
  val prevMonth: MonthRange get() = addMonths(-1)
}