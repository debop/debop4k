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

/**
 * @author debop sunghyouk.bae@gmail.com
 */
enum class Month {

  JANUARY,
  FEBURARY,
  MARCH,
  APRIL,
  MAY,
  JUNE,
  JULY,
  AUGUST,
  SEPTEMBER,
  OCTOBER,
  NOVEMBER,
  DECEMBER;

  operator fun plus(months: Int): Month {
    val amount = months % 12
    return monthArray[(ordinal + amount + 12) % 12]
  }

  operator fun minus(months: Int): Month = plus(-months)

  val value: Int get() = ordinal + 1

  companion object {
    @JvmStatic val monthArray: Array<Month> = Month.values()

    @JvmStatic fun of(m: Int): Month {
      if (m < 1 || m > 12)
        throw IllegalArgumentException("Invalid month value. 1~12")
      return monthArray[m - 1]
    }
  }
}