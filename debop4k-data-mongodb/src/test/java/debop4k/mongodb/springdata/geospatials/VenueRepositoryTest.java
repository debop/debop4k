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

package debop4k.mongodb.springdata.geospatials;

import debop4k.mongodb.AbstractMongoTest;
import debop4k.mongodb.springdata.SpringDataMongoConfiguration;
import debop4k.mongondb.geometry.MongoGeometry;
import debop4k.science.gis.coords.BoundingBox;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Box;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.List;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SpringDataMongoConfiguration.class})
public class VenueRepositoryTest extends AbstractMongoTest {

  @Inject VenueRepository repo;
  @Inject MongoTemplate template;

  @Before
  public void setup() {
    repo.deleteAll();

    repo.insert(Venue.of("all", BoundingBox.of(0, 0, 10, 10)));
    repo.insert(Venue.of("Q1", BoundingBox.of(5, 5, 10, 10)));
    repo.insert(Venue.of("Q2", BoundingBox.of(0, 5, 5, 10)));
    repo.insert(Venue.of("Q3", BoundingBox.of(0, 0, 5, 5)));
    repo.insert(Venue.of("Q4", BoundingBox.of(5, 0, 10, 5)));
  }

  @Test
  public void allWithin() {
    Box large = MongoGeometry.toBox(BoundingBox.of(-10, -10, 20, 20));
    testLocationWithin(large, 5);
    testBboxWithin(large, 5);

    Box intersect = MongoGeometry.toBox(BoundingBox.of(-4, -4, 6, 6));
    testLocationWithin(intersect, 5);  // Q1,Q2,Q3,Q4는 (5,5) 가 다 포함되고, All 은 (0,0)이 포함된다.
    testBboxWithin(intersect, 2);  // Q3, Q4

    Box intersect2 = MongoGeometry.toBox(BoundingBox.of(4, -2, 6, 2));
    testLocationWithin(intersect2, 2);  // Q3, Q4
    testBboxWithin(intersect2, 0);  // Q3

    Box other = MongoGeometry.toBox(BoundingBox.of(-10, -10, -20, -20));
    testLocationWithin(other, 0);
    testBboxWithin(other, 0);
  }

  @Test
  public void containsOnlyQ1() {
    Box box1 = MongoGeometry.toBox(BoundingBox.of(5, 5, 10, 10));
    testLocationWithin(box1, 5);  // (5,5) 가 모두 속해있다.
    testBboxWithin(box1, 2);  // q1, q4

    Box box2 = MongoGeometry.toBox(BoundingBox.of(4, 4, 11, 11));
    testLocationWithin(box2, 5);  // (5, 5)가 모두 속해 있다. all, a1
    testBboxWithin(box2, 2);   // q1, q4

    // box3 가 q1 에 속해 있어, 더 큰 놈은 못 찾는다.
    Box box3 = MongoGeometry.toBox(BoundingBox.of(6, 6, 9, 9));
    testLocationWithin(box3, 0);
    testBboxWithin(box3, 0);

  }

  private void testLocationWithin(Box box, int expectedSize) {
    List<Venue> venues = repo.findByPointsWithin(box);
    log.debug("Location: {}", venues);
    Assertions.assertThat(venues).hasSize(expectedSize);
  }

  private void testBboxWithin(Box box, int expectedSize) {
    List<Venue> venues = repo.findByBboxWithin(box);
    log.debug("BBox: {}", venues);
    Assertions.assertThat(venues).hasSize(expectedSize);
  }
}
