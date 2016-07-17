package debop4k.timeperiod.calendars

import debop4k.timeperiod.*
import debop4k.timeperiod.models.DayOfWeek
import debop4k.timeperiod.timeranges.DayOfWeekHourRange
import debop4k.timeperiod.timeranges.HourRangeInDay
import debop4k.timeperiod.timeranges.WeekRange
import org.eclipse.collections.impl.list.mutable.FastList
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

/**
 * Created by debop
 */
open class CalendarDateAdd : DateAdd() {

  private val log = LoggerFactory.getLogger(javaClass)

  val calendar = TimeCalendar.EMPTY_OFFSET
  val weekDays = FastList.newList<DayOfWeek>()
  val workingHours = FastList.newList<HourRangeInDay>()
  val workingDaysHours = FastList.newList<DayOfWeekHourRange>()

  override val includePeriods: TimePeriodCollection
    get() = throw UnsupportedOperationException("IncludePeriods는 지원하지 않습니다.")

  fun addWorkingWeekdays(): Unit {
    addWeekdays(*Weekdays)
  }

  fun addWeekendWeekdays(): Unit {
    addWeekdays(*Weekends)
  }

  fun addWeekdays(vararg dayOfWeeks: DayOfWeek) {
    weekDays.addAll(dayOfWeeks.asList())
  }

  override fun add(start: DateTime, offset: Duration, seekBoundary: SeekBoundaryMode): DateTime {
    TODO()
  }

  override fun substract(start: DateTime, offset: Duration, seekBoundary: SeekBoundaryMode): DateTime {
    TODO()
  }

  override fun calculateEnd(start: DateTime, offset: Duration, seekDir: SeekDirection, seekBoundary: SeekBoundaryMode): Pair<DateTime, Duration> {
    TODO()
  }

  private fun findNextWeek(current: WeekRange): WeekRange {
    TODO()
  }

  private fun findPrevWeek(current: WeekRange): WeekRange {
    TODO()
  }

  private fun getAvailableWeekPeriods(): List<ITimePeriod> {
    TODO()
  }

}