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
 * UTM Zone 을 표현합니다.
 * <p>
 * 참고: http://wiki.kesti.co.kr/pages/viewpage.action?pageId=3703027
 * 참고: https://ko.wikipedia.org/wiki/UTM_%EC%A2%8C%ED%91%9C%EA%B3%84
 *
 * 경도 기준의 Zone (6도 간격) (한국은 51, 52 에 걸쳐 있다)
 * <p>
 * UTM 51: 120~126
 * UTM 52: 126~132
 * UTM 53: 132~138
 *
 * 위도 기준의 Zone (8도 간격) (한국은 S, T 에 걸쳐 있다)
 * R: 24~32
 * S: 32~40
 * T: 40~48
 *
 * @author sunghyouk.bae @gmail.com
 * @see org.jscience.geography.coordinates.UTM
 * @see ucar.nc2.dataset.transform.UTM
 */
data class UtmZone @JvmOverloads constructor(val longitudeZone: Int = 52,
                                             val latitudeZone: Char = 'S') : Comparable<UtmZone>, Serializable {

  init {
    require(longitudeZone.coerceIn(UTM_LONGITUDE_MIN, UTM_LONGITUDE_MAX) == longitudeZone)
    require(latitudeZone.isUtmLatitude)
  }

  override fun compareTo(other: UtmZone): Int {
    var diff = latitudeZone.compareTo(other.latitudeZone)
    if (diff == 0)
      diff = longitudeZone.compareTo(other.longitudeZone)
    return diff
  }

  override fun hashCode(): Int = hashOf(longitudeZone, latitudeZone)

  //override fun toString(): String = "$longitudeZone,$latitudeZone"
}