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
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Item
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 6.
 */
@Entity(name = "OneToOne_Item")
@NoArgsConstructor
@Getter
@Setter
public class Item extends IntEntity {

  public Item(String name) {
    this.name = name;
  }

  private String name;
  private String description;
  private BigDecimal initialPrice;

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 3546651609074930061L;
}
