package debop4k.timeperiod.timelines

import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.ITimePeriodContainer
import debop4k.timeperiod.ITimePeriodMapper

/**
 * Created by debop
 */
open class TimeGapCalculator<T : ITimePeriod>(val mapper: ITimePeriodMapper? = null) {

  @JvmOverloads
  fun gaps(excludePeriods: ITimePeriodContainer, limits: ITimePeriod? = null): ITimePeriodCollection {
    val timeline = TimeLine<T>(excludePeriods, limits, mapper)
    return timeline.calculateCaps()
  }
}