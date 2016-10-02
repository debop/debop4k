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

import debop4k.core.Guardx;
import debop4k.core.collections.Collectionx;
import debop4k.core.utils.Stringx;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

import static debop4k.core.utils.Convertx.asDouble;
import static debop4k.science.gis.coords.Geometryx.vectorOf;
import static java.lang.Math.*;

/**
 * GeometryEx2 는 {@link GeometryEx} 가 -Y 축을 기준으로 각도를 계산해서, +X 축으로 각도를 계산하는 모듈을 새로 만듦
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2016. 3. 14.
 * @deprecated use {@link Geometryx}
 */
@Slf4j
@Deprecated
public final class GeometryEx2 {

  private GeometryEx2() {}

  /**
   * Pixel in meter
   */
  public static double Px2M = 0.0002645833333333;

  public static double MIN_X = -20037508.3427892;
  public static double MAX_X = 20037508.3427892;
  public static double MIN_Y = -44927335.4270971;
  public static double MAX_Y = 44927335.4270966;

  public static double MIN_Y_ENC = -147730762.669922;
  public static double MAX_Y_ENC = 147730758.194568;

  public static final Point2D.Double TOP_LEFT = new Point2D.Double(MIN_X, 147730758.194568); // lon : -180, lat :  89.99999999
  public static final Point2D.Double BOTTOM_RIGHT = new Point2D.Double(MAX_X, -147730762.669922); // lon :  180, lat : -89.99999999

  public static final BoundingBox WorldBBox = BoundingBox.of(MIN_X, MIN_Y, MAX_X, MAX_Y);
  public static final BoundingBox EncBBox = BoundingBox.of(MIN_X, MIN_Y_ENC, MAX_X, MAX_Y_ENC); // lon: -180 ~ 180, lat = 90 ~ -90

  public static double PI_M2 = PI * 2.0;
  public static double PI_D2 = PI / 2.0;

  /**
   * 도/분/초 단위의 경위도 값을 double 수형의 도 단위로 변환한다.
   *
   * @param degree  도
   * @param minutes 분
   * @return 도 단위의 변환
   */
  public static double toDegree(int degree, double minutes) {
    return degree + minutes / 60.0;
  }

  /**
   * 도/분/초 단위의 경위도 값을 dobule 수형의 도 단위로 변환한다.
   *
   * @param d 도
   * @param m 분
   * @param s 초
   * @return 도 단위의 변환
   */
  public static double toDegree(int d, double m, double s) {
    return d + m / 60.0 + s / 3600.0;
  }

  /**
   * double 수형의 값을 도/분/초 단위로 변환한다.
   *
   * @param degree 경위도 값
   * @return 도/분/초 형태의 값
   */
  public static DMS toDMS(double degree) {
    return Geometryx.toDMS(degree);
  }


  /**
   * 유효한 위도(Latitude) 인지 판단합니다. (-90.0 ~ 90.0 사이이어야 합니다)
   *
   * @param latitude 위도 값
   */
  public static boolean validLatitude(double latitude) {
    return (latitude >= -90.0 && latitude <= 90.0);
  }

  /**
   * 유효한 경도(Longutude) 인지 판단합니다. (-180.0 ~ 180.0 사이이어야 합니다)
   *
   * @param longitude 경도 값
   */
  public static boolean validLongitude(double longitude) {
    return (longitude >= -180.0 && longitude <= 180.0);
  }

  /**
   * 사각형 영역 (Left-Top, Bottom-Right) 의 좌표를 가진 문자열을 파싱해서 최대 영역 안에 존재하는 영역인지 파악합니다.
   *
   * @param bboxStr x1,y1,x2,y2 죄표를 표현한 문자열
   * @return {@link BoundingBox} 인스턴스, 파싱 실패 시에는 null 값을 반환합니다.
   */
  public static BoundingBox parseBbox(String bboxStr) {
    return parseBbox(bboxStr, WorldBBox);
  }

  /**
   * 사각형 영역 (Left-Top, Bottom-Right) 의 좌표를 가진 문자열을 파싱해서 최대 영역 안에 존재하는 영역인지 파악합니다.
   *
   * @param bboxStr  x1,y1,x2,y2 죄표를 표현한 문자열
   * @param limitBox 좌표계의 최대 / 최소값
   * @return {@link BoundingBox} 인스턴스, 파싱 실패 시에는 null 값을 반환합니다.
   */
  public static BoundingBox parseBbox(String bboxStr, BoundingBox limitBox) {
    log.trace("bbox 값을 파싱합니다. bbox={}", bboxStr);

    if (Stringx.isEmpty(bboxStr))
      return null;

    try {
      String[] array = bboxStr.split(String.valueOf(Stringx.COMMA));
      Guardx.shouldBe(array.length == 4, "잘못된 bbox 문자열입니다. bboxStr=%s", bboxStr);

      double x1 = asDouble(array[0], Double.MIN_VALUE);
      double y1 = asDouble(array[1], Double.MIN_VALUE);
      double x2 = asDouble(array[2], Double.MAX_VALUE);
      double y2 = asDouble(array[3], Double.MAX_VALUE);

      return BoundingBox.of(x1, y1, x2, y2);
    } catch (Exception ignored) {
      log.error("bbox 파싱에 실패했습니다. bboxStr={}", bboxStr);
      log.error("예외정보:", ignored);
      return null;
    }
  }

