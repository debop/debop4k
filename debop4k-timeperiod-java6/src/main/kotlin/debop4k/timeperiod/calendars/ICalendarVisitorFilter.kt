package debop4k.timeperiod.calendars

import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.models.DayOfWeek
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList
import java.io.Serializable

/**
 * Created by debop
 */
interface ICalendarVisitorFilter : Serializable {

  val excludePeriods: ITimePeriodCollection

  val years: IntArrayList

  val monthOfYears: IntArrayList

  val dayOfMonths: IntArrayList

  val weekOfDays: Set<DayOfWeek>

  val hourOfDays: IntArrayList

  val minuteOfHours: IntArrayList

  fun addWorkingWeekdays(): Unit

  fun addWorkingWeekends(): Unit

  fun addWeekdays(vararg days: DayOfWeek)

  fun clear(): Unit
}