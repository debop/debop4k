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

package debop4k.timeperiod.models

import debop4k.timeperiod.*

/**
 * @author debop sunghyouk.bae@gmail.com
 */
enum class Quarter(val value: Int) {

  /** 1/4 분기  */
  FIRST(1),

  /** 2/4 분기  */
  SECOND(2),

  /** 3/4 분기  */
  THIRD(3),

  /** 4/4 분기  */
  FOURTH(4);

  operator fun plus(delta: Int): Quarter {
    val amount = delta % QuartersPerYear
    return quarterArray[(ordinal + (amount + 4)) % 4]
  }

  operator fun minus(delta: Int): Quarter = plus(-(delta % 12))

  val monthsOfQuarter: IntArray get() = when (this) {
    FIRST -> FirstQuarterMonths
    SECOND -> SecondQuarterMonths
    THIRD -> ThirdQuarterMonths
    FOURTH -> FourthQuarterMonths
  }

  val startMonth: Int get() = this.ordinal * MonthsPerQuarter + 1

  val endMonth: Int get() = this.value * MonthsPerQuarter

  companion object {

    val quarterArray: Array<Quarter> = Quarter.values()

    @JvmStatic
    fun of(q: Int): Quarter {
      if (q < 1 || q > 4)
        throw IllegalArgumentException("Invalid q for Quarter. 1..4")
      return quarterArray[q - 1]
    }

    @JvmStatic
    fun ofMonth(monthOfYear: Int): Quarter {
      return quarterArray[(monthOfYear - 1) / MonthsPerQuarter]
    }

  }

}