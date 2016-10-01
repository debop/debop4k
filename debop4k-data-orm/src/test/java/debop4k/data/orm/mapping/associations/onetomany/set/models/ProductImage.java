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

package debop4k.data.orm.mapping.associations.onetomany.set.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Parent;

import javax.persistence.Embeddable;

@Embeddable
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class ProductImage extends AbstractValueObject {

  public ProductImage(String name) {
    this.name = name;
  }

  // OneToOne 이나 @ManyToOne 에 많이 쓰인다. Primary Key 로 쓰인다는 뜻이다.
  @Parent
  private ProductItem item;

  private String name;
  private String filename;
  private Integer sizeX;
  private Integer sizeY;

  @Override
  public int hashCode() {
    return Hashx.compute(name, filename);
  }

  private static final long serialVersionUID = 5610268573587209644L;
}
