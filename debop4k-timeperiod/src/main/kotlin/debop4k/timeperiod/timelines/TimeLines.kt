/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package debop4k.timeperiod.timelines

import debop4k.core.shouldBePositive
import debop4k.core.uninitialized
import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.TimePeriodCollection
import debop4k.timeperiod.TimeRange

object TimeLines {

  fun combinePeriods(moments: ITimeLineMomentCollection?): ITimePeriodCollection {

    val periods = TimePeriodCollection()
    if (moments == null || moments.isEmpty) {
      return periods
    }

    var itemIndex = 0

    while (itemIndex < moments.size) {
      val periodStart = moments[itemIndex]
      var balance = periodStart.startCount
      balance.shouldBePositive("balance")

      var periodEnd: ITimeLineMoment? = null

      while (itemIndex < moments.size - 1 && balance > 0) {
        itemIndex++
        periodEnd = moments[itemIndex]
        balance += periodEnd.startCount
        balance -= periodEnd.endCount
      }

      assert(periodEnd != null)

      if (periodEnd!!.startCount == 0L && itemIndex < moments.size) {
        val period = TimeRange(periodStart.moment, periodEnd.moment)
        periods += period
      }
      itemIndex++
    }
    return periods
  }

  fun intersectPeriods(moments: ITimeLineMomentCollection?): ITimePeriodCollection {
    val periods = TimePeriodCollection()
    if (moments == null || moments.isEmpty) {
      return periods
    }

    var intersectionStart = -1
    var balance = 0L
    var i = 0
    while (i < moments.size) {
      val moment = moments[i]
      val startCount = moment.startCount
      val endCount = moment.endCount

      balance += startCount
      balance -= endCount

      if (startCount > 0 && balance > 1 && intersectionStart < 0) {
        intersectionStart = i
      } else if (endCount > 0 && balance <= 1 && intersectionStart >= 0) {
        val period = TimeRange(moments[intersectionStart].moment, moment.moment)
        periods += period
        intersectionStart = -1
      }
      i++
    }

    return periods
  }

  fun calculateGap(moments: ITimeLineMomentCollection?,
                   range: ITimePeriod): ITimePeriodCollection {
    val gaps = TimePeriodCollection()
    if (moments == null || moments.isEmpty) {
      return gaps
    }

    val periodStart = moments.min()

    // find leading gap
    if (periodStart != null && range.start < periodStart.moment) {
      val startingGap = TimeRange(range.start, periodStart.moment)
      gaps += startingGap
    }

    // find intermediated gap
    var itemIndex = 0
    while (itemIndex < moments.size) {
      val moment = moments[itemIndex]
      assert(moment != null)
      assert(moment.startCount > 0) { "moment.startCount 값이 0보다 커야합니다. startCount=${moment.startCount}" }

      var balance = moment.startCount
      var gapStart: ITimeLineMoment? = uninitialized<ITimeLineMoment>()

      while (itemIndex < moments.size - 1 && balance > 0) {
        itemIndex++
        gapStart = moments[itemIndex]

        balance += gapStart.startCount
        balance -= gapStart.endCount
      }

      assert(gapStart != null)

      if (gapStart!!.startCount <= 0) {
        // found a gap
        if (itemIndex < moments.size - 1) {
          gaps += TimeRange(gapStart.moment, moments.get(itemIndex + 1).moment)
        }
      }
      itemIndex++
    }

    // find ending gap
    val periodEnd = moments.max()
    if (periodEnd != null && range.end > periodEnd.moment) {
      gaps += TimeRange(periodEnd.moment, range.end)
    }

    return gaps
  }
}