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

package debop4k.data.orm.mapping.associations.join.model;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.AbstractHibernateEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Join_Address_Entity")
@Cache(region = "associations", usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@ToString
public class AddressEntity extends AbstractHibernateEntity<Integer> {

  @Id
  @GeneratedValue
  Integer id;

  String street;
  String city;
  String zipcode;

  @Override
  public int hashCode() {
    return Hashx.compute(street, city, zipcode);
  }

  private static final long serialVersionUID = -5579234087375374313L;
}
