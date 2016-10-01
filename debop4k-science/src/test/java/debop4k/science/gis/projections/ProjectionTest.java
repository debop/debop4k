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

package debop4k.science.gis.projections;

import com.jhlabs.map.proj.Projection;
import com.jhlabs.map.proj.ProjectionFactory;
import debop4k.science.gis.AbstractGisTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * ProjectionTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 9.
 */
@Slf4j
public class ProjectionTest extends AbstractGisTest {

  // 좌표변환 참고
  //  1. http://spatialreference.org/ref/epsg/32652/
  //  2. http://www.osgeo.kr/17

  // Data 참고 : http://www.latlong.net/place/seoul-south-korea-621.html
  @Test
  public void utmToWgs84() {
    Point2D.Double seoul = new Point2D.Double(127.024612, 37.532600);
    Point2D.Double tongyeong = new Point2D.Double(128.429581, 34.855228);

    List<String> SPEC_UTM_WGS84 = FastList.newListWith(
        "+proj=utm",
        "+ellps=WGS84",
        "+datum=WGS84",
        "+units=m",
        "+no_defs"
                                                      );

    List<String> utm52 = FastList.newList(SPEC_UTM_WGS84);
    utm52.add("+zone=52");

    String[] utm52Spec = new String[utm52.size()];

    Projection projection = ProjectionFactory.fromPROJ4Specification(utm52.toArray(utm52Spec));

//    Point2D.Double utm = projection.transform(seoul, new Point2D.Double());
//    log.debug("seoul={}, utm={}", seoul, utm);

    log.debug("서울={}, utm={}", seoul, projection.transform(seoul, new Point2D.Double()));
    log.debug("통영={}, utm={}", tongyeong, projection.transform(tongyeong, new Point2D.Double()));
  }
}
