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

  @Test
      //  @Ignore("Redis 3.2 부터 지원, Ubuntu 에서는 Redis 3.0.7이 설치되어 있다.")
  fun geoAdd() {
    val geo = redisson.getGeo<String>("test")
    geo.add(GeoEntry(13.361389, 38.115556, "Palermo"),
            GeoEntry(15.087269, 37.502669, "Catania"))
    geo.addAsync(37.618423, 55.751244, "Moscow")

    val distance = geo.dist("Palermo", "Catania", GeoUnit.KILOMETERS)
    println("Palermo - Catania distance: $distance")

    val map = geo.hashAsync("Palermo", "Catania")
    println("hash: ${map.get()}")

    val positions = geo.pos("test2", "Palermo", "test3", "Catania")
    println("positions = $positions")

    val cities = geo.radius(15.0, 37.0, 200.0, GeoUnit.KILOMETERS)
    println("cities = $cities")

    val cityWithPositions = geo.radiusWithPosition(15.0, 37.0, 200.0, GeoUnit.KILOMETERS)
    println("city with positions = $cityWithPositions")
  }
}