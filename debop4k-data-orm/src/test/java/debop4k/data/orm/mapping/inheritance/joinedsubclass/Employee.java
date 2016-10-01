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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "JoinedSubclass_Employee")
@Table(indexes = {
    @Index(name = "ix_joined_subclass_employee_empNo", columnList = "empNo")
})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class Employee extends Person {

  public Employee(String name, String ssn, String empNo) {
    super(name, ssn);
    this.empNo = empNo;
  }

  @Column(nullable = false, length = 64)
  @NonNull private String empNo;

  @ManyToOne
  @JoinColumn(name = "managerId")
  private Employee manager;

  @OneToMany(mappedBy = "manager", cascade = {CascadeType.ALL})
  @LazyCollection(LazyCollectionOption.EXTRA)
  private Set<Employee> members = new HashSet<Employee>();

  @Override
  public int hashCode() {
    return Hashx.compute(super.hashCode(), empNo);
  }

  private static final long serialVersionUID = 5755204303014507012L;
}
