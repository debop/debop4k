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

package debop4k.data.orm.springdata.jpa21;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;

@Entity
@NamedStoredProcedureQuery(
    name = "User.plus1",
    procedureName = "plus1inout",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "arg", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.OUT, name = "res", type = Integer.class)
    })
@NoArgsConstructor
public class User extends IntEntity {

  public User(@NonNull String name) {
    this.name = name;
  }

  private String name;

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 5246734636337087811L;
}
