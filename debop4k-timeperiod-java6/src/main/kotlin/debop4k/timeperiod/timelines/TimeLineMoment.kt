package debop4k.timeperiod.timelines

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.HashEx
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.TimePeriodCollection
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class TimeLineMoment(override val moment: DateTime) :
    AbstractValueObject(), ITimeLineMoment {

  private val _periods: TimePeriodCollection = TimePeriodCollection()

  override val periods: ITimePeriodCollection
    get() = _periods

  override val startCount: Long
    get() = _periods.periods.count { it.start == moment }.toLong()

  override val endCount: Long
    get() = _periods.periods.count { it.end == moment }.toLong()

  override fun compareTo(other: ITimeLineMoment): Int {
    return moment.compareTo(other.moment)
  }

  override fun hashCode(): Int {
    return HashEx.compute(moment)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("moment", moment)
        .add("startCount", startCount)
        .add("endCount", endCount)
        .add("periods", periods)
  }
}