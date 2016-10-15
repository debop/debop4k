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

package debop4k.data.orm.mapping.associations.unidirection;

import debop4k.core.utils.Objects;
import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.associations.unidirection.models.Cloud;
import debop4k.data.orm.mapping.associations.unidirection.models.Snowflake;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UnidirectionConfiguration.class})
@Transactional
public class UnidirectionalTest extends AbstractMappingTest {

  @PersistenceContext EntityManager em;

  @Test
  public void checkMapping() {
    assertThat(em).isNotNull();
  }

  @Test
  public void unidirectionalCollection() {
    Snowflake sf = new Snowflake(null, "Snowflake 1");
    Snowflake sf2 = new Snowflake(null, "Snowflake 2");

    Cloud cloud = new Cloud(null, 23.0);
    cloud.getProducedSnowflakes().add(sf);
    cloud.getProducedSnowflakes().add(sf2);
    em.persist(cloud);
    em.flush();
    em.clear();

    Cloud cloud2 = em.find(Cloud.class, cloud.getId());
    assertThat(cloud2).isNotNull().isEqualTo(cloud);
    Assertions.assertThat(cloud2.getProducedSnowflakes()).hasSize(2);

    Snowflake removedSf = cloud2.getProducedSnowflakes().iterator().next();
    Snowflake sf3 = new Snowflake(null, "Snowflake 3");

    cloud2.getProducedSnowflakes().remove(removedSf);
    cloud2.getProducedSnowflakes().add(sf3);
    em.persist(cloud2);
    em.flush();
    em.clear();

    cloud2 = em.find(Cloud.class, cloud.getId());
    assertThat(cloud2).isNotNull().isEqualTo(cloud);
    Assertions.assertThat(cloud2.getProducedSnowflakes()).hasSize(2);

    boolean removedExists = false;
    for (Snowflake snowflake : cloud2.getProducedSnowflakes()) {
      if (Objects.equals(snowflake.getDescription(), removedSf.getDescription())) {
        removedExists = true;
      }
    }
    assertThat(removedExists).isFalse();

    assertThat(cloud2.getProducedSnowflakes().contains(sf3)).isTrue();

    cloud2.getProducedSnowflakes().clear();
    em.remove(em.getReference(Snowflake.class, removedSf.getId()));
    em.persist(cloud2);
    em.flush();
    em.clear();

    cloud2 = em.find(Cloud.class, cloud.getId());
    assertThat(cloud2).isNotNull().isEqualTo(cloud);
    Assertions.assertThat(cloud2.getProducedSnowflakes()).hasSize(0);

    em.remove(cloud2);
    em.flush();
    em.clear();

    assertThat(em.find(Cloud.class, cloud.getId())).isNull();

  }
}
