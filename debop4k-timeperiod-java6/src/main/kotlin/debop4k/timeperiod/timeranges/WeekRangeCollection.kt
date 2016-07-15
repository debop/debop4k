package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.startTimeOfWeek
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class WeekRangeCollection(startTime: DateTime = today().startTimeOfWeek(),
                               weekCount: Int = 1,
                               calendar: ITimeCalendar = DefaultTimeCalendar)
: WeekTimeRange(startTime, weekCount, calendar) {

  constructor(weekyear: Int,
              weekOfWeekyear: Int,
              weekCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(startTimeOfWeek(weekyear, weekOfWeekyear), weekCount, calendar)

  fun weekStream(): FastList<WeekRange> = TODO()

}