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

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.science.gis.coords.BoundingBox;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Document(collection = "GeoJsonStore")
@Getter
@Setter
@NoArgsConstructor
public class Store extends AbstractValueObject {

  @PersistenceConstructor
  public Store(String name, String street, String city, GeoJsonPoint location, Point point, Box cell, BoundingBox bbox) {
    this.name = name;
    this.street = street;
    this.city = city;
    this.location = location;
    this.point = point;
    this.cell = cell;
    this.bbox = bbox;
  }


  @Id
  private String id;
  private String name;
  private String street;
  private String city;

  @Indexed
  private GeoJsonPoint location;


  public void setLocation(GeoJsonPoint point) {
    this.location = point;
    this.point = new Point(point.getX(), point.getY());

    this.cell = new Box(new Point(point.getX(), point.getY()),
                        new Point(point.getX() + 0.01, point.getY() + 0.01));

    this.bbox = BoundingBox.of(cell.getFirst().getX(), cell.getFirst().getY(),
                               cell.getSecond().getX(), cell.getSecond().getY());
  }

  @GeoSpatialIndexed
  private Point point;

  @GeoSpatialIndexed
  private Box cell;

  @GeoSpatialIndexed
  private BoundingBox bbox;

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("id", id)
                .add("name", name)
                .add("street", street)
                .add("city", city)
                .add("location", location);
  }

  private static final long serialVersionUID = 4423227241428404675L;
}
