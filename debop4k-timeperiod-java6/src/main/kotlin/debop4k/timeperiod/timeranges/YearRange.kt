package debop4k.timeperiod.timeranges

import debop4k.core.kodatimes.now
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class YearRange(year: Int,
                     calendar: ITimeCalendar = DefaultTimeCalendar) : YearTimeRange(year, 1, calendar) {
  constructor(m: DateTime, calendar: ITimeCalendar = DefaultTimeCalendar) : this(m.year, calendar)

  val nextYear: YearRange get() = addYears(1)
  val prevYear: YearRange get() = addYears(-1)

  fun addYears(years: Int): YearRange {
    return YearRange(year + years, calendar)
  }

  companion object {
    @JvmOverloads
    @JvmStatic
    fun of(calendar: ITimeCalendar = DefaultTimeCalendar): YearRange {
      return YearRange(now(), calendar)
    }

    @JvmOverloads
    @JvmStatic
    fun of(year: Int, calendar: ITimeCalendar = DefaultTimeCalendar): YearRange {
      return YearRange(year, calendar)
    }
  }
}