package debop4k.timeperiod.calendars

import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.TimePeriodCollection
import org.joda.time.DateTime
import org.joda.time.Duration
import org.slf4j.LoggerFactory

/**
 * Created by debop
 */
open class DateAdd {

  private val log = LoggerFactory.getLogger(javaClass)

  open val includePeriods: TimePeriodCollection = TimePeriodCollection()
  open val excludePeriods: TimePeriodCollection = TimePeriodCollection()

  @JvmOverloads
  open fun add(start: DateTime,
               offset: Duration,
               seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): DateTime {
    TODO()
  }

  @JvmOverloads
  open fun substract(start: DateTime,
                     offset: Duration,
                     seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): DateTime {
    TODO()
  }

  @JvmOverloads
  open protected fun calculateEnd(start: DateTime,
                                  offset: Duration,
                                  seekDir: SeekDirection,
                                  seekBoundary: SeekBoundaryMode = SeekBoundaryMode.Next): Pair<DateTime, Duration> {
    TODO()
  }

  private fun getAvailablePeriods(searchPeriods: ITimePeriodCollection): ITimePeriodCollection {
    TODO()
  }

  private fun findPeriodForward(availablePeriods: ITimePeriodCollection,
                                remaining: Duration,
                                startPeriod: ITimePeriod,
                                seekMoment: DateTime,
                                seekBoundary: SeekBoundaryMode): Pair<DateTime, Duration> {
    TODO()
  }

  private fun findPeriodBackward(availablePeriods: ITimePeriodCollection,
                                 remaining: Duration,
                                 startPeriod: ITimePeriod,
                                 seekMoment: DateTime,
                                 seekBoundary: SeekBoundaryMode): Pair<DateTime, Duration> {
    TODO()
  }

  private fun findNextPeriod(start: DateTime,
                             periods: Collection<ITimePeriod>): Pair<ITimePeriod, DateTime> {
    TODO()
  }

  private fun findPrevPeriod(start: DateTime,
                             periods: Collection<ITimePeriod>): Pair<ITimePeriod, DateTime> {
    TODO()
  }
}