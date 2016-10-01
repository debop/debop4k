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

package debop4k.data.orm.mapping.associations.onetoone;

import debop4k.data.orm.mapping.AbstractMappingTest;
import debop4k.data.orm.mapping.associations.onetoone.models.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * OneToOneTests
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 6.
 */
@Slf4j
@SpringBootTest(classes = {OneToOneConfiguration.class})
public class OneToOneTests extends AbstractMappingTest {

  @PersistenceContext EntityManager em;

  @Test
  public void checkMapping() {
    assertThat(em).isNotNull();
  }

  @Test
  public void authorBiography() {
    Author author = new Author("debop");
    author.getBiography().setInformation("Sunghyouk Bae");
    author.getPicture().setPath("file:/a/b/c");

    em.persist(author);
    em.flush();
    em.clear();

    // Biograph 로드시 author 는 lazy loading
    log.debug("load biography");
    Biography biography = em.find(Biography.class, author.getId());
    assertThat(biography.getAuthor()).isNotNull();
    assertThat(biography.getAuthor()).isEqualTo(author);

    log.debug("load author...");
    author = em.find(Author.class, author.getId());
    assertThat(author).isNotNull();

    Biography bio = author.getBiography();
    assertThat(bio).isNotNull();
    assertThat(bio.getInformation()).isEqualTo("Sunghyouk Bae");

    em.remove(author);
    em.flush();
    em.clear();

    assertThat(em.find(Author.class, author.getId())).isNull();
  }

  @Test
  public void unidirectionalManyToOne() {
    Horse horse = new Horse("적토마");

    Cavalier cavalier = new Cavalier("관우");
    cavalier.setHorse(horse);

    em.persist(cavalier);
    em.flush();
    em.clear();

    Cavalier loaded = em.find(Cavalier.class, cavalier.getId());
    assertThat(loaded).isNotNull();
    assertThat(loaded.getHorse()).isNotNull();

    em.remove(loaded);
    em.flush();
    em.clear();

    assertThat(em.find(Cavalier.class, cavalier.getId())).isNull();
  }

  @Test
  public void unidirectionalOneToOne() {
    Vehicle bmw = new Vehicle("BMW");
    Wheel wheel = new Wheel("18inch");
    wheel.setVehicle(bmw);

    em.persist(bmw);
    em.persist(wheel);
    em.flush();
    em.clear();

    wheel = em.find(Wheel.class, wheel.getId());
    assertThat(wheel).isNotNull();
    assertThat(wheel.getVehicle()).isNotNull().isEqualTo(bmw);

    em.remove(wheel);
    em.remove(wheel.getVehicle());
    em.flush();
    em.clear();

    assertThat(em.find(Vehicle.class, bmw.getId())).isNull();
  }

}
