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


import debop4k.data.orm.mapping.AbstractMappingTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 부모 클래스를 공통으로 사용하고, 자식 클래스들은 각각의 고유의 정보를 각자 테이블에 저장합니다.
 * subclass 와는 달리 자식 클래스들이 not null 속성을 가집니다.
 * 단 MySQL 같이 join 성능이 않좋은 RDBMS에서는 사용을 자제해야 합니다.
 */
@SpringBootTest(classes = {JoinedSubclassConfiguration.class})
public class JoinedSubclassTest extends AbstractMappingTest {

  @Test
  public void joinedSubclass() {
    Employee emp1 = new Employee("배성혁", "123456-1234567", "123456");
    Employee emp2 = new Employee("권미숙", "123456-1234567", "789012");

    emp1.getMembers().add(emp2);
    emp2.setManager(emp1);

    Customer customer = new Customer("고객", "111111-9999999", "010-9999-9999");
    customer.setContactEmployee(emp2);

    em.persist(emp1);
    em.persist(emp2);
    em.persist(customer);
    em.flush();
    em.clear();

    Customer customer1 = em.find(Customer.class, customer.getId());
    assertThat(customer1).isNotNull();
    assertThat(customer1.getContactEmployee()).isEqualTo(emp2);

    Employee employee1 = em.find(Employee.class, emp1.getId());
    assertThat(employee1).isNotNull();
    assertThat(employee1.getMembers()).hasSize(1).containsOnly(emp2);

    em.remove(customer1);
    em.remove(employee1);
    em.remove(employee1.getMembers().iterator().next());
    em.flush();

    Assertions.assertThat(em.find(Customer.class, customer.getId())).isNull();
    Assertions.assertThat(em.find(Employee.class, emp1.getId())).isNull();
  }
}
