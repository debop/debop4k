package debop4k.timeperiod.timeranges

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.kodatimes.trimToDay
import debop4k.core.utils.hashOf
import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.MaxDaysPerMonth

/**
 * Created by debop
 */
open class DayRangeInMonth(val startDayOfMonth: Int = 1,
                           val endDayOfMonth: Int = MaxDaysPerMonth) :
    AbstractValueObject(), Comparable<DayRangeInMonth> {

  init {
    require(startDayOfMonth >= 1)
    require(endDayOfMonth <= MaxDaysPerMonth)
    require(startDayOfMonth <= endDayOfMonth)
  }

  companion object {
    fun of(startDay: Int = 1, endDay: Int = MaxDaysPerMonth): DayRangeInMonth {
      require(startDay >= 1)
      require(endDay <= MaxDaysPerMonth)
      require(startDay <= endDay)

      return DayRangeInMonth(startDay, endDay)
    }

    fun of(period: ITimePeriod): DayRangeInMonth {
      require(period.hasPeriod())
      require(period.start.trimToDay() == period.end.trimToDay())
      return of(period.start.dayOfMonth, period.end.dayOfMonth)
    }
  }

  val isSingleDay: Boolean
    get() = startDayOfMonth == endDayOfMonth

  fun hasInside(dayOfMonth: Int): Boolean {
    return startDayOfMonth <= dayOfMonth && dayOfMonth <= endDayOfMonth
  }

  fun assertValidDayRange(dayOfMonth: Int): Unit {
    assert(dayOfMonth > 0 && dayOfMonth <= MaxDaysPerMonth,
           { "dayOfMonth[$dayOfMonth]는 1 ~ $MaxDaysPerMonth 사이 값이어야 합니다." })
  }

  override fun compareTo(other: DayRangeInMonth): Int {
    return startDayOfMonth - other.startDayOfMonth
  }

  override fun hashCode(): Int {
    return hashOf(startDayOfMonth, endDayOfMonth)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("startDayOfMonth", startDayOfMonth)
        .add("endDayOfMonth", endDayOfMonth)
  }
}