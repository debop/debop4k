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

import debop4k.science.gis.coords.GeoLocation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.util.List;
import java.util.Map;

/**
 * OceanShapeFileLoadTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2016. 3. 4.
 */
@Slf4j
public class OceanShapeFileLoadTest extends AbstractShapeFileTest {

  @Test
  public void loadOceanShapeFile() {
    loadShapeFile(SHAPEFILE_OCEAN);
  }

  @Test
  public void loadHarborShapeFile() {
    loadShapeFile(SHAPEFILE_HARBOUR);
  }

  @SneakyThrows
  private void loadShapeFile(String path) {
    Shape shape = ShapeFilex.loadShape(ResourceUtils.getFile(path));
    Assertions.assertThat(shape).isNotNull();

    ShapeHeader header = shape.getHeader();
    log.trace("header={}", header);

    List<ShapeAttribute> attrs = shape.getAttributes();
    for (int i = 0; i < attrs.size(); i++) {
      ShapeAttribute attr = attrs.get(i);
      log.debug("==============  index={}", i);

      for (Map.Entry<String, Object> entry : attr.getFields().entrySet()) {
        log.debug("field name={}, value={}", entry.getKey(), entry.getValue());
      }
    }

    int recordSize = shape.getRecords().size();
    log.debug("record n={}", shape.getRecords().size());
    for (int r = 0; r < recordSize; r++) {
      List<GeoLocation> locations = shape.getLocations(r);
      log.trace("locations={}", locations);
    }
  }
}
