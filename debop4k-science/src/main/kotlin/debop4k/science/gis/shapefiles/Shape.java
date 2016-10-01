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

package debop4k.science.gis.shapefiles;//package debop4k.science.gis.shapefiles;
//
//import com.vividsolutions.jts.geom.Coordinate;
//import com.vividsolutions.jts.geom.Geometry;
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import debop4k.science.gis.coords.GeoLocation;
//import lombok.Getter;
//import lombok.Setter;
//import org.eclipse.collections.api.list.MutableList;
//import org.eclipse.collections.impl.list.mutable.FastList;
//
///**
// * Shape 파일 정보
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//public class Shape extends AbstractValueObject {
//
//  /** 헤더 정보 */
//  private ShapeHeader header;
//
//  /**
//   * Shape 파일의 레코드 정보
//   */
//  private MutableList<ShapeRecord> records = FastList.newList();
//
//  /** 특성 정보 */
//  private MutableList<ShapeAttribute> attributes = FastList.newList();
//
//  public int getRecordCount() {
//    return records.size();
//  }
//
//  public MutableList<GeoLocation> getLocations(int index) {
//    Geometry geom = getGeometry(index);
//
//    FastList<GeoLocation> locations = FastList.newList();
//    for (Coordinate coord : geom.getCoordinates()) {
//      locations.add(GeoLocation.of(coord.y, coord.x));
//    }
//
//    return locations;
//  }
//
//  public Geometry getGeometry(int index) {
//    return records.get(index).getGeom();
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(header);
//  }
//
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("header", header)
//                .add("attributes", attributes);
//  }
//
//  private static final long serialVersionUID = -168835409318104524L;
//}
