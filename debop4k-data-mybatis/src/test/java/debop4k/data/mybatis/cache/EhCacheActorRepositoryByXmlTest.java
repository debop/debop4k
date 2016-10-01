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

package debop4k.data.mybatis.cache;

import debop4k.data.mybatis.models.Actor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EhCacheActorRepositoryByXmlTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {EhCacheConfigurationByXml.class})
public class EhCacheActorRepositoryByXmlTest {

  @Inject CacheManager cm;
  @Inject EhCacheActorRepository actorRepo;

  private Cache cache = null;

  public Cache getCache() {
    if (cache == null) {
      cache = cm.getCache("ehcache-actors");
    }
    return cache;
  }

  @Test
  public void testConfiguration() {
    assertThat(actorRepo).isNotNull();
    assertThat(cm).isNotNull();
  }

  @Test
  public void testFindById() {
    Actor actor = actorRepo.findById(1);
    assertThat(actor).isNotNull();

    // 캐시로부터 직접 Actor 정보를 얻는다. 위의 findById 에 의해 캐시에 저장된다는 뜻 !!!
    Actor cached = getCache().get(1, Actor.class);
    assertThat(cached).isNotNull();
    assertThat(cached).isEqualTo(actor);
  }

  @Test
  public void testFindByName() {
    Actor actor = actorRepo.findByFirstname("Sunghyouk");
    assertThat(actor).isNotNull();

    Actor cached = getCache().get("firstname=Sunghyouk", Actor.class);
    assertThat(cached).isNotNull();
  }

  @Test
  public void testCachePut() {
    String firstname = "mybatis";
    Actor actor = Actor.of(null, firstname, "debop4k");
    actor = actorRepo.insertActor(actor);
    log.debug("saved actor={}", actor);

    Actor cached = getCache().get(actor.getId(), Actor.class);
    assertThat(cached).isNotNull().isEqualTo(actor);

    // Cache 가 제대로 되는지 확인
    Actor loaded = actorRepo.findById(actor.getId());
    log.debug("actor={}", loaded);
    assertThat(loaded).isNotNull();
    assertThat(loaded).isEqualTo(actor);

    // CacheEvict 테스트 (캐시에서 제거되어야 합니다)
    //
    actorRepo.deleteById(actor.getId());

    Actor deleted = getCache().get(actor.getId(), Actor.class);
    assertThat(deleted).isNull();
  }
}
