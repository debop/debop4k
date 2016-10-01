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

package debop4k.data.orm.mapping.property;

import debop4k.core.utils.Stringx;
import debop4k.data.orm.mapping.AbstractMappingTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {PropertyConfiguration.class})
public class PropertyTest extends AbstractMappingTest {

  @Test
  public void enumTest() {
    EnumeratedEntity entity = new EnumeratedEntity(OrdinalEnum.Second, StringEnum.Decimal);
    em.persist(entity);
    em.flush();
    em.clear();

    EnumeratedEntity loaded = em.find(EnumeratedEntity.class, entity.getId());
    assertThat(loaded).isNotNull().isEqualTo(entity);
    assertThat(loaded.getOrdinalValue()).isEqualTo(OrdinalEnum.Second);
    assertThat(loaded.getStringValue()).isEqualTo(StringEnum.Decimal);

    em.remove(loaded);
    em.flush();

    assertThat(em.find(EnumeratedEntity.class, entity.getId())).isNull();
  }

  @Test
  public void lob() {
    LobEntity lob = new LobEntity();
    lob.setName("name");
    lob.setData(Stringx.replicate("동해물과 백두산이 마르고 닳도록", 10000));

    em.persist(lob);
    em.flush();
    em.clear();

    LobEntity loaded = em.find(LobEntity.class, lob.getId());
    assertThat(loaded).isEqualTo(lob);
    assertThat(loaded.getData()).isEqualTo(lob.getData());

    em.remove(loaded);
    em.flush();

    assertThat(em.find(LobEntity.class, lob.getId())).isNull();
  }
}
