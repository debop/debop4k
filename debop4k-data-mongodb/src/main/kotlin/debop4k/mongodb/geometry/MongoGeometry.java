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

package debop4k.mongodb.geometry;

import debop4k.science.gis.coords.BoundingBox;
import debop4k.science.gis.coords.GeoLocation;
import lombok.NonNull;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;


/**
 * MongoDB 의 Geometry 관련 Helper class
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 23.
 * @deprecated use {@link MongodbGeometry}
 */
@Deprecated
public final class MongoGeometry {

  private MongoGeometry() {}

  /**
   * {@link Point} 인스턴스를 {@link GeoLocation} 인스턴스로 변환한다.
   *
   * @param point {@link Point} 인스턴스
   * @return 변환된 {@link GeoLocation} 인스턴스
   */
  public static GeoLocation toGeoLocation(Point point) {
    return GeoLocation.of(point.getY(), point.getX());
  }

  /**
   * {@link GeoLocation} 인스턴스를 {@link Point}로 변환한다.
   *
   * @param location {@link GeoLocation} 인스턴스
   * @return 변환된 {@link Point} 인스턴스
   */
  public static Point toPoint(@NonNull GeoLocation location) {
    return new Point(location.getLongitude(), location.getLatitude());
  }

  /**
   * {@link Box} 인스턴스를 {@link BoundingBox}로 변환합니다
   *
   * @param box {@link Box} 인스턴스
   * @return 변환된 {@link BoundingBox} 인스턴스
   */
  public static BoundingBox toBoundingBox(@NonNull Box box) {
    return BoundingBox.of(box.getFirst().getX(),
                          box.getFirst().getY(),
                          box.getSecond().getX(),
                          box.getSecond().getY());
  }

  /**
   * {@link BoundingBox} 인스턴스를 {@link Box}로 변환합니다
   *
   * @param bbox {@link BoundingBox} 인스턴스
   * @return 변환된 {@link Box} 인스턴스
   */
  public static Box toBox(@NonNull BoundingBox bbox) {
    // bottom-left, upper-right ([0,0], [100,100])
    return new Box(new Point(bbox.getLeft(), bbox.getBottom()),
                   new Point(bbox.getRight(), bbox.getTop()));
  }


}
