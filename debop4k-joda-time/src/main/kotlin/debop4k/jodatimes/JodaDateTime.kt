package debop4k.jodatimes

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter

interface JodaDateTime {

  fun now(): DateTime = DateTime.now()
  fun tomorrow(): DateTime = nextDay()
  fun yesterday(): DateTime = lastDay()

  fun nextSecond(): DateTime = now().plusSeconds(1)
  fun nextMinute(): DateTime = now().plusMinutes(1)
  fun nextHour(): DateTime = now().plusHours(1)
  fun nextDay(): DateTime = now().plusDays(1)
  fun nextWeek(): DateTime = now().plusWeeks(1)
  fun nextMonth(): DateTime = now().plusMonths(1)
  fun nextYear(): DateTime = now().plusYears(1)

  fun lastSecond(): DateTime = now().minusSeconds(1)
  fun lastMinute(): DateTime = now().minusMinutes(1)
  fun lastHour(): DateTime = now().minusHours(1)
  fun lastDay(): DateTime = now().minusDays(1)
  fun lastWeek(): DateTime = now().minusWeeks(1)
  fun lastMonth(): DateTime = now().minusMonths(1)
  fun lastYear(): DateTime = now().minusYears(1)

  fun parse(s: String, fmt: DateTimeFormatter): DateTime = DateTime.parse(s, fmt)

  fun fromJsonString(json: String): DateTime = DateTime(json)
}
