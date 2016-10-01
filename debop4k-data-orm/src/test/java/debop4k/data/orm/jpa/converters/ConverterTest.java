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

package debop4k.data.orm.jpa.converters;

import debop4k.data.orm.jpa.AbstractJpaTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
public class ConverterTest extends AbstractJpaTest {

  @PersistenceContext EntityManager em;

  @Test
  public void jpaConverter() {

    ConverterEntity entity = new ConverterEntity();
    entity.setName("debop");
    entity.setLocale(Locale.US);
    entity.setDateStr(DateTime.now());
    entity.setTimestamp(entity.getDateStr());

    em.persist(entity);
    em.flush();
    em.clear();

    ConverterEntity loaded = em.find(ConverterEntity.class, entity.getId());

    assertThat(loaded).isNotNull();
    assertThat(loaded.getLocale()).isEqualTo(entity.getLocale());
    assertThat(loaded.getDateStr().getMillis()).isEqualTo(entity.getDateStr().getMillis());
    assertThat(loaded.getTimestamp()).isEqualTo(entity.getTimestamp());

    em.remove(loaded);
    em.flush();

    assertThat(em.find(ConverterEntity.class, entity.getId())).isNull();
  }
}