  /**
   * {@link Polygon} 각 꼭지점을 {@link Point2D.Double} 배열로 변환합니다.
   *
   * @param polygon {@link Polygon}
   * @return {@link Point2D.Double} 배열
   */
  public static Point2D.Double[] polygonToPointArray(Polygon polygon) {
    if (polygon == null || polygon.npoints == 0)
      return new Point2D.Double[0];

    int count = polygon.npoints;
    Point2D.Double[] points = new Point2D.Double[count];
    for (int i = 0; i < count; i++) {
      points[i] = new Point2D.Double(polygon.xpoints[i], polygon.ypoints[i]);
    }
    return points;
  }

  /**
   * Point 배열을 {@link Polygon} 으로  빌드합니다.
   *
   * @param points 다각형을 구성할 Point 의 배열
   * @return {@link Polygon} 인스턴스
   */
  public static Polygon pointsToPolygon(Point2D.Double[] points) {
    if (points == null || points.length == 0)
      return new Polygon();

    Polygon polygon = new Polygon();
    for (Point2D.Double p : points) {
      polygon.addPoint((int) p.x, (int) p.y);
    }
    return polygon;
  }

  /**
   * 다각형 영역에 대한 면적을 구합니다.
   * 참고: http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
   *
   * @param polygon array of points in the polygon
   * @return polygon point 로 둘러싸인 부분의 면적
   */
  public static double area(Polygon polygon) {
    return area(polygonToPointArray(polygon));
  }

  /**
   * 다각형 영역에 대한 면적을 구합니다.
   * 참고: http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
   *
   * @param points array of points in the polygon
   * @return polygon point 로 둘러싸인 부분의 면적
   */
  public static double area(Point2D.Double[] points) {
    if (points == null || points.length == 0)
      return 0.0;

    int n = points.length;
    double area = 0.0;
    for (int i = 0; i < n; i++) {
      int j = (i + 1) % n;
      area += points[i].x * points[j].y;
      area -= points[j].x * points[i].y;
    }
    area /= 2.0;
    log.trace("calc area. area={}, points length={}", area, points.length);
    return area;
  }

  /**
   * 다각형 영역 면의 무게중심을 구합니다.
   * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
   *
   * @param polygon 다각형
   * @return 다각형의 무게 중심 위치
   */
  public static Point2D.Double centerOfGravity(Polygon polygon) {
    return centerOfGravity(polygonToPointArray(polygon));
  }

  public static Point2D.Double centerOfGravity(Collection<Point2D.Double> points) {
    if (points == null || points.size() == 0)
      new Point2D.Double();

    assert (points != null);
    return centerOfGravity(Collectionx.asArray(points, Point2D.Double.class));
  }

  /**
   * 다각형 영역 면의 무게중심을 구합니다.
   * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
   *
   * @param points 다각형을 구성하는 Point 배열
   * @return 다각형의 무게 중심 위치
   */
  public static Point2D.Double centerOfGravity(Point2D.Double[] points) {
    if (points == null || points.length == 0)
      return new Point2D.Double();

    double cx = 0.0;
    double cy = 0.0;
    double area = area(points);
    int n = points.length;
    double factor;

    for (int i = 0; i < n; i++) {
      int j = (i + 1) % n;
      factor = points[i].x * points[j].y - points[j].x * points[i].y;
      cx += (points[i].x + points[j].x) * factor;
      cy += (points[i].y + points[j].y) * factor;
    }
    factor = 1.0 / (area * 6.0);
    cx *= factor;
    cy *= factor;
    log.trace("center of gravity. cx={}, cy={}", cx, cy);
    return new Point2D.Double(cx, cy);
  }

