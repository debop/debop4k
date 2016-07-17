package debop4k.timeperiod.timelines

import debop4k.timeperiod.ITimePeriodCollection
import org.joda.time.DateTime

/**
 * Created by debop
 */
interface ITimeLineMoment : Comparable<ITimeLineMoment> {

  val moment: DateTime

  val periods: ITimePeriodCollection

  val startCount: Long

  val endCount: Long
}