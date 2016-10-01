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

package debop4k.data.orm.mapping.simple;

import debop4k.core.io.serializers.Serializers;
import debop4k.data.orm.jpa.dao.JpaDao;
import debop4k.data.orm.mapping.AbstractMappingTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {SimpleConfiguration.class})
public class SimpleTest extends AbstractMappingTest {

  @Inject JpaDao dao;

  @Test
  public void lifecyle() {
    LifecycleEntity entity = new LifecycleEntity("이름");
    em.persist(entity);
    em.flush();
    em.clear();
    log.debug("saved entity={}", entity);
    Assertions.assertThat(entity.isPersisted()).isTrue();

    LifecycleEntity loaded = em.find(LifecycleEntity.class, entity.getId());
    assertThat(loaded).isNotNull().isEqualTo(entity);
    assertThat(loaded.getCreateAt()).isNotNull();
    assertThat(loaded.getUpdatedAt()).isNull();

    em.detach(loaded);
    loaded.setName("변경된 이름");
    log.debug("detached entity={}", loaded);
    Assertions.assertThat(loaded.isPersisted()).isTrue();

    em.merge(loaded);
    em.flush();

    loaded = em.find(LifecycleEntity.class, entity.getId());
    assertThat(loaded).isNotNull().isEqualTo(entity);
    assertThat(loaded.getCreateAt()).isNotNull();
    assertThat(loaded.getUpdatedAt()).isNotNull();

    em.remove(loaded);
    em.flush();

    assertThat(em.find(LifecycleEntity.class, entity.getId())).isNull();
  }

  @Test
  public void transientObject() {
    SimpleEntity transientObj = new SimpleEntity("transient1");
    SimpleEntity copiedObj = Serializers.FST.copy(transientObj);

    copiedObj.setDescription("description");
    assertThat(copiedObj).isEqualTo(transientObj);

    SimpleEntity savedObj = Serializers.FST.copy(transientObj);
    em.persist(savedObj);
    em.flush();

    // 저장된 엔티티는 Id 값을 가지지만, transientObj는 Id 값을 가지지 않습니다.
    assertThat(savedObj).isNotEqualTo(transientObj);

    SimpleEntity loaded = em.find(SimpleEntity.class, savedObj.getId());
    assertThat(loaded).isNotNull()
                      .isEqualTo(savedObj)
                      .isNotEqualTo(transientObj);

    SimpleEntity savedObj2 = Serializers.FST.copy(transientObj);
    em.persist(savedObj2);
    em.flush();

    SimpleEntity loaded2 = em.find(SimpleEntity.class, savedObj2.getId());
    assertThat(loaded2).isNotNull()
                       .isEqualTo(savedObj2)
                       .isNotEqualTo(savedObj)
                       .isNotEqualTo(loaded)
                       .isNotEqualTo(transientObj);

    em.remove(loaded2);
    em.remove(loaded);
    em.flush();

    assertThat(em.find(SimpleEntity.class, loaded2.getId())).isNull();
  }
}
