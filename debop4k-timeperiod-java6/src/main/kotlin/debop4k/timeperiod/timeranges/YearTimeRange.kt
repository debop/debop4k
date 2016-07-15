package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.TimeCalendar
import debop4k.timeperiod.utils.relativeYearPeriod
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class YearTimeRange(val year: Int,
                         val yearCount: Int = 1,
                         calendar: ITimeCalendar = DefaultTimeCalendar)
: YearCalendarTimeRange(relativeYearPeriod(year, yearCount),
                        calendar) {

  constructor() : this(now().year)

  constructor(m: DateTime,
              yearCount: Int = 1,
              calendar: ITimeCalendar = DefaultTimeCalendar)
  : this(m.year, yearCount, calendar)


  fun halfyearStream(): FastList<HalfyearRange> = TODO()
  fun quarterStream(): FastList<QuarterRange> = TODO()
  fun monthStream(): FastList<MonthRange> = TODO()
  fun dayStream(): FastList<DayRange> = TODO()
  fun hourStream(): FastList<HourRange> = TODO()
  fun MinuteStream(): FastList<MinuteRange> = TODO()


  companion object {

    @JvmStatic
    @JvmOverloads
    fun of(calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearTimeRange {
      return YearTimeRange(now().year, 1, calendar)
    }

    @JvmStatic
    @JvmOverloads
    fun of(m: DateTime, yearCount: Int = 1, calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearTimeRange {
      return YearTimeRange(m.year, yearCount)
    }

  }
}

