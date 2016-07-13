/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.timeperiod.models

import com.sun.javaws.exceptions.InvalidArgumentException

/**
 * @author debop sunghyouk.bae@gmail.com
 */
enum class Halfyear(val value: Int) {

  /** 전반기  */
  First(1),

  /** 후반기  */
  Second(2);

  fun plus(halfyear: Int): Halfyear {
    val amount = halfyear % 2
    return values()[(ordinal + amount + 2) % 2]
  }

  fun minus(halfyear: Int): Halfyear {
    return plus(-(halfyear % 2))
  }

  companion object {
    /**
     *
     */
    fun of(halfyear: Int): Halfyear {
      if (halfyear < 1 || halfyear > 2)
        throw InvalidArgumentException(arrayOf("Invalid value for Halfyear: $halfyear"))
      return values()[halfyear - 1]
    }
  }
}