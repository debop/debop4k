package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.relativeHourPeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HourTimeRange(startTime: DateTime = now(),
                         val hourCount: Int = 1,
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(startTime.relativeHourPeriod(hourCount), calendar) {

  val hourOfDayOfEnd: Int get() = end.hourOfDay

  fun minuteStream(): FastList<MinuteRange> = TODO()

}