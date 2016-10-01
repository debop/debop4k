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

package debop4k.data.orm.jpa;

import debop4k.data.orm.mapping.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@Transactional
public class JpaExTest extends AbstractJpaTest {

  @PersistenceContext EntityManager em;

  @Test
  public void currentConnection() throws Exception {
    assertThat(em).isNotNull();

    Connection connection = JpaEx.currentConnection(em);
    assertThat(connection).isNotNull();
    assertThat(connection.isReadOnly()).isFalse();
  }

  @Test
  @Transactional
  public void withReadOnlyEntityManager() {
    Employee emp = createEmployee();

    em.persist(emp);
    em.flush();

    // NOTE: @Transactional(readOnly=true) 를 사용해야 합니다.
    // Employee loaded = JpaEx.withReadOnly(em, em -> em.find(Employee.class, emp.getId()));
    Employee loaded = loadEmployee(emp.getId());
    assertThat(loaded).isEqualTo(emp);
  }

  @Transactional(readOnly = true)
  private Employee loadEmployee(Integer empId) {
    return em.find(Employee.class, empId);
  }
}
