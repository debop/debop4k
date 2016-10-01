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
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Point2D;

import static debop4k.science.gis.coords.GeometryEx.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * GeometryEx 관련 테스트 코드
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 12. 12.
 */
@Slf4j
public class GeometryExTest extends AbstractGisTest {

  @Test
  public void convertDegree() {
    double latitude = 35.7353389;
    DMS dms = DMS.of(35, 44, 7.22);

    double convertedLatitude = GeometryEx.toDegree(35, 44, 7.22);
    assertThat(convertedLatitude).isEqualTo(latitude, Offset.offset(1e-7));
    log.debug("latitude={}, converted={}", latitude, convertedLatitude);

    DMS convertedDMS = GeometryEx.toDMS(latitude);

    assertThat(convertedDMS.getD()).isEqualTo(dms.getD());
    assertThat(convertedDMS.getM()).isEqualTo(dms.getM());
    assertThat(convertedDMS.getS()).isEqualTo(dms.getS(), Offset.offset(1e-2));

  }

  Point2D.Double[] points = new Point2D.Double[]{
      new Point2D.Double(0, 0),
      new Point2D.Double(100, 0),
      new Point2D.Double(100, 100),
      new Point2D.Double(0, 100),
      //new Point2D.Double(0, 0)
  };

  @Test
  public void areaByPoints() {
    double area = area(points);
    assertThat(area).isEqualTo(100 * 100);
  }

  @Test
  public void areaByPolygon() {
    Polygon polygon = pointsToPolygon(points);
    double area = area(polygon);
    assertThat(area).isEqualTo(100 * 100);
  }

  @Test
  public void testCenterOfGravity() {
    Point2D.Double center = centerOfGravity(points);
    assertThat(center).isEqualTo(new Point2D.Double(50, 50));

    Point2D.Double center2 = centerOfGravity(FastList.newListWith(points));
    assertThat(center2).isEqualTo(new Point2D.Double(50, 50));
  }

  // -Y 축 기준으로 시계 반대 방향
  @Test
  public void testAngle() {
    double angle = getAngle(0, 0, 1, 0);
    assertThat(angle).isEqualTo(90);

    angle = getAngle(0, 0, 0, 1);
    assertThat(angle).isEqualTo(180);

    angle = getAngle(0, 0, 1, 1);
    assertThat(angle).isEqualTo(135);
  }

  @Test
  public void vectorValue() {
    // Y 축이 방향이 각도 0 이고,시계방향으로 계산한다.
    Point2D.Double start = new Point2D.Double(0, 0);
    Point2D.Double end = new Point2D.Double(1, 0);

    Vector vv = GeometryEx.getVector(start, end);
    assertThat(vv.getDegree()).isEqualTo(90);
    assertThat(vv.getLength()).isEqualTo(1);

    Point2D.Double end2 = getVectorEndPoint(start, vv.getDegree(), vv.getLength());
    assertThat(end2.x).isEqualTo(end.x, TOLERANCE);
    assertThat(end2.y).isEqualTo(end.y, TOLERANCE);
  }

  @Test
  public void testRotateXY() {
    Point2D.Double origin = new Point2D.Double(0, 0);
    Point2D.Double moved = rotateXYPoint(origin, new Point2D.Double(1, 0), 90);
    assertThat(moved.x).isCloseTo(0, TOLERANCE);
    assertThat(moved.y).isCloseTo(1, TOLERANCE);

    moved = rotateXYPoint(origin, new Point2D.Double(1, 0), -90);
    assertThat(moved.x).isCloseTo(0, TOLERANCE);
    assertThat(moved.y).isCloseTo(-1, TOLERANCE);
  }

  @Test
  public void testCheckPositionPoint() {
    Point2D.Double origin = new Point2D.Double(0, 0);

    Point2D.Double inner = checkPositionPoint(origin, points[0], points[2]);
    assertThat(inner).isEqualTo(origin);

    Point2D.Double leftTop = checkPositionPoint(points[0], points[0], points[2]);
    assertThat(leftTop).isEqualTo(points[0]);

    Point2D.Double ltOver = checkPositionPoint(new Point2D.Double(points[0].x - 10, points[0].y - 10),
                                               points[0],
                                               points[2]);
    assertThat(ltOver).isEqualTo(points[0]);

    Point2D.Double bottomRight = checkPositionPoint(points[2], points[0], points[2]);
    assertThat(bottomRight).isEqualTo(points[2]);

    Point2D.Double brOver = checkPositionPoint(new Point2D.Double(points[2].x + 10, points[2].y + 10),
                                               points[0],
                                               points[2]);
    assertThat(brOver).isEqualTo(points[2]);

    // LeftTop 과 BottomRight 를 변경해도, 대응 처리를 해줍니다.
    brOver = checkPositionPoint(new Point2D.Double(points[2].x + 10, points[2].y + 10),
                                points[2],
                                points[0]);
    assertThat(brOver).isEqualTo(points[2]);
  }


  @Test
  public void parseBoundingBox() {
    BoundingBox bbox = parseBbox("14372607.30251826,4187526.157575095,14382391.242138762,4197310.097195597");

    assertThat(bbox).isNotNull();
    assertThat(bbox.getMinX()).isEqualTo(14372607.30251826);
    assertThat(bbox.getMinY()).isEqualTo(4187526.157575095);
    assertThat(bbox.getMaxX()).isEqualTo(14382391.242138762);
    assertThat(bbox.getMaxY()).isEqualTo(4197310.097195597);

    bbox = parseBbox("19411336.20707708,6261721.35712164,20037508.342789244,6887893.492833804");
    assertThat(bbox).isNotNull();
  }


}
