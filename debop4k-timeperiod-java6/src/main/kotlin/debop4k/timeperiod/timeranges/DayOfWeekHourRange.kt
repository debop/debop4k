package debop4k.timeperiod.timeranges

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.models.DayOfWeek

/**
 * 특정 요일의 하루 동안의 시간 간격
 * Created by debop
 */
class DayOfWeekHourRange(val dayOfWeek: DayOfWeek,
                         startHourOfDay: Int = 0,
                         endHourOfDay: Int = 23) : HourRangeInDay(startHourOfDay, endHourOfDay) {

  override fun hashCode(): Int {
    return hashOf(dayOfWeek, startHourOfDay, endHourOfDay)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("dayOfWeek", dayOfWeek)
  }
}