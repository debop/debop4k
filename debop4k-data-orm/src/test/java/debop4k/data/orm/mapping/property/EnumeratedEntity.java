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

package debop4k.data.orm.mapping.property;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class EnumeratedEntity extends IntEntity {

  public EnumeratedEntity(OrdinalEnum ordinalValue, StringEnum stringValue) {
    this.ordinalValue = ordinalValue;
    this.stringValue = stringValue;
  }

  @Enumerated(EnumType.ORDINAL)
  private OrdinalEnum ordinalValue;

  @Enumerated(EnumType.STRING)
  private StringEnum stringValue;

  @Override
  public int hashCode() {
    return Hashx.compute(ordinalValue, stringValue);
  }

  private static final long serialVersionUID = 3646676223002363414L;
}
