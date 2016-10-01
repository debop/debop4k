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

package debop4k.science.gis.coords;//package debop4k.science.gis.coords;
//
//import debop4k.core.AbstractValueObject;
//import debop4k.core.ToStringHelper;
//import debop4k.core.utils.Hashx;
//import lombok.Getter;
//import lombok.NonNull;
//
//import java.awt.geom.Point2D;
//
//import static java.lang.Math.max;
//import static java.lang.Math.min;
//
///**
// * 지구를 위경도 좌표를 이용하여, 사각형 격자로 영역을 표현하는 클래스입니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//public class BoundingBox extends AbstractValueObject implements Comparable<BoundingBox> {
//
//  /**
//   * BoundingBox 를 생성합니다.
//   *
//   * @param src 원본 BoundingBox
//   * @return 복사한 BoundingBox
//   */
//  public static BoundingBox of(@NonNull BoundingBox src) {
//    return of(src.left, src.top, src.right, src.bottom);
//  }
//
//  /**
//   * 경위도로 영역을 표현하는 BoundingBox 를 생성합니다.
//   *
//   * @param left   최소 경도
//   * @param top    최대 위도
//   * @param right  최대 경도
//   * @param bottom 최소 위도
//   * @return BoundingBox 인스턴스
//   */
//  public static BoundingBox of(double left, double top, double right, double bottom) {
//    return new BoundingBox(left, top, right, bottom);
//  }
//
//  /**
//   * 경위도로 영역을 표현하는 BoundingBox 를 생성합니다.
//   *
//   * @param lt left-top location
//   * @param rb right-bottom location
//   * @return BoundingBox 인스턴스
//   */
//  public static BoundingBox of(@NonNull GeoLocation lt, @NonNull GeoLocation rb) {
//    return of(lt.getLongitude(), lt.getLatitude(), rb.getLongitude(), rb.getLatitude());
//  }
//
//  /**
//   * 최소 경도 Longitude (Left)
//   */
////  private
//  final double left;
//  /**
//   * 최대 위도 Latitude (Top)
//   */
////  private
//  final double top;
//  /**
//   * 최대 경도 Longitude (Right)
//   */
////  private
//  final double right;
//  /**
//   * 최소 위도 Latitude (Bottom)
//   */
////  private
//  final double bottom;
//
//  /** 기본 생성자 */
//  public BoundingBox() { this(0, 0, 0, 0); }
//
//  /**
//   * 생성자
//   *
//   * @param left   최소 경도
//   * @param top    최대 위도
//   * @param right  최대 경도
//   * @param bottom 최소 위도
//   */
//  public BoundingBox(double left, double top, double right, double bottom) {
//    this.left = min(left, right);
//    this.top = max(top, bottom);
//    this.right = max(left, right);
//    this.bottom = min(top, bottom);
//  }
//
//  public BoundingBox(@NonNull GeoLocation lt, @NonNull GeoLocation rb) {
//    this(lt.getLongitude(), lt.getLatitude(), rb.getLongitude(), rb.getLatitude());
//  }
//
//  /**
//   * 경도의 Width
//   *
//   * @return 경도의 Width
//   */
//  public double getWidth() {
//    return Math.abs(right - left);
//  }
//
//  /**
//   * 위도의 Height
//   *
//   * @return 위도의 Height
//   */
//  public double getHeight() {
//    return Math.abs(top - bottom);
//  }
//
//  /**
//   * 특정 위치가 Bounding Box 에 포함되는지 여부 (LeftTop 은 include, RightBottom 은 exclude 이다)
//   *
//   * @param lat latitude
//   * @param lon longitude
//   * @return 포함 여부
//   */
//  public boolean contains(double lat, double lon) {
//    return (lon >= left && lon < right) &&
//           (lat <= top && lat > bottom);
//  }
//
//  /**
//   * 특정 위치가 Bounding Box 에 포함되는지 여부 (LeftTop 은 include, RightBottom 은 exclude 이다)
//   *
//   * @param geoLoc 위경도 정보
//   * @return 포함 여부
//   */
//  public boolean contains(@NonNull GeoLocation geoLoc) {
//    return contains(geoLoc.getLatitude(), geoLoc.getLongitude());
//  }
//
//  public double getX1() { return left; }
//
//  public double getY1() { return top; }
//
//  public double getX2() { return right; }
//
//  public double getY2() { return bottom; }
//
////  @Getter(lazy = true)
////  private
//  final double minX = min(getX1(), getX2());
////  @Getter(lazy = true)
////  private
//  final double minY = min(getY1(), getY2());
////  @Getter(lazy = true)
////  private
//  final double maxX = max(getX1(), getX2());
////  @Getter(lazy = true)
////  private
//  final double maxY = max(getY1(), getY2());
//
//  @Getter(lazy = true)
//  private final double meanX = (getMinX() + getMaxX()) / 2.0;
//  @Getter(lazy = true)
//  private final double meanY = (getMinY() + getMaxY()) / 2.0;
//
//  @Getter(lazy = true)
//  private final double diffX = getMaxX() - getMinX();
//  @Getter(lazy = true)
//  private final double diffY = getMaxY() - getMinY();
//
//
//  public boolean isInRange(Point2D.Double point) {
//    return isInRange(point.x, point.y);
//  }
//
//  public boolean isInRange(double x, double y) {
//    return x >= getMinX() &&
//           x <= getMaxX() &&
//           y >= getMinY() &&
//           y <= getMaxY();
//  }
//
//  public boolean isInRange(double v, double min, double max) {
//    return v >= min && v <= max;
//  }
//
//  /**
//   * 현 BoundingBox 가 대상 bbox 와 overlap 되는지 판단한다.
//   *
//   * @param bbox overlap 을 조사할 대상
//   */
//  public boolean isOverlap(@NonNull BoundingBox bbox) {
//    return isInRange(bbox.getMinX(), bbox.getMinY()) ||
//           isInRange(bbox.getMaxX(), bbox.getMaxY()) ||
//           isInRange(bbox.getMinX(), bbox.getMaxY()) ||
//           isInRange(bbox.getMaxX(), bbox.getMinY());
//  }
//
//  /**
//   * 위경도 위치에 대한 정렬 시에 사용합니다. Left, Top 순으로 값을 비교하고, 둘 다 같다면 면적을 비교합니다.
//   *
//   * @param that 비교할 BoundingBox
//   * @return 두 Bounding Box의 순서를 정할 값
//   */
//  @Override
//  public int compareTo(@NonNull BoundingBox that) {
//    int result = (int) Math.floor(left - that.left);
//    if (result == 0) {
//      result = (int) Math.floor(top - that.top);
//    }
//    if (result == 0) {
//      result = (int) (getWidth() * getHeight() - that.getWidth() * that.getHeight());
//    }
//    return result;
//  }
//
//  @Override
//  public boolean equals(Object obj) {
//    if (obj != null && obj instanceof BoundingBox) {
//      BoundingBox that = (BoundingBox) obj;
//      return this.left == that.left &&
//             this.right == that.right &&
//             this.top == that.top &&
//             this.bottom == that.bottom;
//    }
//    return false;
//  }
//
//  @Override
//  public int hashCode() {
//    return Hashx.compute(left, top, right, bottom);
//  }
//
//  @Override
//  public ToStringHelper buildStringHelper() {
//    return super.buildStringHelper()
//                .add("left", left)
//                .add("top", top)
//                .add("right", right)
//                .add("bottom", bottom);
//  }
//
//  private static final long serialVersionUID = 7699530938443271602L;
//
//}
