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

import debop4k.core.ToStringHelper;
import debop4k.gis.coords.BoundingBox;
import debop4k.gis.coords.BoundingBoxEx;
import debop4k.mongodb.AbstractMongoDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Document(collection = "Venue")
@Getter
@Setter
@NoArgsConstructor
public class Venue extends AbstractMongoDocument {

  public static Venue of(String name, BoundingBox bbox) {
    return new Venue(name, bbox);
  }

  private String name;

  @GeoSpatialIndexed(name = "ix_venue_center")
  private List<Point2D.Double> points;

  @GeoSpatialIndexed(name = "ix_venue_bbox")
  private BoundingBox bbox;

  public Venue(@NonNull String name, @NonNull BoundingBox bbox) {
    super();
    this.name = name;
    this.bbox = bbox;

    this.points = BoundingBoxEx.getPoints(bbox);
  }


  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("name", name)
                .add("points", points)
                .add("bbox", bbox);
  }

  private static final long serialVersionUID = 527900620093791890L;
}