  /**
   * 두 포인트의 각도를 계산합니다. 반환 값의 단위는 Degree 입니다.
   * NOTE : +X 측을 각도 0으로 시계반대방향으로 각도가 증가합니다!!!
   * OLD: -Y 축 (South 방향) 방향이 각도 0 이고 시계 반대 방향으로 계산한다.
   *
   * @return 두 점의 각도 (단위: Degree)
   */
  public static double getAngle(double x1, double y1, double x2, double y2) {
    try {
      double dx = x2 - x1;
      double dy = y2 - y1;

      // +X 축 방향이 0도이고, 시계반대방향으로 증가한다.
      if (dx == 0.0) {
        return 90.0;
      }
      double rad = atan2(dy, dx);

      // -Y 축 (South 방향) 방향이 각도 0 이고 시계 반대 방향으로 계산한다.
//      double rad = atan2(dy, dx) + PI / 2;

      double degree = toDegrees(rad);
      if (degree < 0)
        degree += 360;
      if (degree >= 360)
        degree -= 360;

      return degree;
    } catch (Exception ignored) {
      log.error("각도 구하기 실패.", ignored);
      return Double.NaN;
    }
  }

  /**
   * 두 포인트의 각도를 계산합니다. 반환 값의 단위는 Degree 입니다.
   *
   * @param p1 Point 1
   * @param p2 Point 2
   * @return 두 점의 각도 (단위: Degree)
   */
  public static double getAngle(Point2D.Double p1, Point2D.Double p2) {
    return getAngle(p1.x, p1.y, p2.x, p2.y);
  }


  /**
   * 시작 점과 끝점을 이용하여 벡터를 표현하는 클래스 {@link Vector} 를 생성합니다.
   * NOTE: +X 축이 방향이 각도 0 이고, 시계반대방향으로 계산한다.
   *
   * @param s 시작 Point
   * @param e 끝 Point
   * @return {@link Vector} 인스턴스
   */
  public static Vector getVector(@NonNull Point2D.Double s, @NonNull Point2D.Double e) {
    double dx = e.x - s.x;
    double dy = e.y - s.y;
    double distance = sqrt(dx * dx + dy * dy);
    double degree = getAngle(s, e);

    return vectorOf(degree, distance);
  }

  /**
   * 기준점으로부터 지정한 각도와 길이에 해당하는 Point 를 구한다.
   * 각도는 +X 측을 0으로, 시계 반대방향으로 증가한다.
   *
   * @param base   벡터의 시작 Point
   * @param degree 각도 (단위 : degree)
   * @param length 벡터 길이
   * @return 벡터의 끝 Point
   */
  public static Point2D.Double getVectorEndPoint(Point2D.Double base, double degree, double length) {
    double rad = toRadians(degree);
    double x = length * cos(rad) + base.x;
    double y = length * sin(rad) + base.y;

    return new Point2D.Double(x, y);
  }

  /**
   * 원점을 기준으로 point를 각도만큼 회전 시킵니다.
   *
   * @param point  회전 시킬 포인트
   * @param degree 회전 각도 (단위: degree)
   * @return 회전한 포인트
   */
  public static Point2D.Double rotateXYPoint(Point2D.Double point, double degree) {
    return rotateXYPoint(new Point2D.Double(0, 0), point, degree);
  }

  /**
   * basePoint 를 기준으로 point를 각도만큼 시계 방향으로 회전 시킵니다.
   *
   * @param basePoint 기준 포인트
   * @param point     회전 시킬 포인트
   * @param degree    회전 각도 (단위: degree)
   * @return 회전한 포인트
   */
  public static Point2D.Double rotateXYPoint(Point2D.Double basePoint,
                                             Point2D.Double point,
                                             double degree) {
    double rad = toRadians(degree % 360.0);
    double cos = cos(rad);
    double sin = sin(rad);

    double dx = point.x - basePoint.x;
    double dy = point.y - basePoint.y;

    double x = dx * cos + dy * sin + basePoint.x;
    double y = dx * sin - dy * cos + basePoint.y;

    return new Point2D.Double(x, y);
  }

  /**
   * 원점을 중심으로 특정 Point를 시계방향 회전시켜 이동한 Point를 계산합니다.
   *
   * @param p      변경할 Point
   * @param degree 회전할 각도 (단위: Degree)
   * @return 회전환 Point 값
   */
  public static Point2D.Double rotateXY(Point p, double degree) {
    return rotateXY(0, 0, p.x, p.y, degree);
  }

  /**
   * 기준점을 중심으로 특정 Point를 시계 반대방향으로 회전시켜 이동한 Point를 계산합니다.
   *
   * @param base   기준 점
   * @param p      변경할 Point
   * @param degree 회전할 각도 (단위: Degree)
   * @return 회전환 Point 값
   */
  public static Point2D.Double rotateXY(Point base, Point p, double degree) {
    return rotateXYPoint(new Point2D.Double(base.x, base.y),
                         new Point2D.Double(p.x, p.y),
                         degree);
  }

