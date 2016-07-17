package debop4k.timeperiod.timeranges

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf

/**
 * Created by debop
 */
open class HourRangeInDay(val startHourOfDay: Int = 0,
                          val endHourOfDay: Int = 23) :
    AbstractValueObject(), Comparable<HourRangeInDay> {

  constructor(hourOfDay: Int) : this(hourOfDay, hourOfDay)

  override fun compareTo(other: HourRangeInDay): Int {
    return startHourOfDay.compareTo(other.startHourOfDay)
  }

  override fun hashCode(): Int {
    return hashOf(startHourOfDay, endHourOfDay)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("startHourOfDay", startHourOfDay)
        .add("endHourOfDay", endHourOfDay)
  }
}