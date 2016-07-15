package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.relativeDayPeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class DayTimeRange(startTime: DateTime = today(),
                        val dayCount: Int = 1,
                        calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(startTime.relativeDayPeriod(dayCount), calendar) {

  fun hourStream(): FastList<HourRange> = TODO()
}