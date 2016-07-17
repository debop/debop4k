package debop4k.timeperiod.timelines

import debop4k.timeperiod.ITimePeriod
import org.joda.time.DateTime
import java.io.Serializable

/**
 * Created by debop
 */
interface ITimeLineMomentCollection : Iterable<ITimeLineMoment>, Serializable {

  val size: Int

  val isEmpty: Boolean

  fun min(): ITimeLineMoment

  fun max(): ITimeLineMoment

  fun get(index: Int): ITimeLineMoment

  fun add(period: ITimePeriod?): Unit

  fun addAll(periods: Collection<ITimePeriod?>): Unit

  fun remove(period: ITimePeriod?): Unit

  fun find(moment: DateTime): ITimeLineMoment?

  fun contains(moment: DateTime): Boolean

}