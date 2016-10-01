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

import javax.persistence.*;
import java.util.List;

@Entity(name = "OneToMany_Father")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Father extends IntEntity {

  public Father(String name) {
    this.name = name;
  }

  private String name;

  // mappedBy 가 없으므로, update 시에 children 들을 모두 삭제한 후 새로운 collection으로 저장합니다.
  // hibernate 에서는 inverse = false 에 해당합니다.
  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
  @JoinTable(name = "OneToMany_Father_Child")
  @OrderColumn(name = "birthday")
  @LazyCollection(LazyCollectionOption.EXTRA)
  List<Child> orderedChildren = FastList.newList();


  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = -8989943497456334753L;
}
