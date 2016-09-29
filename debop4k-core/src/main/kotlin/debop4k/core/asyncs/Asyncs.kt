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

@file:JvmName("Asyncs")

package debop4k.core.asyncs

import nl.komponents.kovenant.*
import java.util.concurrent.*


/**
 * 특정 코드를 비동기 방식으로 작업하도록 합니다.
 */
@JvmOverloads
fun <V> future(context: Context = Kovenant.context,
               body: () -> V): Promise<V, Exception> {
  return task(context) { body() }
}

@JvmOverloads
fun <V> future(context: Context = Kovenant.context,
               result: V,
               body: () -> V): Promise<V, Exception> {
  return task(context) { body() }.thenApply { result }
}


fun <V> futureAll(context: Context = Kovenant.context, tasks: List<() -> V>): Collection<Promise<V, Exception>> {
  return tasks.map { task(context) { it() } }
}


fun <V, E> Promise<V, E>.ready(): Promise<V, E> {
  val latch = CountDownLatch(1)
  this always { latch.countDown() }
  latch.await()
  return this
}

fun <V> readyAll(vararg promises: Promise<V, Exception>): Promise<List<V>, Exception> {
  val mp = all(*promises).ready()
  return mp.ready()
}

fun <V> Collection<Promise<V, Exception>>.readyAll(): Promise<List<V>, Exception> {
  val mp = all(*this.toTypedArray())
  return mp.ready()
}

fun <V> Collection<Promise<V, Exception>>.readyAny(): Promise<V, List<Exception>> {
  return any(*this.toTypedArray())
}


/**
 * [Promise]이 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> Promise<V, Exception>.result(): V {
  this.ready()
  return this.get()
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> resultAll(vararg promises: Promise<V, Exception>): List<V> {
  return readyAll(*promises).get()
}

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> Collection<Promise<V, Exception>>.resultAll(): List<V> {
  return this.readyAll().get()
}

/**
 * [Promise] 컬렉션 중 첫번째 완료된 결과값을 반환합니다.
 */
fun <V> Collection<Promise<V, Exception>>.resultAny(): V {
  return this.readyAny().get()
}
