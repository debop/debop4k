package debop4k.jodatimes

import org.joda.time.Period


fun Int.millis(): DurationBuilder = DurationBuilder(Period.millis(this))
fun Int.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this))
fun Int.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this))
fun Int.hours(): DurationBuilder = DurationBuilder(Period.hours(this))
fun Int.days(): Period = Period.days(this)
fun Int.weeks(): Period = Period.weeks(this)
fun Int.months(): Period = Period.months(this)
fun Int.years(): Period = Period.years(this)


fun Long.millis(): DurationBuilder = DurationBuilder(Period.millis(this.toInt()))
fun Long.seconds(): DurationBuilder = DurationBuilder(Period.seconds(this.toInt()))
fun Long.minutes(): DurationBuilder = DurationBuilder(Period.minutes(this.toInt()))
fun Long.hours(): DurationBuilder = DurationBuilder(Period.hours(this.toInt()))
fun Long.days(): Period = Period.days(this.toInt())
fun Long.weeks(): Period = Period.weeks(this.toInt())
fun Long.months(): Period = Period.months(this.toInt())
fun Long.years(): Period = Period.years(this.toInt())