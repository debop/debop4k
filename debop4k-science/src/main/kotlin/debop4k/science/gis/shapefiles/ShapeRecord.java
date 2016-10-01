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
//import com.vividsolutions.jts.geom.Geometry;
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import debop4k.science.gis.coords.BoundingBox;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import lombok.Setter;
//import org.geotools.data.shapefile.shp.ShapeType;
//import org.geotools.data.shapefile.shp.ShapefileReader;
//
///**
// * Shape 파일의 여러 개의 형상 중 하나의 형상을 표현하는 정보를 표현합니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//@NoArgsConstructor
//public class ShapeRecord extends AbstractValueObject {
//
//  public static ShapeRecord of(@NonNull ShapefileReader.Record record) {
//    ShapeRecord sr = new ShapeRecord();
//
//    sr.number = record.number;
//    sr.type = record.type;
//    sr.bounds = BoundingBox.of(record.minX, record.minY, record.maxX, record.maxY);
//    sr.geom = (Geometry) record.shape();
//
//    return sr;
//  }
//
//  /** 레코드 번호 */
//  private int number;
//  /** Shape 형태 (Polygon, Point ...) */
//  private ShapeType type;
//  /** Shape 의 외접 사각형 */
//  private BoundingBox bounds;
//  /** Shape 의 geometry 정보 */
//  private Geometry geom;
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(number);
//  }
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("number", number)
//                .add("type", type)
//                .add("bounds", bounds)
//                .add("geom", geom);
//
//  }
//  private static final long serialVersionUID = -6042989702174763993L;
//}
