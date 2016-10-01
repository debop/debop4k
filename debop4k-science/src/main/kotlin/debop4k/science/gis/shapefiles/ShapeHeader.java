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
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import debop4k.science.gis.coords.BoundingBox;
//import lombok.Getter;
//import lombok.NonNull;
//import lombok.Setter;
//import org.geotools.data.shapefile.shp.ShapeType;
//import org.geotools.data.shapefile.shp.ShapefileHeader;
//
///**
// * Shape 파일의 헤더 정보
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Setter
//public class ShapeHeader extends AbstractValueObject {
//
//  public static ShapeHeader of(@NonNull ShapefileHeader header) {
//    int size = header.getFileLength();
//    int version = header.getVersion();
//    ShapeType shapeType = header.getShapeType();
//    BoundingBox bounds = BoundingBox.of(header.minX(), header.minY(), header.maxX(), header.maxY());
//
//    return new ShapeHeader(size, version, shapeType, bounds);
//  }
//
//  private final int size;
//  private final int version;
//  private final ShapeType shapeType;
//  private final BoundingBox bounds;
//
//  public ShapeHeader(int size, int version, ShapeType shapeType, BoundingBox bounds) {
//    this.size = size;
//    this.version = version;
//    this.shapeType = shapeType;
//    this.bounds = bounds;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(shapeType, bounds);
//  }
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("size", size)
//                .add("version", version)
//                .add("shapeType", shapeType)
//                .add("bounds", bounds);
//
//  }
//  private static final long serialVersionUID = -1553565274056583599L;
//}
