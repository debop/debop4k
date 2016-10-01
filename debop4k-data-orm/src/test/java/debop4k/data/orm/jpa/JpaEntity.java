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

package debop4k.data.orm.jpa;

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity

public class JpaEntity extends IntEntity {

  public JpaEntity() {}

  public JpaEntity(@NonNull String name) {
    this.name = name;
  }

  @Column(name = "entityName", nullable = false, length = 32)
  private String name;


  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("name", name);
  }

  private static final long serialVersionUID = -6490840306618162066L;
}
