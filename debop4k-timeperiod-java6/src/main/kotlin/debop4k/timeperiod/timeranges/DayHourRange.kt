package debop4k.timeperiod.timeranges

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.timeperiod.models.DayOfWeek

/**
 * 특정 요일의 시간 범위를 나타냅니다.
 * Created by debop
 */
open class DayHourRange(val dayOfWeek: DayOfWeek,
                        startHourOfDay: Int = 0,
                        endHourOfDay: Int = 23) : HourRangeInDay(startHourOfDay, endHourOfDay) {

  override fun hashCode(): Int {
    return hashOf(super.hashCode(), dayOfWeek)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("dayOfWeek", dayOfWeek)
  }
}