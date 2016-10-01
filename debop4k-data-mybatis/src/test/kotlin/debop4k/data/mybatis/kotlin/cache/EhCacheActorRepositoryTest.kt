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

package debop4k.data.mybatis.kotlin.cache

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import debop4k.data.mybatis.kotlin.models.KotlinActor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(EhCacheConfiguration::class))
class EhCacheActorRepositoryTest {

  private val log = loggerOf(javaClass)

  @Inject val cm: CacheManager = uninitialized()
  @Inject val actorRepo: EhCacheActorRepository = uninitialized()

  private var cache: Cache? = null

  fun getCache(): Cache {
    if (cache == null) {
      cache = cm!!.getCache("kotlin-actors")
    }
    return cache!!
  }

  @Test
  fun testConfiguration() {
    assertThat(actorRepo).isNotNull()
    assertThat(cm).isNotNull()
  }

  @Test
  fun testFindById() {
    val actor = actorRepo.findById(1)
    assertThat(actor).isNotNull()

    // 캐시로부터 직접 KotlinActor 정보를 얻는다. 위의 findById 에 의해 캐시에 저장된다는 뜻 !!!
    val cached = getCache().get(1, KotlinActor::class.java)
    assertThat(cached).isNotNull()
    assertThat(cached).isEqualTo(actor)
  }

  @Test
  fun testFindByName() {
    val actor = actorRepo.findByFirstname("Sunghyouk")
    assertThat(actor).isNotNull()

    val cached = getCache().get("firstname=Sunghyouk", KotlinActor::class.java)
    assertThat(cached).isNotNull()
  }

  @Test
  fun testCachePut() {
    val firstname = "mybatis"
    var actor = KotlinActor.of(null, firstname, "debop4k")
    actor = actorRepo.insertActor(actor)
    log.debug("saved actor={}", actor)

    val cached = getCache().get(actor.id, KotlinActor::class.java)
    assertThat(cached).isNotNull().isEqualTo(actor)

    // Cache 가 제대로 되는지 확인
    val loaded = actorRepo.findById(actor.id!!)
    log.debug("actor={}", loaded)
    assertThat(loaded).isNotNull()
    assertThat(loaded).isEqualTo(actor)

    // CacheEvict 테스트 (캐시에서 제거되어야 합니다)
    //
    actorRepo.deleteById(actor.id)

    val deleted = getCache().get(actor.id, KotlinActor::class.java)
    assertThat(deleted).isNull()
  }
}