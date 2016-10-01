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
 * 위경도 정보 (Geographical Location)
 *
 * @author sunghyouk.bae@gmail.com
 */
data class GeoLocation @JvmOverloads constructor(val latitude: Double = 0.0,
                                                 val longitude: Double = 0.0) : Comparable<GeoLocation>, Serializable {

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(latitude: Double = 0.0, longitude: Double = 0.0): GeoLocation
        = GeoLocation(latitude, longitude)

    @JvmStatic
    fun of(src: GeoLocation): GeoLocation = of(src.latitude, src.longitude)
  }

  override fun hashCode(): Int = hashOf(latitude, longitude)

  override fun compareTo(other: GeoLocation): Int {
    var diff = latitude.compareTo(other.latitude)
    if (diff == 0)
      diff = longitude.compareTo(other.longitude)
    return diff
  }

}