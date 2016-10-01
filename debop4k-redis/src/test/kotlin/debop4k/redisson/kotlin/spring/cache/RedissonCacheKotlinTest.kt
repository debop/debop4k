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

package debop4k.redisson.kotlin.spring.cache

import debop4k.core.uninitialized
import debop4k.redisson.kotlin.AbstractRedissonKotlinTest
import debop4k.redisson.kotlin.spring.cache.UserRepository.UserObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

/**
 * RedissonCacheKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(RedissonCacheKotlinConfig::class))
class RedissonCacheKotlinTest : AbstractRedissonKotlinTest() {

  @Inject val cacheManager: CacheManager = uninitialized()
  @Inject val userRepo: UserRepository = uninitialized()

  @Test
  fun testConfiguration() {
    assertThat(cacheManager).isNotNull()
    assertThat(userRepo).isNotNull()
  }

  @Test
  fun validateCache() {
    val users = cacheManager.getCache("kotlinUsers")

    assertThat(users).isNotNull()
    users.clear()
    assertThat(users.get("user1")).isNull()

    userRepo.save("user1", UserObject("name1", "value1"))
    val user1 = userRepo.get("user1")
    assertThat(users.get("user1").get()).isNotNull().isEqualTo(user1)

  }

  @Test
  fun testNull() {
    userRepo.save("user1", null)
    assertThat(userRepo.getNull("user1")).isNull()
    userRepo.remove("user1")
    assertThat(userRepo.getNull("user1")).isNull()
  }

  @Test
  fun testRemove() {
    userRepo.save("user1", UserObject("name1", "value1"))
    assertThat(userRepo.get("user1")).isNotNull().isInstanceOf(UserObject::class.java)
    userRepo.remove("user1")
    assertThat(userRepo.getNull("user1")).isNull()
  }

  @Test
  fun testPutGet() {
    userRepo.save("user1", UserObject("name1", "value1"))
    val u = userRepo.get("user1")
    assertThat(u).isNotNull()
    assertThat(u?.name).isEqualTo("name1")
    assertThat(u?.value).isEqualTo("value1")
  }

  @Test(expected = IllegalStateException::class)
  fun testGet() {
    userRepo.get("notExists")
  }
}