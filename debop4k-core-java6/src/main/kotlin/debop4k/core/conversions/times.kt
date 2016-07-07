package debop4k.core.conversions

import org.joda.time.Duration

fun Long.standardMillis(): Duration = Duration.millis(this)
fun Long.standardSeconds(): Duration = Duration.standardSeconds(this)
fun Long.standardMinutes(): Duration = Duration.standardMinutes(this)
fun Long.standardHours(): Duration = Duration.standardHours(this)
fun Long.standardDays(): Duration = Duration.standardDays(this)