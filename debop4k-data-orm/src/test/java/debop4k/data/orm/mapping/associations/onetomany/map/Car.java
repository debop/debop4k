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

package debop4k.data.orm.mapping.associations.onetomany.map;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.collections.impl.factory.Maps;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Map;

@Entity(name = "OneToManyList_Car")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Car extends IntEntity {

  public Car(String name) {
    this.name = name;
  }

  private String name;

  @CollectionTable(name = "OneToManyMap_Car_Options", joinColumns = {@JoinColumn(name = "carId")})
  @MapKeyClass(String.class)
  @MapKeyColumn(name = "optionKey", length = 128)
  @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
  Map<String, String> options = Maps.mutable.of();

  @CollectionTable(name = "OneToManyMap_Car_CarOptions", joinColumns = {@JoinColumn(name = "carId")})
  @MapKeyClass(String.class)
  @MapKeyColumn(name = "optionKey", length = 128)
  @ElementCollection(targetClass = CarOption.class, fetch = FetchType.EAGER)
  Map<String, CarOption> carOptions = Maps.mutable.of();

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = -9137697578523314209L;
}
