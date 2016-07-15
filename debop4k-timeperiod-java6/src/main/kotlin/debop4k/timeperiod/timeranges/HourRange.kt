package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.core.kodatimes.trimToMinute
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HourRange(startTime: DateTime = now().trimToMinute(),
                     calendar: ITimeCalendar = DefaultTimeCalendar)
: HourTimeRange(startTime, 1, calendar) {

  val year: Int get() = startYear
  val monthOfYear: Int get() = startMonthOfYear
  val dayOfMonth: Int get() = startDayOfMonth
  val hourOfDay: Int get() = startHourOfDay

  fun addHours(hours: Int): HourRange {
    val startHour = start.trimToMinute()
    return HourRange(startHour.plusHours(hours), calendar)
  }

  fun nextHour(): HourRange = addHours(1)
  fun prevHour(): HourRange = addHours(-1)
}