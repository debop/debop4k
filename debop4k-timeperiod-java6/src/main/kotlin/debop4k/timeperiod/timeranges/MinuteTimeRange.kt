package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.relativeMinutePeriod
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MinuteTimeRange(moment: DateTime,
                           val minuteCount: Int = 1,
                           calendar: ITimeCalendar = DefaultTimeCalendar)
: CalendarTimeRange(moment.relativeMinutePeriod(minuteCount), calendar) {

  constructor(minuteCount: Int, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(now(), minuteCount, calendar)

  val minuteOfHourOfEnd: Int get() = end.minuteOfHour
}