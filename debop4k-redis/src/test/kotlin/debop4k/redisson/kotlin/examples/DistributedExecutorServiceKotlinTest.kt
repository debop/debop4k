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

package debop4k.redisson.kotlin.examples

import debop4k.core.uninitialized
import debop4k.redisson.kotlin.AbstractRedissonKotlinTest
import org.junit.Test
import org.redisson.api.RedissonClient
import org.redisson.api.annotation.RInject
import org.redisson.client.codec.StringCodec
import java.io.Serializable
import java.util.concurrent.*

/**
 * DistributedExecutorServiceKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
class DistributedExecutorServiceKotlinTest : AbstractRedissonKotlinTest() {

  @Test
  fun testSimpleTask() {

    val map = redisson.getMap<String, Int>("myMap")
    map.fastPut("a", 10)
    map.fastPut("b", 20)

    val executorService = redisson.getExecutorService(StringCodec(), "simpleTask")
    executorService.submit(CallableTask())

//    val future = executorService.submit(CallableTask())

    Thread.sleep(1000)

    // Single Node 서버에서는 작동하지 않는다
//     println(future.get())
  }

}

class CallableTask : Callable<Long?>, Serializable {

  @RInject
  val redissonClient: RedissonClient = uninitialized()

  override fun call(): Long? {
    val map = redissonClient.getMap<String, Int>("myMap")
    map.fastPut("a", 10)
    map.fastPut("b", 20)

    var result = 0L
    map.values.forEach {
      if (Thread.currentThread().isInterrupted)
        return null
      result += it
    }
    return result
  }
}
