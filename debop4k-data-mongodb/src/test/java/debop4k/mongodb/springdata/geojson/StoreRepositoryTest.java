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

package debop4k.mongodb.springdata.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import debop4k.core.json.Jsonx;
import debop4k.core.utils.Resources;
import debop4k.core.utils.With;
import debop4k.mongodb.springdata.SpringDataMongoConfigurationTest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class StoreRepositoryTest extends SpringDataMongoConfigurationTest {

  @Inject StoreRepository repository;
  @Inject MongoOperations operations;

  private static final GeoJsonPolygon GEO_JSON_POLYGON =
      new GeoJsonPolygon(new Point(-73.992514, 40.758934),
                         new Point(-73.961138, 40.760348),
                         new Point(-73.991658, 40.730006),
                         new Point(-73.992514, 40.758934));

  private static final Box BOX = new Box(new Point(-73.992514, 40.760348), new Point(-73.961138, 40.730006));

  @Before
  @SneakyThrows({IOException.class})
  public void setup() {
    repository.deleteAll();


    String jsonText = Resources.getString("starbucks-in-nyc.json");
    log.debug("jsonText={}", jsonText);

    // NOTE: GeoJson 을 파싱할 수 있도록 합니다.
    ObjectMapper mapper = Jsonx.getDefaultObjectMapper();
    mapper.addMixIn(GeoJsonPoint.class, GeoJsonPointMixin.class);

    List<Store> stores = mapper.readValue(jsonText, new TypeReference<List<Store>>() {});
    for (Store store : stores) {
      log.trace("store={}", store);
    }

    repository.insert(stores);
    log.debug("inserted n={}", stores.size());

  }

  static abstract class GeoJsonPointMixin {
    GeoJsonPointMixin(@JsonProperty("longitude") double x, @JsonProperty("latitude") double y) {}
  }

  @Test
  public void findWithinGeoJsonPolygon() {
    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        List<Store> stores = repository.findByLocationWithin(GEO_JSON_POLYGON);
        for (Store store : stores) {
          log.trace("store={}", store);
        }
        assertThat(stores.size()).isGreaterThan(0);
      }
    });
  }

  @Test
  public void findWithinBox() {
    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        List<Store> stores = repository.findByCellFirstWithin(BOX);
        for (Store store : stores) {
          log.trace("store={}", store);
        }
        assertThat(stores.size()).isGreaterThan(0);
      }
    });
  }

  @Test
  public void findByQueryWithinBox() {
    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        Query query = Query.query(Criteria.where("cell.first").within(BOX));
        List<Store> stores = operations.find(query, Store.class);
        for (Store store : stores) {
          log.trace("store={}", store);
        }
        assertThat(stores.size()).isGreaterThan(0);
      }
    });
  }

  @Test
  public void findByBoundingBoxWithinBox() {
    With.stopwatch(new Runnable() {
      @Override
      public void run() {
        Query query = Query.query(Criteria.where("bbox").within(BOX));
        List<Store> stores = operations.find(query, Store.class);
        for (Store store : stores) {
          log.trace("store={}", store);
        }
        assertThat(stores.size()).isGreaterThan(0);
      }
    });
  }

  @Test
  public void findStoresShtatIntersectGivenPolygon() {
    DBObject geoJsonDbo = new BasicDBObject();
    operations.getConverter().write(GEO_JSON_POLYGON, geoJsonDbo);

    BasicQuery bq = new BasicQuery(new BasicDBObject("location",
                                                     new BasicDBObject("$geoIntersects",
                                                                       new BasicDBObject("$geometry", geoJsonDbo))));
    List<Store> stores = operations.find(bq, Store.class);

    for (Store store : stores) {
      log.trace("store={}", store);
    }
    assertThat(stores.size()).isGreaterThan(0);
  }
}
