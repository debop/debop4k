package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.daysInMonth
import debop4k.timeperiod.utils.relativeMonthPeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MonthTimeRange(startTime: DateTime = today(),
                          val monthCount: Int = 1,
                          calendar: ITimeCalendar = DefaultTimeCalendar) :
    CalendarTimeRange(startTime.relativeMonthPeriod(monthCount), calendar) {

  fun dayStream(): FastList<DayRange> {
    val days = FastList.newList<DayRange>()
    for (m in 0 until monthCount) {
      val monthStart = start.plusMonths(m)
      val dayCountInMonth = monthStart.daysInMonth()
      for (d in 0 until dayCountInMonth) {
        days.add(DayRange(monthStart.plusDays(d), calendar))
      }
    }
    return days
  }

}