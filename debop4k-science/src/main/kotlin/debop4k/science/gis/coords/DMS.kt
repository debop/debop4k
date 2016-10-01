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

import debop4k.core.utils.hashOf
import java.io.Serializable

/**
 * DMS
 * @author sunghyouk.bae@gmail.com
 */
data class DMS
@JvmOverloads constructor(val d: Int = 0,
                          val m: Int = 0,
                          val s: Double = 0.0) : Comparable<DMS>, Serializable {

  override fun compareTo(other: DMS): Int {
    var diff = d.compareTo(other.d)
    if (diff == 0)
      diff = m.compareTo(other.m)
    if (diff == 0)
      diff = s.compareTo(other.s)
    return diff
  }

  override fun hashCode(): Int = hashOf(d, m, s)

  companion object {
    @JvmStatic
    fun of(degree: Double): DMS = degree.toDMS()

    @JvmStatic
    @JvmOverloads
    fun of(d: Int = 0, m: Int = 0, s: Double = 0.0): DMS = DMS(d, m, s)

  }
}