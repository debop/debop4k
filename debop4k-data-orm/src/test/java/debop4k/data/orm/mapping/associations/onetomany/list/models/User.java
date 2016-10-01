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

package debop4k.data.orm.mapping.associations.onetomany.list.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.collections.impl.factory.Maps;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity(name = "OneToMany_User")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class User extends IntEntity {

  public User(String name) {
    this.name = name;
  }

  private String name;

  @OneToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "OneToMany_User_Address")
  @MapKeyColumn(name = "address_name")
  @ElementCollection(targetClass = Address.class, fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  Map<String, Address> addresses = Maps.mutable.of();

  @ElementCollection
  @JoinTable(name = "OneToMany_User_Nicks", joinColumns = {@JoinColumn(name = "userId")})
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  Set<String> nicknames = new HashSet<String>();

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = -9040250813101140389L;
}
