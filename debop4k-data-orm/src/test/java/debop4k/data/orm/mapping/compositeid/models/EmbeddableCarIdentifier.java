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

package debop4k.data.orm.mapping.compositeid.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.utils.Hashx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmbeddableCarIdentifier extends AbstractValueObject {


  @Column(nullable = false, length = 32)
  private String brand;

  @Column(nullable = false)
  private int year;


  @Override
  public int hashCode() {
    return Hashx.compute(brand, year);
  }

  private static final long serialVersionUID = 8055191593561034526L;
}
