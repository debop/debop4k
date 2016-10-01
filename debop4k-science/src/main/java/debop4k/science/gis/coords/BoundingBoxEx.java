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

import debop4k.core.utils.Objects;
import debop4k.science.gis.BoundingBoxRelation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * 경위도 좌표를 이용한 격자를 나타내는 {@link BoundingBox}를 위한 Helper class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class BoundingBoxEx {

  private BoundingBoxEx() {}

  /**
   * 전체 Bounding Box를 가로/세로 갯수만큼의 격자의 2차원 배열로 빌드합니다.
   * 주의할 점: 위도는 Top 이 Bottom 보다 크고, 경도는 Left 보다 Right 가 크다!!!
   *
   * @param bbox 대상 Bounding Box
   * @param rows row 격자 갯수
   * @param cols column 격자 갯수
   * @return 격자들의 2차원 배열
   */
  public static BoundingBox[][] makeGrid(BoundingBox bbox, int rows, int cols) {
    BoundingBox[][] cells = new BoundingBox[rows][cols];

    double cellWidth = bbox.getWidth() / cols;
    double cellHeight = bbox.getHeight() / rows;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        BoundingBox cell = BoundingBox.of(bbox.getLeft() + c * cellWidth,
                                          bbox.getTop() - r * cellHeight,
                                          bbox.getLeft() + (c + 1) * cellWidth,
                                          bbox.getTop() - (r + 1) * cellHeight);
        cells[r][c] = cell;
      }
    }
    return cells;
  }

  /**
   * Bounding Box 의 4개의 꼭지점을 Point 컬렉션으로 반환한다.
   *
   * @param bbox BoundingBox 인스턴스
   * @return Point 컬렉션
   */
  public static MutableList<Point2D.Double> getPoints(BoundingBox bbox) {
    return FastList.newListWith(new Point2D.Double(bbox.getLeft(), bbox.getTop()),
                                new Point2D.Double(bbox.getRight(), bbox.getTop()),
                                new Point2D.Double(bbox.getRight(), bbox.getBottom()),
                                new Point2D.Double(bbox.getLeft(), bbox.getBottom()));
  }

  /**
   * BoundingBox 의 4 꼭지점의 좌표를 1차원 배열 형태로 반환합니다. (X, Y 값 순으로 저장됩니다)
   *
   * @param bbox BoundingBox 인스턴스
   * @return Point x,y 값의 배열
   */
  public static DoubleArrayList getPointDoubleArray(@NonNull BoundingBox bbox) {
    DoubleArrayList array = new DoubleArrayList();

    for (Point2D.Double point : getPoints(bbox)) {
      array.add(point.getX());
      array.add(point.getY());
    }
    return array;
  }

  public static BoundingBox getBbox(@NonNull List<Point2D.Double> points) {

    double left = Double.POSITIVE_INFINITY;
    double top = Double.NEGATIVE_INFINITY;
    double right = Double.NEGATIVE_INFINITY;
    double bottom = Double.POSITIVE_INFINITY;

    for (Point2D.Double point : points) {
      if (left > point.x) left = point.x;
      if (top < point.y) top = point.y;
      if (right < point.x) right = point.x;
      if (bottom > point.y) bottom = point.y;
    }

    return BoundingBox.of(left, top, right, bottom);
  }


  /**
   * Bounding Box 의 4개의 꼭지점을 {@link GeoLocation} 컬렉션으로 반환한다.
   *
   * @param bbox BoundingBox 인스턴스
   * @return {@link GeoLocation} 컬렉션
   */
  public static MutableList<GeoLocation> getLocations(BoundingBox bbox) {
    return FastList.newListWith(GeoLocation.of(bbox.getTop(), bbox.getLeft()),
                                GeoLocation.of(bbox.getTop(), bbox.getRight()),
                                GeoLocation.of(bbox.getBottom(), bbox.getRight()),
                                GeoLocation.of(bbox.getBottom(), bbox.getLeft()));
  }

  /**
   * Bounding Box를 {@link Rectangle2D.Double} 로 변환한다.
   *
   * @param bbox BoundingBox 인스턴스
   * @return Rectangle 인스턴스
   */
  public static Rectangle2D.Double toRectangle(BoundingBox bbox) {
    // HINT: Rectangle2D 와 경위도를 나타내는 BoundingBox는 y 좌표계가 반대이다!!!
    return new Rectangle2D.Double(bbox.getLeft(), bbox.getBottom(), bbox.getWidth(), bbox.getHeight());
  }

  /**
   * bbox1 과 bbox2 영역의 관계를 나타낸다.
   * bbox1 을 기준으로 bbox2 를 '포함', '겹침', '포함됨' 으로 표현할 수 있다.
   *
   * @param bbox1 검사할 bounding box
   * @param bbox2 검사 대상 bounding box
   * @return 두 영영의 관계
   */
  public static BoundingBoxRelation getRelation(final BoundingBox bbox1, final BoundingBox bbox2) {
    if (exactMatchingWith(bbox1, bbox2)) {
      return BoundingBoxRelation.ExactMatch;
    }

    MutableList<GeoLocation> locations2 = getLocations(bbox2);

    //boolean containsAll = locations2.stream().allMatch(p -> containsWith(bbox1, p));
    boolean containsAll = locations2.allSatisfy(new Predicate<GeoLocation>() {
      @Override
      public boolean accept(GeoLocation each) {
        return containsWith(bbox1, each);
      }
    });
    if (containsAll) {
      return BoundingBoxRelation.Contain;
    }

    //boolean containsAny = locations2.stream().anyMatch(p -> containsWith(bbox1, p));
    boolean containsAny = locations2.anySatisfy(new Predicate<GeoLocation>() {
      @Override
      public boolean accept(GeoLocation each) {
        return containsWith(bbox1, each);
      }
    });
    if (containsAny) {
      return BoundingBoxRelation.Intersection;
    }

    MutableList<GeoLocation> locations1 = getLocations(bbox1);

    // boolean containsAll2 = locations1.stream().allMatch(p -> containsWith(bbox2, p));
    boolean containsAll2 = locations1.allSatisfy(new Predicate<GeoLocation>() {
      @Override
      public boolean accept(GeoLocation each) {
        return containsWith(bbox2, each);
      }
    });

    if (containsAll2) {
      return BoundingBoxRelation.Include;
    }

    return BoundingBoxRelation.None;
  }

  /**
   * 두 영역이 정확히 일치하는지 여부
   *
   * @param bbox1 영역 1
   * @param bbox2 영역 2
   * @return 두 영역이 정확히 일치하는지 여부
   */
  public static boolean exactMatchingWith(@NonNull BoundingBox bbox1, @NonNull BoundingBox bbox2) {
    MutableList<Point2D.Double> points1 = getPoints(bbox1);
    MutableList<Point2D.Double> points2 = getPoints(bbox2);

    for (int i = 0; i < points1.size(); i++) {
      if (!Objects.equals(points1.get(i), points2.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * bbox1 이 bbox2에 포함되는지 여부 (bbox1 &lt;= bbox2)
   *
   * @param bbox1 검사할 bounding box
   * @param bbox2 검사 대상 bounding box
   * @return bbox1이 bbox2의 내부에 있거나 정확히 일치하면 true, 아니면 false 를 반환
   */
  public static boolean includeWith(@NonNull BoundingBox bbox1, @NonNull BoundingBox bbox2) {
    BoundingBoxRelation relation = getRelation(bbox1, bbox2);

    return relation == BoundingBoxRelation.ExactMatch ||
           relation == BoundingBoxRelation.Include;
  }

  /**
   * bbox1과 bbox2의 영역이 겹치는지 여부 (교집합 영역이 존재하면 true, 전혀 상관없는 영역이면 false)
   * BoundingBoxRelation.None 이 아니면 참
   *
   * @param bbox1 검사할 bounding box
   * @param bbox2 검사 대상 bounding box
   * @return (교집합 영역이 존재하면 true, 전혀 상관없는 영역이면 false)
   */
  public static boolean overlapWith(@NonNull BoundingBox bbox1, @NonNull BoundingBox bbox2) {
    return getRelation(bbox1, bbox2) != BoundingBoxRelation.None;
  }

  /**
   * 두 영역이 교차하는지 여부 (포함이나 ExactMatching 인 경우는 제외) (XOR 영역이 존재해야 한다)
   *
   * @param bbox1 검사할 bounding box
   * @param bbox2 검사 대상 bounding box
   * @return 두 영역이 포함관계나 일치하지 않으면서, 영역을 공유할 경우에 true 를 반환
   */
  public static boolean intersectWith(@NonNull BoundingBox bbox1, @NonNull BoundingBox bbox2) {
    return getRelation(bbox1, bbox2) == BoundingBoxRelation.Intersection;
  }

  /**
   * bbox1 영역이 bbox2 영역 전체를 포함하는지 여부  (bbox1 &gt;= bbox2)
   *
   * @param bbox1 검사할 bounding box
   * @param bbox2 검사 대상 bounding box
   * @return bbox1 영역에 bbox2 영역 전체가 포함되면 true, 아니면 false
   */
  public static boolean containsWith(@NonNull BoundingBox bbox1, @NonNull BoundingBox bbox2) {
    BoundingBoxRelation relation = getRelation(bbox1, bbox2);

    return relation == BoundingBoxRelation.ExactMatch ||
           relation == BoundingBoxRelation.Contain;
  }

  /**
   * bbox 영역에 지정한 point 가 포함되는지 여부 (Left, Top 값은 include, Right Bottom 값은 exlucde 이다)
   *
   * @param bbox1 영역
   * @param point 검사할 point
   * @return 영역에 포함되는 point 이면 true, 아니면 false
   */
  public static boolean containsWith(@NonNull BoundingBox bbox1, Point2D.Double point) {

    boolean xinclude = bbox1.getLeft() <= point.x && point.x < bbox1.getRight();
    boolean yinclude = bbox1.getBottom() < point.y && point.y <= bbox1.getTop();

    return xinclude && yinclude;
  }

  /**
   * bbox 영역에 지정한 경위도가 포함되는지 여부 (Left, Top 값은 include, Right Bottom 값은 exlucde 이다)
   *
   * @param bbox1    영역
   * @param location 검사할 경위도
   * @return 영역에 포함되는 경위도 이면 true, 아니면 false
   */
  public static boolean containsWith(@NonNull BoundingBox bbox1, GeoLocation location) {
    boolean xinclude = bbox1.getLeft() <= location.getLongitude() && location.getLongitude() < bbox1.getRight();
    boolean yinclude = bbox1.getBottom() < location.getLatitude() && location.getLatitude() < bbox1.getTop();

    return xinclude && yinclude;
  }


}
