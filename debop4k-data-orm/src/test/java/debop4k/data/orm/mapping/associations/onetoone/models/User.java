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

package debop4k.data.orm.mapping.associations.onetoone.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * User
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 6.
 */
@Entity(name = "onetoone_user")
@Getter
@Setter
public class User extends IntEntity {

  private String firstname;
  private String lastname;
  private String username;
  private String password;
  private String email;
  private String ranking;
  private String admin;

  @OneToOne
  @JoinColumn(name = "shippingAddressId")
  Address shippingAddress = new Address();

  @Override
  public int hashCode() {
    return Hashx.compute(username, email);
  }

  private static final long serialVersionUID = -1500450269526600885L;
}
