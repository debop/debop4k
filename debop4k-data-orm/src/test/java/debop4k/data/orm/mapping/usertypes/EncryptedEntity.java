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

package debop4k.data.orm.mapping.usertypes;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class EncryptedEntity extends IntEntity {

  public EncryptedEntity(@NonNull String username, @NonNull String password) {
    this.username = username;
    this.password = password;
  }

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  @Type(type = "debop4k.data.orm.hibernate.usertypes.encrypt.RC2StringUserType")
  private String password;

  @Override
  public int hashCode() {
    return Hashx.compute(username);
  }

  private static final long serialVersionUID = 7481902280881713013L;
}
