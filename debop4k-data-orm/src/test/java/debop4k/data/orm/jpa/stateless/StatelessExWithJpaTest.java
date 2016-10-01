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

package debop4k.data.orm.jpa.stateless;

import debop4k.data.orm.hibernate.StatelessEx;
import debop4k.data.orm.jpa.AbstractJpaTest;
import debop4k.data.orm.mapping.Employee;
import kotlin.jvm.functions.Function1;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Criteria;
import org.hibernate.internal.StatelessSessionImpl;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class StatelessExWithJpaTest extends AbstractJpaTest {

  @PersistenceContext EntityManager em;

  // NOTE: JPA 사용 시에는 꼭 @Transactinal 을 사용해야 합니다.
  @Transactional
  @Test
  @SuppressWarnings("unchecked")
  public void hibernateStatelessSessionDirectly() {

    StatelessEx.withTransaction(em, new Function1<StatelessSessionImpl, Object>() {
      @Override
      public Object invoke(StatelessSessionImpl stateless) {
        for (int i = 0; i < 10; i++) {
          Employee emp = new Employee();
          emp.setEmpNo("empNo-" + i);
          emp.setName("empName-" + i);

          stateless.insert(emp);
        }
        return null;
      }
    });

    List<Employee> emps = StatelessEx.withReadOnly(em, new Function1<StatelessSessionImpl, List<Employee>>() {
      @Override
      public List<Employee> invoke(StatelessSessionImpl stateless) {
        Criteria criteria = stateless.createCriteria(Employee.class);
        return (List<Employee>) criteria.list();
      }
    });

    assertThat(emps).isNotNull();
    assertThat(emps.size()).isGreaterThan(0);
    for (Employee emp : emps) {
      log.debug("emp={}", emp);
    }
  }

}