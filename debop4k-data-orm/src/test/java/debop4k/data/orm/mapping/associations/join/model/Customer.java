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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "Join_Customer")
@Cache(region = "associations", usage = CacheConcurrencyStrategy.READ_WRITE)
@SecondaryTable(name = "JoinCustomerAddress", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "customerId")})
@Getter
@Setter
@ToString
public class Customer extends AbstractHibernateEntity<Integer> {

  @Id
  @GeneratedValue
  @Column(name = "customerId")
  Integer id;

  String name;
  String email;

  @Embedded @AttributeOverrides({
                                    @AttributeOverride(name = "street", column = @Column(name = "street", table = "JoinCustomerAddress")),
                                    @AttributeOverride(name = "city", column = @Column(name = "city", table = "JoinCustomerAddress")),
                                    @AttributeOverride(name = "zipcode", column = @Column(name = "zipcode", table = "JoinCustomerAddress")),
                                })
  Address address = new Address();


  @CreatedDate
  Date createdAt;

  @LastModifiedDate
  Date lastModifiedAt;

  @Override
  public int hashCode() {
    return Hashx.compute(name, email);
  }

  private static final long serialVersionUID = 7169746850817753252L;
}
