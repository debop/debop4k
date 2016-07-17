package debop4k.timeperiod.calendars

import debop4k.core.ToStringHelper

/**
 * Created by debop
 */
class CalendarPeriodCollectorContext(val scope: CollectKind) : ICalendarVisitorContext {

  override fun toString(): String {
    return ToStringHelper(this).add("scope", scope).toString()
  }
}