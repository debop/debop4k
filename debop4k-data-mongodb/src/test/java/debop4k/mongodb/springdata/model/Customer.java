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

package debop4k.mongodb.springdata.model;

import debop4k.core.ToStringHelper;
import debop4k.mongodb.AbstractMongoDocument;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Document
@CompoundIndex(name = "ix_customer_name", def = "{ 'firstname': 1, 'lastname': 1}", background = true)
@Getter
@Setter
public class Customer extends AbstractMongoDocument {

  private String firstname;
  private String lastname;

  @Field("Email")
  @Indexed(unique = true)
  private EmailAddress emailAddress;

  private final Set<Address> addresses = new HashSet<Address>();

  public Customer() {}

  public Customer(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public void add(@NonNull Address addr) {
    addresses.add(addr);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("firstname", firstname)
                .add("lastname", lastname)
                .add("emailAddress", emailAddress)
                .add("addresses", addresses);
  }

  private static final long serialVersionUID = -2293608468177033704L;
}
