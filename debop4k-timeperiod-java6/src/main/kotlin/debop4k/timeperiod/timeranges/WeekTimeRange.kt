package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.relativeWeekPeriod
import debop4k.timeperiod.utils.startTimeOfWeek
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class WeekTimeRange(startTime: DateTime = today().startTimeOfWeek(),
                         val weekCount: Int,
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(startTime.relativeWeekPeriod(weekCount), calendar) {

  val year: Int get() = startYear
  val weekyear: Int get() = start.weekyear
  val weekOfWeekyear: Int get() = start.weekOfWeekyear

  val startWeekyear: Int get() = start.weekyear
  val startWeekOfWeekyear: Int get() = start.weekOfWeekyear
  val endWeekyear: Int get() = end.weekyear
  val endWeekOfWeekyear: Int get() = end.weekOfWeekyear

  fun dayStream(): FastList<DayRange> = TODO()
}