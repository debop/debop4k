package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.asDate
import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class HourRangeCollection(startTime: DateTime = now(),
                               hourCount: Int = 1,
                               calendar: ITimeCalendar = DefaultTimeCalendar)
: HourTimeRange(startTime, hourCount, calendar) {

  constructor(year: Int, monthOfYear: Int, hourCount: Int = 1, calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(asDate(year, monthOfYear), hourCount, calendar)

  fun hourStream(): FastList<HourRange> = TODO()
}