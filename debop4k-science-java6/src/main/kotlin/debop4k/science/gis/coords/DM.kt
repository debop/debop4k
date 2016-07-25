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

package debop4k.science.gis.coords

import debop4k.core.asDouble
import debop4k.core.asInt

/**
 * 도 (Degree) 와 분(Decimal Minutes) 으로 좌표를 표현하는 방식을 표현하는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @see DMS
 */
data class DM(val d: Int = 0,
              val m: Double = 0.0) : Comparable<DM> {

  fun toDegree(): Double = d + m / 60.0

  override fun compareTo(other: DM): Int {
    var diff = d.compareTo(other.d)
    if (diff == 0)
      diff = m.compareTo(other.m)
    return diff
  }

  companion object {

    @JvmStatic
    fun of(degree: Double): DM {
      val d = degree.toInt()
      val m = (degree - d) * 60

      return DM(d, m)
    }

    @JvmStatic
    fun parse(str: String): DM {
      val items = str.split(' ', limit = 2)

      var d = 0
      var m = 0.0
      if (items.size > 0) d = items[0].asInt()
      if (items.size > 1) m = items[1].asDouble()

      return DM(d, m)
    }
  }
}