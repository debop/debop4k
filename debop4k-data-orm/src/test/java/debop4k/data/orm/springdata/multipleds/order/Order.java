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

package debop4k.data.orm.springdata.multipleds.order;

import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

@Entity(name = "SimpleOrder")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Order extends IntEntity {

  public Order(@NonNull String no, Integer customerId) {
    this.no = no;
    this.customerId = customerId;
  }

  private String no;
  private Integer customerId;

  @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
  @LazyCollection(LazyCollectionOption.EXTRA)
  private final List<LineItem> lineItems = FastList.newList();


  public void addLineItem(@NonNull LineItem lineItem) {
    this.lineItems.add(lineItem);
  }

  @Override
  public int hashCode() {
    return Hashx.compute(no);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("no", no)
                .add("customerId", customerId);
  }

  private static final long serialVersionUID = -125654853825802405L;
}
