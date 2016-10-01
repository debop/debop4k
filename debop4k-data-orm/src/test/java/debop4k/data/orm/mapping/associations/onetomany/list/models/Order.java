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

package debop4k.data.orm.mapping.associations.onetomany.list.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity(name = "OneToMany_Order")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Order extends IntEntity {

  public Order(String no) {
    this.no = no;
  }

  private String no;

  // mappedBy 를 지정하면, OrderItem 하나마다 처리합니다.
  // hibernate 에서는 inverse = true 에 해당합니다.
  @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.EXTRA)
  List<OrderItem> items = FastList.newList();

  public void addItems(OrderItem... items) {
    for (OrderItem item : items) {
      this.items.add(item);
      item.setOrder(this);
    }
  }

  public void removeItems(OrderItem... items) {
    for (OrderItem item : items) {
      this.items.remove(item);
      item.setOrder(null);
    }
  }

  @Override
  public int hashCode() {
    return Hashx.compute(no);
  }

  private static final long serialVersionUID = 5393702613278969737L;
}
