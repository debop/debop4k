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

package debop4k.science.gis.coords

import debop4k.core.asDouble
import debop4k.core.asInt

/**
 * 경위도를 도/분/초 형식으로 나타내는 클래스
 *
 * @author sunghyouk.bae@gmail.com
 * @see DM
 * @since 2015. 12. 12.
 */
data class DMS(
    /** Degree */
    val d: Int = 0,
    /** Minute ( 1 minute = 1/60 degree) */
    val m: Int = 0,
    /** Second ( 1 second = 1/60 minute = 1/3600 degree ) */
    val s: Double = 0.0) : Comparable<DMS> {

  fun toDegree(): Double {
    return d + m / 60.0 + s / 3600.0
  }

  override fun compareTo(other: DMS): Int {
    return toDegree().compareTo(other.toDegree())
  }

  companion object {

    @JvmStatic
    fun of(degree: Double): DMS {
      val d: Int = degree.toInt()
      val m: Int = ((degree - d) * 60).toInt()
      val s: Double = ((degree - d) * 60 - m) * 60.0

      return DMS(d, m, s)
    }

    @JvmStatic
    fun of(d: Int, m: Double): DMS {
      val mm = m.toInt()
      val s = (m - mm) * 60
      return DMS(d, mm, s)
    }

    @JvmStatic
    fun parse(str: String): DMS {
      val items = str.split(' ', limit = 3)

      var d = 0
      var m = 0
      var s = 0.0

      if (items.size > 0) d = items[0].asInt()
      if (items.size > 1) m = items[1].asInt()
      if (items.size > 2) s = items[2].asDouble()

      return DMS(d, m, s)
    }
  }
}