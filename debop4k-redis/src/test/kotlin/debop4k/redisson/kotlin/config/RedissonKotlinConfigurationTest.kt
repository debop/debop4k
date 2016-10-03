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

package debop4k.redisson.kotlin.config

import debop4k.core.loggerOf
import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.client.RedisClient
import org.redisson.client.protocol.RedisCommands
import org.redisson.client.protocol.RedisStrictCommand
import org.redisson.client.protocol.decoder.StringMapDataDecoder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(RedissonKotlinConfigration::class))
class RedissonKotlinConfigurationTest {

  private val log = loggerOf(javaClass)

  @Inject val redisson: RedissonClient = uninitialized()
  @Inject val redissonReactive: RedissonReactiveClient = uninitialized()
  @Inject val redis: RedisClient = uninitialized()

  @Test
  fun testInjection() {
    assertThat(redisson).isNotNull()
    assertThat(redissonReactive).isNotNull()
    assertThat(redis).isNotNull()
  }

  @Test
  fun testRedisConnection() {
    val conn = redis.connect()
    assertThat(conn.sync(RedisCommands.PING)).isEqualTo("PONG")

    val INFO_ALL = RedisStrictCommand("INFO", null, StringMapDataDecoder())
    val infos = conn.sync(INFO_ALL)
    for ((key, value) in infos) {
      println("redis key=$key, value=$value")
    }
    val future = conn.closeAsync()
    future.sync()
  }

  @Test
  fun testBucket() {
    redisson.keys.flushdb()

    val bucket = redisson.getBucket<String>("a")
    bucket.delete()
    assertThat(bucket.trySet("b")).isTrue()
    assertThat(bucket.delete()).isTrue()
    assertThat(redisson.getBucket<Any>("a").delete()).isFalse()
  }
}