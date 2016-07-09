/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("KovenantUtils")

package debop4k.core.asyncs.kovenantEx

import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import java.util.concurrent.*


/**
 * 특정 코드를 비동기 방식으로 작업하도록 합니다.
 */
fun <V> async(body: () -> V): Promise<V, Exception> {
  return task { body() }
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다립니다.
 */
fun <V, E> await(promise: Promise<V, E>): Unit {
  val latch = CountDownLatch(1)
  promise success { latch.countDown() }
  promise fail { latch.countDown() }
  latch.await()
}

fun <V, E> awaitAll(promises: Collection<Promise<V, E>>): Unit {
  val latch = CountDownLatch(promises.size)
  promises.forEach { p ->
    p success { latch.countDown() }
    p fail { latch.countDown() }
  }
  latch.await()
}

fun <V, E> result(promise: Promise<V, E>): V {
  await(promise)
  return promise.get()
}

fun <V, E> resultAll(promises: Collection<Promise<V, E>>): Collection<V> {
  awaitAll(promises)
  return promises.map { it.get() }
}