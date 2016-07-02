@file:JvmName("timeperiods")

package debop4k.timeperiod

import org.joda.time.DateTime
import org.joda.time.Duration


/**
 * 기간을 나타냅니다.
 * @author Sunghyouk Bae <sunghyouk.bae@gmail.com>
 */
interface ITimePeriod : Comparable<ITimePeriod> {

  val start: DateTime

  val end: DateTime

  fun duration(): Duration = Duration(start, end)

  val readOnly: Boolean

}

/**
 * Time Period
 */
class TimePeriod(override val start: DateTime,
                 override val end: DateTime,
                 override val readOnly: Boolean = false) : ITimePeriod {

  override fun compareTo(other: ITimePeriod): Int
      = start.compareTo(other.start)

  operator fun rangeTo(endIncluded: ITimePeriod): ClosedRange<ITimePeriod> = TODO()

  companion object {

    fun of(moment: DateTime, readOnly: Boolean = false): TimePeriod
        = TimePeriod(moment, moment, readOnly)

    fun of(start: DateTime, duration: Duration, readOnly: Boolean = false): TimePeriod
        = TimePeriod(start, start.plus(duration), readOnly)
  }
}
