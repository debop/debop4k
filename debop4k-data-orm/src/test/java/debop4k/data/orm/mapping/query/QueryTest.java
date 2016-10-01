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

package debop4k.data.orm.mapping.query;

import debop4k.data.orm.mapping.AbstractMappingTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {QueryConfiguration.class})
public class QueryTest extends AbstractMappingTest {

  @Before
  public void setup() {
    log.debug("예제용 데이터 추가...");

    em.createQuery("delete from Hypothesis").executeUpdate();
    em.createQuery("delete from Helicopter").executeUpdate();

    Hypothesis socrates = new Hypothesis("13");
    socrates.setDescription("There are more than two dimensions over the shadows we see out newPeriod the cave");
    socrates.setPosition(1);
    em.persist(socrates);

    Hypothesis peano = new Hypothesis("14");
    peano.setDescription("Peano's curve and then Hilbert's space filling curve proof the connection from mono-dimensional to bi-dimensional space");
    peano.setPosition(2);
    em.persist(peano);

    Hypothesis sanne = new Hypothesis("15");
    sanne.setDescription("Hilbert's proof newPeriod connection to 2 dimensions can be induced to reason on N dimensions");
    sanne.setPosition(3);
    em.persist(sanne);

    Hypothesis shortOne = new Hypothesis("16");
    shortOne.setDescription("stuff works");
    shortOne.setPosition(4);
    em.persist(shortOne);

    Helicopter helicopter = new Helicopter("No creative clue");
    em.persist(helicopter);

    em.flush();
    em.clear();

    Assertions.assertThat(helicopter.isPersisted()).isTrue();
  }

  @SuppressWarnings("unchecked")
  private static void assertQuery(EntityManager em, int expectedSize, Query query) {
    assertThat(query.getResultList()).hasSize(expectedSize);
    em.clear();
  }

  @Test
  public void simpleQuery() {
    String hypothesisName = Hypothesis.class.getName();

    assertQuery(em, 4, em.createQuery("select h from Hypothesis h"));
    assertQuery(em, 4, em.createQuery("select h from " + hypothesisName + " h"));
    assertQuery(em, 1, em.createQuery("select h from Helicopter h"));
  }

  @Test
  public void constantParameterQuery() {
    assertQuery(em, 1, em.createQuery("select h from Hypothesis h where h.description = 'stuff works'"));
  }

  @Test
  public void parametericQuery() {
    Query query = em.createQuery("select h from Hypothesis h where h.description=:description")
                    .setParameter("description", "stuff works");
    assertQuery(em, 1, query);
  }
}
