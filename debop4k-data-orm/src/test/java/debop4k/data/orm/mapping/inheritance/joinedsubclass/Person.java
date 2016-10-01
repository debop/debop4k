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

package debop4k.data.orm.mapping.inheritance.joinedsubclass;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity(name = "JoinedSubclass_Person")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {
    @Index(name = "ix_joined_subclass_person_name", columnList = "personName, ssn")
})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person extends IntEntity {

  @Column(name = "personName", nullable = false, length = 128)
  private String name;

  @Column(nullable = false, length = 128)
  @NonNull private String ssn;

  @Override
  public int hashCode() {
    return Hashx.compute(name, ssn);
  }

  private static final long serialVersionUID = 3223033422058158442L;
}
