package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.today
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.utils.startTimeOfMonth
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class MonthRangeCollection(startTime: DateTime = today(),
                                monthCount: Int = 1,
                                calendar: ITimeCalendar = DefaultTimeCalendar)
: MonthTimeRange(startTime, monthCount, calendar) {

  constructor(year: Int,
              month: Int,
              monthCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar) :
  this(startTimeOfMonth(year, month), monthCount, calendar)


  fun monthStream(): FastList<MonthRange> = TODO()
}