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

import debop4k.data.orm.model.AbstractHibernateEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.eclipse.collections.impl.factory.SortedMaps;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity(name = "Join_User")
@Cache(region = "associations", usage = CacheConcurrencyStrategy.READ_WRITE)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@ToString
public class User extends AbstractHibernateEntity<Integer> {

  @Id
  @GeneratedValue
  @Column(name = "userId")
  Integer id;

  String name;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
  @JoinTable(name = "JoinUserAddressMap",
             joinColumns = {@JoinColumn(name = "userId")},
             inverseJoinColumns = {@JoinColumn(name = "addressId")})
  @MapKeyColumn(name = "address_name")
  @ElementCollection(targetClass = AddressEntity.class, fetch = FetchType.EAGER)
  @Fetch(FetchMode.JOIN)
  Map<String, AddressEntity> addresses = SortedMaps.mutable.of();

  @JoinTable(name = "JoinUserNicknameSet", joinColumns = {@JoinColumn(name = "userId")})
  @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
  @Cascade(org.hibernate.annotations.CascadeType.ALL)
  Set<String> nicknames = new LinkedHashSet<String>();

  private static final long serialVersionUID = -892871535124703571L;
}
