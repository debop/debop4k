package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.joda.time.DateTime
import java.time.DayOfWeek

/**
 * Created by debop
 */
open class DayRange(startTime: DateTime = today(),
                    calendar: ITimeCalendar = DefaultTimeCalendar)
: DayTimeRange(startTime, 1, calendar) {

  val year: Int get() = startYear
  val monthOfYear: Int get() = startMonthOfYear
  val dayOfMonth: Int get() = startDayOfMonth
  val dayOfWeek: DayOfWeek get() = startDayOfWeek

  fun addDays(days: Int): DayRange {
    return DayRange(start.plusDays(days), calendar)
  }

  fun nextDay(): DayRange = addDays(1)
  fun prevDay(): DayRange = addDays(-1)
}