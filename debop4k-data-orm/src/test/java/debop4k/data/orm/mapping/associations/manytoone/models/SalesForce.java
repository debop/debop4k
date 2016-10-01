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

@Entity(name = "ManyToOne_SalesForce")
@Getter
@Setter
@NoArgsConstructor
public class SalesForce extends IntEntity {

  public SalesForce(String corporation) {
    this.corporation = corporation;
  }

  private String corporation;

  @OneToMany(mappedBy = "salesForce", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.EXTRA)
  Set<SalesGuy> salesGuys = new HashSet<SalesGuy>();

  public void addGuys(SalesGuy... guys) {
    for (SalesGuy guy : guys) {
      salesGuys.add(guy);
      guy.setSalesForce(this);
    }
  }

  public void removeGuys(SalesGuy... guys) {
    for (SalesGuy guy : guys) {
      salesGuys.remove(guy);
      guy.setSalesForce(null);
    }
  }

  @Override
  public int hashCode() {
    return Hashx.compute(corporation);
  }

  private static final long serialVersionUID = -8707481913466118682L;
}
