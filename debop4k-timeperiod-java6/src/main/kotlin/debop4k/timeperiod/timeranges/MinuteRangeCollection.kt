package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MinuteRangeCollection(startTime: DateTime = now(),
                                 minuteCount: Int = 1,
                                 calendar: ITimeCalendar = DefaultTimeCalendar)
: MinuteTimeRange(startTime, minuteCount, calendar) {

  fun minuteStream(): FastList<MinuteRange> = TODO()
  fun minutes(): FastList<MinuteRange> = minuteStream()
}