  /**
   * 기준점을 중심으로 특정 Point를 시계 반대방향으로 회전시켜 이동한 Point를 계산합니다.
   *
   * @param baseX  기준점 X 좌표
   * @param baseY  기준점 Y 좌표
   * @param x      변경할 점 X 좌료
   * @param y      변경할 점 Y 좌료
   * @param degree 회전할 각도 (단위 : Degree)
   * @return 회전환 Point 값
   */
  public static Point2D.Double rotateXY(int baseX, int baseY, int x, int y, double degree) {
    return rotateXYPoint(new Point2D.Double(baseX, baseY),
                         new Point2D.Double(x, y),
                         degree);
  }


  /**
   * 사각 영역안에 Point 를 위치시킵니다.
   *
   * @param point       대상 Point
   * @param leftTop     Left-Top Point
   * @param rightBottom Bottom-Right Point
   * @return 대상 Point 를 사각 영역안에 위치 시킨 새로운 Point
   */
  public static Point2D.Double checkPositionPoint(Point2D.Double point,
                                                  Point2D.Double leftTop,
                                                  Point2D.Double rightBottom) {
    return checkPositionPoint(point, BoundingBox.of(GeoLocation.of(leftTop.x, leftTop.y),
                                                    GeoLocation.of(rightBottom.x, rightBottom.y)));
  }

  /**
   * Point 가 BoundingBox 안에 위치하도록 값을 조정합니다.
   *
   * @param point 조정할 Point 값
   * @param bbox  영역을 나타내는 {@link BoundingBox}
   * @return 새롭게 조정된 Point
   */
  public static Point2D.Double checkPositionPoint(Point2D.Double point, BoundingBox bbox) {
    double x = min(max(point.x, bbox.getMinX()), bbox.getMaxX());
    double y = min(max(point.y, bbox.getMinY()), bbox.getMaxY());

    return new Point2D.Double(x, y);
  }

  /**
   * 지정한 라인을 n 등분한 위치 좌표들을 반환한다.
   *
   * @param line 라인 정보
   * @param n    등분할 수
   * @return 선분을 N 등분한 Point 중보
   */
  public static Point2D.Double[] getLineNPoints(@NonNull Line2D.Double line, int n) {
    Guardx.shouldBePositiveNumber(n, "n");

    if (n < 2) {
      return new Point2D.Double[]{(Point2D.Double) line.getP1(),
          (Point2D.Double) line.getP2()};
    }

    Point2D.Double start = (Point2D.Double) line.getP1();
    Point2D.Double end = (Point2D.Double) line.getP2();

    Vector vector = getVector(start, end);
    log.debug("vector={}", vector);

    Point2D.Double[] points = new Point2D.Double[n + 1];
    points[0] = (Point2D.Double) line.getP1();
    points[n] = (Point2D.Double) line.getP2();

    double length = vector.getLength();
    double nlen = length / (double) n;
    // vector의 각도는 -Y 축이 0 도이다. 그래서 +X 축으로 환산한 경우에는 -90 을 해줘야 한다.
    double degree = vector.getDegree();

    for (int i = 1; i < n; i++) {
      points[i] = getVectorEndPoint(start, degree, nlen * i);
    }

    return points;
  }

  /**
   * 두 선분의 교차점을 구한다. 교차점이 없을 때에는 null 을 반환합니다.
   *
   * @param l1 선분 1
   * @param l2 선분 2
   * @return 두 선분의 교차점.
   */
  public static Point2D.Double getIntersectPoint(Line2D.Double l1, Line2D.Double l2) {
    try {
      double under = (l2.y2 - l2.y1) * (l1.x2 - l1.x1) - (l2.x2 - l2.x1) * (l1.y2 - l1.y1);
      if (under == 0D)
        return null;

      double t = (l2.x2 - l2.x1) * (l1.y1 - l2.y1) - (l2.y2 - l2.y1) * (l1.x1 - l2.x1);
      double s = (l1.x2 - l1.x1) * (l1.y1 - l2.y1) - (l1.y2 - l1.y1) * (l1.x1 - l2.x1);

      if (t == 0 && s == 0)
        return null;

      t /= under;
      s /= under;

      if (t < 0.0 || t > 1.0 || s < 0.0 || s > 1.0)
        return null;

      double x = l1.x1 + t * (l1.x2 - l1.x1);
      double y = l1.y1 + t * (l1.y2 - l1.y1);

      return new Point2D.Double(x, y);

    } catch (Exception ignored) {
      log.error("두 선분 교차점 찾기 실패.", ignored);
      return null;
    }
  }
}
