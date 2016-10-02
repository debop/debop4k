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

package debop4k.science.gis.shapefiles;///*
// * Copyright (c) 2016. KESTI co, ltd
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.science.gis.shapefiles;
//
//import com.vividsolutions.jts.geom.GeometryFactory;
//import debop4k.core.Charsets;
//import lombok.NonNull;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Loggingx;
//import org.eclipse.collections.api.list.MutableList;
//import org.eclipse.collections.impl.list.mutable.FastList;
//import org.geotools.data.shapefile.dbf.DbaseFileHeader;
//import org.geotools.data.shapefile.dbf.DbaseFileReader;
//import org.geotools.data.shapefile.files.ShpFiles;
//import org.geotools.data.shapefile.shp.ShapefileReader;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.nio.charset.Charset;
//import java.util.List;
//
///**
// * ShapeFile 에서 정보를 로드하는 클래스입니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Loggingx
//public final class ShapeFileLoader {
//
//  /**
//   * Shpae 파일을 읽어 {@link Shape} 인스턴스를 빌드합니다.
//   *
//   * @param file Shapefile
//   * @return {@link Shape} 인스턴스
//   */
//  @SneakyThrows({ MalformedURLException.class, IOException.class })
//  public static Shape load(@NonNull File file) {
//
//    ShpFiles shpFiles = new ShpFiles(file);
//    Shape shape = new Shape();
//
//    GeometryFactory gf = new GeometryFactory();
//    ShapefileReader reader = null;
//    try {
//      reader = new ShapefileReader(shpFiles, false, false, gf);
//      ShapeHeader header = loadShapeHeader(reader);
//      shape.setHeader(header);
//      shape.setRecords(loadShapeRecords(reader));
//      shape.setAttributes(loadShapeAttributes(shpFiles));
//
//      log.trace("로드된 Shape 정보={}", shape);
//
//    } finally {
//      if (reader != null)
//        reader.close();
//    }
//    return shape;
//  }
//
//  private static ShapeHeader loadShapeHeader(@NonNull ShapefileReader reader) {
//    return ShapeHeader.of(reader.getHeader());
//  }
//
//  @SneakyThrows({ IOException.class })
//  private static MutableList<ShapeRecord> loadShapeRecords(@NonNull ShapefileReader reader) {
//    log.debug("Shape 파일에서 Shape 정보를 로드합니다...");
//
//    MutableList<ShapeRecord> records = FastList.newList();
//
//    while (reader.hasNext()) {
//      records.add(ShapeRecord.of(reader.nextRecord()));
//    }
//
//    log.debug("로드된 Shape Records={}", records);
//    return records;
//  }
//
//  private static MutableList<ShapeAttribute> loadShapeAttributes(@NonNull ShpFiles shpFiles) {
//    return loadShapeAttributes(shpFiles, Charsets.UTF_8);
//  }
//
//  @SneakyThrows({ IOException.class })
//  private static MutableList<ShapeAttribute> loadShapeAttributes(@NonNull ShpFiles shpFiles,
//                                                                 @NonNull Charset cs) {
//    log.debug("Shape 파일의 특성정보를 로드합니다...");
//
//    DbaseFileReader reader = new DbaseFileReader(shpFiles, false, cs);
//    MutableList<ShapeAttribute> attrs = FastList.newList();
//
//    try {
//      // Header 정보 로드
//      DbaseFileHeader header = reader.getHeader();
//      int fieldCount = header.getNumFields();
//      List<String> headerNames = FastList.newList();
//
//      for (int i = 0; i < fieldCount; i++) {
//        headerNames.add(header.getFieldName(i));
//      }
//      // 레코드 정보 로드
//      int r = 0;
//      while (reader.hasNext()) {
//        Object[] entry = reader.readEntry();
//        ShapeAttribute attr = new ShapeAttribute();
//        for (int i = 0; i < entry.length; i++) {
//          attr.addField(headerNames.get(i), entry[i]);
//        }
//        attrs.add(attr);
//      }
//      log.trace("로드된 특성정보:{}", attrs);
//    } finally {
//      reader.close();
//    }
//    return attrs;
//  }
//}
