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

package debop4k.science.gis.coords;

import debop4k.science.gis.AbstractGisTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.Map;

import static debop4k.science.gis.coords.UtmZonex.utmZoneOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * UtmZonexTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 11.
 */
@Slf4j
public class UtmZonexTest extends AbstractGisTest {

  static final UtmZone utm31N = utmZoneOf(31, 'N');
  static final UtmZone utm51S = utmZoneOf(51, 'S');
  static final UtmZone utm52S = utmZoneOf(52, 'S');

  @Test
  public void testUtmZone() {

    double latitude = UtmZoneEx.getLatitudeByUtm(utm51S.getLatitudeZone());
    double longitude = UtmZoneEx.getLongitudeByUtm(utm51S.getLongitudeZone());

    assertThat(latitude).isEqualTo(32D);
    assertThat(longitude).isEqualTo(120D);

    UtmZone utm51S2 = UtmZoneEx.getUtmZone(GeoLocation.of(latitude, longitude));
    assertThat(utm51S2).isEqualTo(utm51S);

    UtmZone utmZone2 = UtmZoneEx.getUtmZone(GeoLocation.of(38, 127));
    assertThat(utmZone2).isEqualTo(utm52S);

    UtmZone utmZone3 = UtmZoneEx.getUtmZone(GeoLocation.of(0, 0));
    assertThat(utmZone3).isEqualTo(utm31N);
  }

  @Test
  public void utmZoneToBoundingBox() {
    BoundingBox bbox31N = UtmZoneEx.getBoundingBox(utm31N);

    assertThat(bbox31N.getBottom()).isEqualTo(0);
    assertThat(bbox31N.getLeft()).isEqualTo(0);

    BoundingBox bbox51S = UtmZoneEx.getBoundingBox(utm51S);
    assertThat(bbox51S.getWidth()).isEqualTo(UtmZonex.UTM_LONGITUDE_SIZE);
    assertThat(bbox51S.getHeight()).isEqualTo(UtmZonex.UTM_LATITUDE_SIZE);

    GeoLocation loc51S = UtmZoneEx.getGeoLocation(utm51S);
    assertThat(bbox51S.contains(loc51S)).isTrue();
  }

  @Test
  public void allUtmZone() {

    for (Map.Entry<UtmZone, BoundingBox> entry : UtmZonex.getUtmZoneBoundingBoxes().entrySet()) {
      log.debug("UtmZone={}, BBox={}", entry.getKey(), entry.getValue());
    }
  }

  @Test
  public void cellBoundingBox() {
    double cellUnitSize = 1. / 120.;

    BoundingBox utmBbox = UtmZoneEx.getBoundingBox(utm51S);
    BoundingBox cellBbox = UtmZoneEx.getCellBoundingBox(utm51S, cellUnitSize, 0, 0);

    assertThat(cellBbox.getLeft()).isEqualTo(utmBbox.getLeft(), Offset.offset(1e-6));
    assertThat(cellBbox.getTop()).isEqualTo(utmBbox.getTop(), Offset.offset(1e-6));
    assertThat(cellBbox.getWidth()).isEqualTo(cellUnitSize, Offset.offset(1e-6));
    assertThat(cellBbox.getHeight()).isEqualTo(cellUnitSize, Offset.offset(1e-6));

    cellBbox = UtmZoneEx.getCellBoundingBox(utm51S,
                                            cellUnitSize,
                                            120 * UtmZonex.UTM_LATITUDE_SIZE - 1,
                                            120 * UtmZonex.UTM_LONGITUDE_SIZE - 1);

    assertThat(cellBbox.getRight()).isEqualTo(utmBbox.getRight(), Offset.offset(1e-6));
    assertThat(cellBbox.getBottom()).isEqualTo(utmBbox.getBottom(), Offset.offset(1e-6));
    assertThat(cellBbox.getWidth()).isEqualTo(cellUnitSize, Offset.offset(1e-6));
    assertThat(cellBbox.getHeight()).isEqualTo(cellUnitSize, Offset.offset(1e-6));
  }

}
