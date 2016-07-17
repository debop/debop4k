package debop4k.timeperiod.timelines

import debop4k.core.AbstractValueObject
import debop4k.timeperiod.*
import org.joda.time.DateTime

/**
 * Created by debop
 */
open class TimeLine<T : ITimePeriod>(override val periods: ITimePeriodContainer,
                                     override val limits: ITimePeriod? = TimeRange.of(periods),
                                     override val mapper: ITimePeriodMapper? = null) :
    AbstractValueObject(), ITimeLine {

  override fun combinePeriods(): ITimePeriodCollection {
    if (periods.isEmpty())
      return TimePeriodCollection()

    val moments = timeLineMoments()
    if (moments == null || moments.isEmpty) {
      return TimePeriodCollection.of(TimeRange.of(periods))
    }
    return combinePeriods(moments)
  }

  override fun intersectPeriods(): ITimePeriodCollection {
    if (periods.isEmpty()) {
      return TimePeriodCollection()
    }

    val moments = timeLineMoments()
    if (moments == null || moments.isEmpty) {
      return TimePeriodCollection.of(TimeRange.of(periods))
    }
    return intersectPeriods(moments)
  }

  override fun calculateCaps(): ITimePeriodCollection {
    TODO()
  }

  private fun timeLineMoments(): ITimeLineMomentCollection? {
    return timeLineMoments(periods)
  }

  private fun timeLineMoments(periods: Collection<ITimePeriod>): ITimeLineMomentCollection? {
    TODO()
  }

  private fun mapPeriodStart(moment: DateTime): DateTime {
    return mapper?.unmapStart(moment) ?: moment
  }

  private fun mapPeriodEnd(moment: DateTime): DateTime {
    return mapper?.unmapEnd(moment) ?: moment
  }
}