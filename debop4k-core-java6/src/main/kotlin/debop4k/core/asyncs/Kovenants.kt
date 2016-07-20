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

import nl.komponents.kovenant.*
import java.util.concurrent.*


/**
 * 특정 코드를 비동기 방식으로 작업하도록 합니다.
 */
fun <V> async(context: Context = Kovenant.context, body: () -> V): Promise<V, Exception> {
  return task(context) { body() }
}


fun <V> Promise<V, Exception>.await(): Unit {
  val latch = CountDownLatch(1)
  this always { latch.countDown() }
  latch.await()
}

fun <V> awaitAll(vararg promises: Promise<V, Exception>): Unit {
  all(*promises)

//  val latch = CountDownLatch(promises.size)
//  promises.forEach { p ->
//    p always { latch.countDown() }
//  }
//  latch.await()
}

fun <V> awaitAll(promises: Collection<Promise<V, Exception>>): Unit {
  all(*promises.toTypedArray())
//  val latch = CountDownLatch(promises.size)
//  promises.forEach { p ->
//    p always { latch.countDown() }
//  }
//  latch.await()
}


/**
 * [Promise] 이 모두 완료될 때까지 기다립니다.
 */
fun <V> Promise<V, Exception>.ready(): Promise<V, Exception> {
  this.await()
  return this
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다립니다.
 */
fun <V> readyAll(vararg promises: Promise<V, Exception>): Unit {
  awaitAll(*promises)
}

/**
 * 모든 [Promise] 가 끝나기를 기다립니다.
 */
fun <V> readyAll(promises: Collection<Promise<V, Exception>>): Unit {
  awaitAll(promises)
}

/**
 * [Promise]이 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> Promise<V, Exception>.result(): V {
  return this.ready().get()
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> resultAll(vararg promises: Promise<V, Exception>): Collection<V> {
  return all(*promises).ready().get()
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> resultAll(promises: Collection<Promise<V, Exception>>): Collection<V> {
  return all(*promises.toTypedArray()).ready().get()
//  awaitAll(promises)
//  return promises.map { it.get() }
}