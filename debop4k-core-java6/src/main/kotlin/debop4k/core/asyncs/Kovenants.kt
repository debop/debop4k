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

@file:JvmName("Kovenants")

package debop4k.core.asyncs

import nl.komponents.kovenant.Context
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import java.util.concurrent.*


/**
 * 특정 코드를 비동기 방식으로 작업하도록 합니다.
 */
fun <V> async(context: Context = Kovenant.context, body: () -> V): Promise<V, Exception> {
  return task(context) { body() }
}

fun <V, E> await(promise: Promise<V, E>): Unit {
  val latch = CountDownLatch(1)
  promise always { latch.countDown() }
  latch.await()
}

fun awaitAll(vararg promises: Promise<*, *>): Unit {
  val latch = CountDownLatch(promises.size)
  promises.forEach { p ->
    p always { latch.countDown() }
  }
  latch.await()
}

fun awaitAll(promises: Collection<Promise<*, *>>): Unit {
  val latch = CountDownLatch(promises.size)
  promises.forEach { p ->
    p always { latch.countDown() }
  }
  latch.await()
}


/**
 * [Promise] 이 모두 완료될 때까지 기다립니다.
 */
fun <V, E> Promise<V, E>.ready(): Promise<V, E> {
  await(this)
  return this
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다립니다.
 */
fun readyAll(vararg promises: Promise<*, *>): Unit {
  awaitAll(*promises)
}

/**
 * 모든 [Promise] 가 끝나기를 기다립니다.
 */
fun <V, E> readyAll(promises: Collection<Promise<V, E>>): Unit {
  awaitAll(promises)
}

/**
 * [Promise]이 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V, E> Promise<V, E>.result(): V {
  return this.ready().get()
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V, E> resultAll(promises: Collection<Promise<V, E>>): Collection<V> {
  awaitAll(promises)
  return promises.map { it.get() }
}