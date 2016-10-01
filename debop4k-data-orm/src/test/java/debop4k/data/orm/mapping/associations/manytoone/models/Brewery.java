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

package debop4k.data.orm.mapping.associations.manytoone.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ManyToOne_Brewery")
@Getter
@Setter
@NoArgsConstructor
public class Brewery extends IntEntity {

  public Brewery(String name) {
    this.name = name;
  }

  private String name;

  @OneToMany(mappedBy = "brewery", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @LazyCollection(LazyCollectionOption.EXTRA)
  Set<Beer> beers = new HashSet<Beer>();

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 7066863736285356521L;
}
