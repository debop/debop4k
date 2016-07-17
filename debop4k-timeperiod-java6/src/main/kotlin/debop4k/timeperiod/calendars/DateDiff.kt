package debop4k.timeperiod.calendars

import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.now
import debop4k.core.utils.hashOf
import debop4k.timeperiod.DefaultTimeCalendar
import debop4k.timeperiod.ITimeCalendar
import debop4k.timeperiod.MonthsPerYear
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * Created by debop
 */
open class DateDiff(val start: DateTime,
                    val end: DateTime = now(),
                    val calendar: ITimeCalendar = DefaultTimeCalendar) {

  val difference: Duration = Duration(start, end)
  val isEmpty: Boolean = (difference == Duration.ZERO)

  val startYear: Int get() = calendar.year(start)
  val endYear: Int get() = calendar.year(end)
  val startMonthOfYear: Int get() = calendar.monthOfYear(start)
  val endMonthOfYear: Int get() = calendar.monthOfYear(end)

  private val _cache: ConcurrentHashMap<String, Long> = ConcurrentHashMap.newMap()

  val years: Long get() = _cache.getIfAbsentPut("years", calcYears())
  val quarters: Long get() = _cache.getIfAbsentPut("quarters", calcQuarters())
  val months: Long get() = _cache.getIfAbsentPut("months", calcMonths())
  val weeks: Long get() = _cache.getIfAbsentPut("weeks", calcWeeks())
  val days: Long get() = difference.standardDays
  val hours: Long get() = difference.standardHours
  val minutes: Long get() = difference.standardMinutes
  val seconds: Long get() = difference.standardSeconds

  val elapsedYears: Long get() = years
  val elapsedQuarters: Long get() = quarters
  val elapsedMonths: Long get() = months - elapsedYears * MonthsPerYear
  val elapsedStartDays: DateTime
    get() = start
        .plusYears(elapsedYears.toInt())
        .plusMonths(elapsedMonths.toInt())
  val elapsedDays: Long get() = Duration(elapsedStartDays, end).standardDays

  val elapsedStartHours: DateTime
    get() = elapsedStartDays.plusDays(elapsedDays.toInt())

  val elapsedHours: Long get() = Duration(elapsedStartHours, end).standardHours

  val elapsedStartMinutes: DateTime
    get() = elapsedStartHours.plusHours(elapsedHours.toInt())

  val elapsedMinutes: Long get() = Duration(elapsedStartMinutes, end).standardMinutes

  val elapsedStartSeconds: DateTime
    get() = elapsedStartMinutes.plusMinutes(elapsedMinutes.toInt())

  val elapsedSeconds: Long get() = Duration(elapsedStartSeconds, end).standardSeconds

  private fun calcYears(): Long {
    TODO()
  }

  private fun calcQuarters(): Long {
    TODO()
  }

  private fun calcMonths(): Long {
    TODO()
  }

  private fun calcWeeks(): Long {
    TODO()
  }

  private fun roundEx(n: Double): Double {
    val rounded = if (n > 0.0) Math.round(n) else -Math.round(-n)
    return rounded.toDouble()
  }

  override fun hashCode(): Int {
    return hashOf(start, end, calendar)
  }

  override fun toString(): String {
    return ToStringHelper(this)
        .add("start", start)
        .add("end", end)
        .add("difference", difference)
        .add("calendar", calendar)
        .toString()
  }
}