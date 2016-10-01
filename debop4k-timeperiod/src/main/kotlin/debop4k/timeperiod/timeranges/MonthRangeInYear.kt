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

package debop4k.timeperiod.timeranges

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper

/**
 * 1년 내의 Month 단위의 기간을 표현합니다.
 * Created by debop
 */
open class MonthRangeInYear @JvmOverloads constructor(val startMonthOfYear: Int = 1,
                                                      val endMonthOfYear: Int = 12) : AbstractValueObject(), Comparable<MonthRangeInYear> {

  init {
    require(startMonthOfYear >= 1)
    require(endMonthOfYear <= 12)
    require(startMonthOfYear <= endMonthOfYear)
  }

  val isSingleMonth: Boolean
    get() = startMonthOfYear == endMonthOfYear

  fun hasInside(monthOfYear: Int): Boolean {
    return startMonthOfYear <= monthOfYear && monthOfYear <= endMonthOfYear
  }

  override fun compareTo(other: MonthRangeInYear): Int {
    return hashCode() - other.hashCode()
  }

  override fun hashCode(): Int {
    return startMonthOfYear * 1000 + endMonthOfYear
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("startMonthOfYear", startMonthOfYear)
        .add("endMonthOfYear", endMonthOfYear)
  }
}