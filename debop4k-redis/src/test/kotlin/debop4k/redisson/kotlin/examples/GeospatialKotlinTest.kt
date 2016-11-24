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
 */

package debop4k.redisson.kotlin.examples

import debop4k.redisson.kotlin.AbstractRedissonKotlinTest
import org.junit.Test
import org.redisson.api.GeoEntry
import org.redisson.api.GeoUnit


class GeospatialKotlinTest : AbstractRedissonKotlinTest() {

  @Test fun geoAdd() {
    val geo = redisson.getGeo<String>("test")

    // 두 지점을 저장
    geo.add(GeoEntry(13.361389, 38.115556, "Palermo"), GeoEntry(15.087269, 37.502669, "Catania"))
    // 한 지점을 비동기로 저장
    geo.addAsync(37.618423, 55.751244, "Moscow")

    // 두 지점의 거리를 계산
    val distance = geo.dist("Palermo", "Catania", GeoUnit.KILOMETERS)
    println("Palermo - Catania distance: $distance")

    // 지정한 지점들의 GeoHash 문자열(11자리)을 나타낸다
    val map = geo.hashAsync("Palermo", "Catania")
    println("geo hash: ${map.get()}")

    // 지점들의 위치를 얻습니다
    val positions = geo.pos("test2", "Palermo", "test3", "Catania")
    println("positions = $positions")

    // 원 형태의 영역에 속한 모든 등록 지점들을 조회합니다
    val cities = geo.radius(15.0, 37.0, 200.0, GeoUnit.KILOMETERS)
    println("cities = $cities")

    // 원 형태의 영역에 속한 모든 등록 지점과 지점의 경위도 정보를 가져옵니다.
    val cityWithPositions = geo.radiusWithPosition(15.0, 37.0, 200.0, GeoUnit.KILOMETERS)
    println("city with positions = $cityWithPositions")
  }
}