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

package debop4k.data.orm.mapping.embeddable;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "Embeddable_User")
@Table(indexes = {
    @Index(name = "idx_embeddable_user_name", columnList = "username, password"),
    @Index(name = "idx_embeddable_user_email", columnList = "email")
})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class User extends IntEntity {

  private String firstname;
  private String lastname;

  @Column(length = 128, nullable = false)
  String username;

  @Column(length = 64, nullable = false)
  private String password;

  @Column(length = 128, nullable = false)
  private String email;

  private Boolean active = true;

  @Embedded
  @AttributeOverrides({
                          @AttributeOverride(name = "street", column = @Column(name = "HomeStreet", length = 128)),
                          @AttributeOverride(name = "zipcode", column = @Column(name = "HomeZipCode", length = 24)),
                          @AttributeOverride(name = "city", column = @Column(name = "HomeCity", length = 128)),
                      })
  private Address homeAddress = new Address();

  @Embedded
  @AttributeOverrides({
                          @AttributeOverride(name = "street", column = @Column(name = "OfficeStreet", length = 128)),
                          @AttributeOverride(name = "zipcode", column = @Column(name = "OfficeZipCode", length = 24)),
                          @AttributeOverride(name = "city", column = @Column(name = "OfficeCity", length = 128)),
                      })
  private Address officeAddress = new Address();


  @Override
  public int hashCode() {
    return Hashx.compute(username);
  }

  private static final long serialVersionUID = 784293757374012704L;
}
