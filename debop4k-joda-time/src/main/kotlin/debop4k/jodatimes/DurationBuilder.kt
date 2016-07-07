package debop4k.jodatimes

import org.joda.time.*

/**
 * @author sunghyouk.bae@gmail.com
 */
class DurationBuilder(val underlying: Period) {

  operator fun minus(that: DurationBuilder): DurationBuilder = DurationBuilder(this.underlying.minus(that.underlying))
  operator fun plus(that: DurationBuilder): DurationBuilder = DurationBuilder(this.underlying.plus(that.underlying))

  fun ago(): DateTime = DateTime.now().minus(underlying)
  fun later(): DateTime = DateTime.now().plus(underlying)
  fun from(moment: DateTime): DateTime = moment.plus(underlying)
  fun before(moment: DateTime): DateTime = moment.minus(underlying)

  fun standardDuration(): Duration = underlying.toStandardDuration()

  fun toDuration(): Duration = underlying.toStandardDuration()
  fun toPeriod(): Period = underlying

  operator fun minus(period: ReadablePeriod): Period = underlying.minus(period)
  operator fun plus(period: ReadablePeriod): Period = underlying.plus(period)

  fun millis(): Long = standardDuration().millis
  fun seconds(): Long = standardDuration().standardSeconds

  operator fun minus(amount: Long): Duration = standardDuration().minus(amount)
  operator fun minus(amount: ReadableDuration): Duration = standardDuration().minus(amount)

  operator fun plus(amount: Long): Duration = standardDuration().plus(amount)
  operator fun plus(amount: ReadableDuration): Duration = standardDuration().plus(amount)
}