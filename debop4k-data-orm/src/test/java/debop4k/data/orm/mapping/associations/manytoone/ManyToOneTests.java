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

package debop4k.data.orm.mapping.associations.manytoone;

import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.associations.manytoone.models.Jug;
import debop4k.data.orm.mapping.associations.manytoone.models.JugMeter;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {ManyToOneConfiguration.class})
public class ManyToOneTests extends AbstractMappingTest {

  @PersistenceContext EntityManager em;

  @Test
  public void checkMapping() {
    assertThat(em).isNotNull();
  }

  @Test
  public void unidirectionalManyToOne() {
    Jug jug = new Jug("JUG Summer Camp");
    JugMeter emmanuel = new JugMeter("Emmanuel Bernard");
    emmanuel.setMemberOf(jug);

    JugMeter jerome = new JugMeter("Jerome");
    jerome.setMemberOf(jug);

    em.persist(jug);
    em.persist(emmanuel);
    em.persist(jerome);
    em.flush();
    em.clear();

    JugMeter emmanuel2 = em.find(JugMeter.class, emmanuel.getId());
    assertThat(emmanuel2).isNotNull().isEqualTo(emmanuel);
    assertThat(emmanuel2.getMemberOf()).isNotNull().isEqualTo(jug);

    em.remove(emmanuel2);
    em.flush();
    em.clear();

    JugMeter jerome2 = em.find(JugMeter.class, jerome.getId());
    assertThat(jerome2).isNotNull().isEqualTo(jerome);
    assertThat(jerome2.getMemberOf()).isNotNull().isEqualTo(jug);

    Jug jug2 = jerome2.getMemberOf();
    // JugMeter 먼저 삭제해야 한다. Jug 먼저 삭제하면, JugMeter#memgerOf 가 null 로 설정되어 버린다.
    em.remove(jerome2);
    em.remove(jug2);
    em.flush();
    em.clear();

    Assertions.assertThat(em.find(Jug.class, jug.getId())).isNull();
  }
}
