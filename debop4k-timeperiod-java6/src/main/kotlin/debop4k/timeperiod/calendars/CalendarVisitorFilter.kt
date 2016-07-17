package debop4k.timeperiod.calendars

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.timeperiod.TimePeriodCollection
import debop4k.timeperiod.Weekdays
import debop4k.timeperiod.Weekends
import debop4k.timeperiod.models.DayOfWeek
import org.eclipse.collections.api.set.MutableSet
import org.eclipse.collections.impl.factory.Sets
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList

/**
 * Calendar 탐색 시의 필터링을 수행합니다.
 * Created by debop
 */
open class CalendarVisitorFilter : AbstractValueObject(), ICalendarVisitorFilter {

  private val _excludePeriods = TimePeriodCollection.of()
  private val _years = IntArrayList()
  private val _monthOfYears = IntArrayList()
  private val _dayOfMonths = IntArrayList()
  private val _weekOfDays: MutableSet<DayOfWeek> = Sets.mutable.of<DayOfWeek>()
  private val _hourOfDays = IntArrayList()
  private val _minuteOfHours = IntArrayList()


  override val excludePeriods: TimePeriodCollection
    get() = _excludePeriods

  override val years: IntArrayList
    get() = _years

  override val monthOfYears: IntArrayList get() = _monthOfYears

  override val dayOfMonths: IntArrayList get() = _dayOfMonths

  override val weekOfDays: Set<DayOfWeek> get() = _weekOfDays

  override val hourOfDays: IntArrayList get() = _hourOfDays

  override val minuteOfHours: IntArrayList get() = _minuteOfHours

  fun addYears(vararg years: Int): Unit {
    _years.addAll(*years)
  }

  fun addMonthOfYears(vararg months: Int): Unit {
    _monthOfYears.addAll(*months)
  }

  fun addDayOfMonths(vararg days: Int): Unit {
    _dayOfMonths.addAll(*days)
  }

  fun addHourOfDays(vararg hours: Int): Unit {
    _hourOfDays.addAll(*hours)
  }

  fun addMinuteOfHours(vararg minutes: Int): Unit {
    _minuteOfHours.addAll(*minutes)
  }

  fun addWeekOfDays(vararg dows: DayOfWeek): Unit {
    _weekOfDays.addAll(dows.asList())
  }

  override fun addWorkingWeekdays() {
    addWeekdays(*Weekdays)
  }

  override fun addWorkingWeekends() {
    addWeekOfDays(*Weekends)
  }

  override fun addWeekdays(vararg days: DayOfWeek) {
    _weekOfDays.addAll(days.asList())
  }

  override fun clear() {
    _years.clear()
    _monthOfYears.clear()
    _dayOfMonths.clear()
    _weekOfDays.clear()
    _hourOfDays.clear()
    _minuteOfHours.clear()
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("years", _years)
        .add("monthOfYears", _monthOfYears)
        .add("dayOfMonths", _dayOfMonths)
        .add("hourOfDays", _hourOfDays)
        .add("minuteOfHours", _minuteOfHours)
        .add("weekOfDays", _weekOfDays)
        .add("excludePeriods", _excludePeriods)
  }
}