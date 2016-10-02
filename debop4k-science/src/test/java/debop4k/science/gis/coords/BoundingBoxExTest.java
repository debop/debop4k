///*
// * Copyright 2015-2020 KESTI s.r.o.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.science.gis.coords;
//
//import debop4k.core.utils.With;
//import debop4k.science.gis.AbstractGisTest;
//import debop4k.science.gis.BoundingBoxRelation;
//import lombok.extern.slf4j.Loggingx;
//import org.junit.Test;
//
//import java.awt.geom.Point2D;
//import java.util.List;
//
//import static debop4k.science.gis.coords.BoundingBox.*;
//import static org.assertj.core.api.Assertionx.assertThat;
//
///**
// * @author sunghyouk.bae@gmail.com
// */
//@Loggingx
//public class BoundingBoxExTest extends AbstractGisTest {
//
//  private static final int ROW_COUNT = 100;
//  private static final int COL_COUNT = 100;
//
//  @Test
//  public void convertBoundingBoxAndPoints() {
//    BoundingBox bbox = BoundingBox.of(0, 10, 100, 5);
//    List<Point2D.Double> points = BoundingBoxEx.getPoints(bbox);
//    BoundingBox bbox2 = BoundingBoxEx.getBbox(points);
//    assertThat(bbox2).isEqualTo(bbox);
//  }
//
//  @Test
//  public void testMakeGrid() {
//    final BoundingBox bbox = BoundingBox.of(0, 0, ROW_COUNT, COL_COUNT);
//
//    With.stopwatch("make grid",
//                   new Runnable() {
//                     @Override
//                     public void run() {
//                       BoundingBox[][] cells = makeGrid(bbox, ROW_COUNT, COL_COUNT);
//
//                       assertThat(cells.length).isEqualTo(ROW_COUNT);
//                       assertThat(cells[0].length).isEqualTo(COL_COUNT);
//
//                       assertThat(cells[0][0].getLeft()).isEqualTo(bbox.getLeft());
//                       assertThat(cells[0][0].getTop()).isEqualTo(bbox.getTop());
//                       assertThat(cells[ROW_COUNT - 1][COL_COUNT - 1].getRight()).isEqualTo(bbox.getRight());
//                       assertThat(cells[ROW_COUNT - 1][COL_COUNT - 1].getBottom()).isEqualTo(bbox.getBottom());
//                     }
//                   });
//  }
//
//
//  @Test
//  public void testMakeGridAsDouble() {
//
//    int rowCount = 253;
//    int colCount = 149;
//    BoundingBox bbox = BoundingBox.of(123.3102, 43.3935, 131.6423, 31.6518);
//
//    BoundingBox[][] cells = makeGrid(bbox, rowCount, colCount);
//
//    assertThat(cells.length).isEqualTo(rowCount);
//    assertThat(cells[0].length).isEqualTo(colCount);
//
//    assertThat(cells[0][0].getLeft()).isEqualTo(bbox.getLeft());
//    assertThat(cells[0][0].getTop()).isEqualTo(bbox.getTop());
//    assertThat(cells[rowCount - 1][colCount - 1].getRight()).isEqualTo(bbox.getRight());
//    assertThat(cells[rowCount - 1][colCount - 1].getBottom()).isEqualTo(bbox.getBottom());
//  }
//
//
//  @Test
//  public void testRelation() {
//    BoundingBox bbox1 = BoundingBox.of(0, 100, 100, 0);
//
//    BoundingBox bboxNone = BoundingBox.of(-100, -100, -50, -200);
//    BoundingBox bboxMatch = BoundingBox.of(bbox1);
//    BoundingBox bboxContained = BoundingBox.of(50, 50, 60, 10);
//    BoundingBox bboxIncluded = BoundingBox.of(-100, 200, 200, -100);
//    BoundingBox bboxIntersect = BoundingBox.of(50, 150, 150, 50);
//
//    assertThat(getRelation(bbox1, bboxNone)).isEqualTo(BoundingBoxRelation.None);
//    assertThat(getRelation(bbox1, bboxMatch)).isEqualTo(BoundingBoxRelation.ExactMatch);
//    assertThat(getRelation(bbox1, bboxContained)).isEqualTo(BoundingBoxRelation.Contain);
//    assertThat(getRelation(bbox1, bboxIncluded)).isEqualTo(BoundingBoxRelation.Include);
//    assertThat(getRelation(bbox1, bboxIntersect)).isEqualTo(BoundingBoxRelation.Intersection);
//
//    assertThat(overlapWith(bbox1, bboxNone)).isFalse();
//    assertThat(overlapWith(bbox1, bboxMatch)).isTrue();
//    assertThat(overlapWith(bbox1, bboxContained)).isTrue();
//    assertThat(overlapWith(bbox1, bboxIncluded)).isTrue();
//    assertThat(overlapWith(bbox1, bboxIntersect)).isTrue();
//
//    assertThat(exactMatchingWith(bbox1, bboxNone)).isFalse();
//    assertThat(exactMatchingWith(bbox1, bboxMatch)).isTrue();
//    assertThat(exactMatchingWith(bbox1, bboxContained)).isFalse();
//    assertThat(exactMatchingWith(bbox1, bboxIncluded)).isFalse();
//    assertThat(exactMatchingWith(bbox1, bboxIntersect)).isFalse();
//
//    assertThat(containsWith(bbox1, bboxNone)).isFalse();
//    assertThat(containsWith(bbox1, bboxMatch)).isTrue();
//    assertThat(containsWith(bbox1, bboxContained)).isTrue();
//    assertThat(containsWith(bbox1, bboxIncluded)).isFalse();
//    assertThat(containsWith(bbox1, bboxIntersect)).isFalse();
//
//
//    assertThat(includeWith(bbox1, bboxNone)).isFalse();
//    assertThat(includeWith(bbox1, bboxMatch)).isTrue();
//    assertThat(includeWith(bbox1, bboxContained)).isFalse();
//    assertThat(includeWith(bbox1, bboxIncluded)).isTrue();
//    assertThat(includeWith(bbox1, bboxIntersect)).isFalse();
//
//
//    assertThat(intersectWith(bbox1, bboxNone)).isFalse();
//    assertThat(intersectWith(bbox1, bboxMatch)).isFalse();
//    assertThat(intersectWith(bbox1, bboxContained)).isFalse();
//    assertThat(intersectWith(bbox1, bboxIncluded)).isFalse();
//    assertThat(intersectWith(bbox1, bboxIntersect)).isTrue();
//  }
//
//
//}
