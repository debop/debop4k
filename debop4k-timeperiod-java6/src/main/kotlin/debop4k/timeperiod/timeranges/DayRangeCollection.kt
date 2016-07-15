package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class DayRangeCollection(startTime: DateTime = today(),
                              dayCount: Int = 1,
                              calendar: ITimeCalendar = DefaultTimeCalendar)
: DayTimeRange(startTime, dayCount, calendar) {

  fun dayStream(): FastList<DayRange> {
    val days = FastList.newList<DayRange>(dayCount)
    for (i in 0 until dayCount) {
      days.add(DayRange(start.plusDays(i), calendar))
    }
    return days
  }
}