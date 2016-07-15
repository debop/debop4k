package debop4k.timeperiod.timeranges

import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.TimeCalendar
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class YearRangeCollection(year: Int,
                               yearCount: Int,
                               calendar: ITimeCalendar = TimeCalendar.DEFAULT)
: YearTimeRange(year, yearCount, calendar) {

  constructor(m: DateTime,
              yearCount: Int,
              calendar: ITimeCalendar = TimeCalendar.DEFAULT) : this(m.year, yearCount, calendar)

  fun yearStream(): FastList<YearRange> = TODO()

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(m: DateTime,
           yearCount: Int,
           calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearRangeCollection {
      return YearRangeCollection(m.year, yearCount, calendar)
    }

    @JvmStatic
    @JvmOverloads
    fun of(year: Int,
           yearCount: Int,
           calendar: ITimeCalendar = TimeCalendar.DEFAULT): YearRangeCollection {
      return YearRangeCollection(year, yearCount, calendar)
    }
  }
}