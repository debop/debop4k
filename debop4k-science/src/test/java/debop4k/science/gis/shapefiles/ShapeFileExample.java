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

package debop4k.science.gis.shapefiles;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import debop4k.science.gis.coords.BoundingBox;
import kotlin.text.Charsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileHeader;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ESRI ShapeFile 읽기 관련 테스트
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class ShapeFileExample extends AbstractShapeFileTest {

  @Test
  @SneakyThrows({Exception.class})
  public void loadClusters() {
    File file = new File(SHAPEFILE_OCEAN);
    loadShapeFiles(file);
  }

  @Test
  @SneakyThrows({Exception.class})
  public void loadHabor() {
    File file = new File(SHAPEFILE_HARBOUR);
    loadShapeFiles(file);
  }

  private void loadShapeFiles(File file) throws Exception {
    assertThat(file).isNotNull();

    ShpFiles shpFiles = new ShpFiles(file);
    assertThat(shpFiles).isNotNull();
    verifyShapefiles(shpFiles);
  }

  private void verifyShapefiles(ShpFiles shpFiles) throws Exception {
    GeometryFactory geometryFactory = new GeometryFactory();

    ShapefileReader reader = null;
    try {
      reader = new ShapefileReader(shpFiles, true, false, geometryFactory);
      int index = 1;
      ShapefileHeader header = reader.getHeader();
      log.debug("header={}", header);

      log.debug("header shape type={}, bounds={}",
                header.getShapeType(),
                BoundingBox.of(header.minX(), header.minY(), header.maxX(), header.maxY()));

      while (reader.hasNext()) {

        ShapefileReader.Record record = reader.nextRecord();
        log.debug("record={}", record);
        Geometry geom = (Geometry) record.shape();

        log.debug("number={}, offset={}, Geometry={}", record.number, record.offset(), geom.getGeometryType());
        log.debug("geometry nums={}, nums of point={}", geom.getNumGeometries(), geom.getNumPoints());
        log.debug("boundary dimension={}", geom.getBoundaryDimension());
        log.debug("geom={}", geom);
        log.debug("userdata={}", geom.getUserData());

        index++;
      }
      assertThat(index).isGreaterThan(1);
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
  }


  @Test
  public void loadDBF() throws Exception {
    File file = new File(SHAPEFILE_OCEAN);
    ShpFiles shpFiles = new ShpFiles(file);
    DbaseFileReader dbfReader = new DbaseFileReader(shpFiles, false, Charsets.UTF_8);

    DbaseFileHeader header = dbfReader.getHeader();

    log.debug("header={}", header);
    log.debug("field number={}", header.getNumFields());

    int fieldCount = header.getNumFields();

    for (int i = 0; i < header.getNumFields(); i++) {
      log.debug("field index={}, field name={}, field type={}", i, header.getFieldName(i), header.getFieldType(i));
    }

    List<Map<String, ?>> rows = FastList.newList();
    int rowNo = 0;
    while (dbfReader.hasNext()) {
      dbfReader.read();
      Map<String, Object> row = Maps.mutable.of();
      for (int i = 0; i < fieldCount; i++) {
        Object value = dbfReader.readField(i);
        row.put(header.getFieldName(i), value);
      }
      rows.add(row);
    }

    log.debug("attrs={}", rows);
  }
}
