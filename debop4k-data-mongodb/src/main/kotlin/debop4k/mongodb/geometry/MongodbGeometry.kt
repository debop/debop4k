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

package debop4k.mongodb.geometry

import debop4k.science.gis.coords.BoundingBox
import debop4k.science.gis.coords.GeoLocation
import lombok.NonNull
import org.springframework.data.geo.Box
import org.springframework.data.geo.Point

fun Point.toGeoLocation(): GeoLocation = GeoLocation(this.x, this.y)
fun GeoLocation.toPoint(): Point = Point(this.longitude, this.latitude)
fun Box.toBoundingBox(): BoundingBox = BoundingBox(first.x, first.y, second.x, second.y)
fun BoundingBox.toBox(): Box = Box(Point(left, top), Point(right, bottom))

/**
 * MongoDB 의 Geometry 관련 Helper class
 *
 * @author sunghyouk.bae@gmail.com
 */
object MongodbGeometry {

  /**
   * [Point] 인스턴스를 [GeoLocation] 인스턴스로 변환한다.

   * @param point [Point] 인스턴스
   * *
   * @return 변환된 [GeoLocation] 인스턴스
   */
  fun toGeoLocation(point: Point): GeoLocation {
    return GeoLocation.of(point.y, point.x)
  }

  /**
   * [GeoLocation] 인스턴스를 [Point]로 변환한다.

   * @param location [GeoLocation] 인스턴스
   * *
   * @return 변환된 [Point] 인스턴스
   */
  fun toPoint(@NonNull location: GeoLocation): Point {
    return Point(location.longitude, location.latitude)
  }

  /**
   * [Box] 인스턴스를 [BoundingBox]로 변환합니다

   * @param box [Box] 인스턴스
   * *
   * @return 변환된 [BoundingBox] 인스턴스
   */
  fun toBoundingBox(@NonNull box: Box): BoundingBox {
    return BoundingBox.of(box.first.x,
                          box.first.y,
                          box.second.x,
                          box.second.y)
  }

  /**
   * [BoundingBox] 인스턴스를 [Box]로 변환합니다

   * @param bbox [BoundingBox] 인스턴스
   * *
   * @return 변환된 [Box] 인스턴스
   */
  fun toBox(@NonNull bbox: BoundingBox): Box {
    // bottom-left, upper-right ([0,0], [100,100])
    return Box(Point(bbox.left, bbox.bottom),
               Point(bbox.right, bbox.top))
  }
}