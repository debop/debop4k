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

@file:JvmName("Asyncx")

package debop4k.core.asyncs

import nl.komponents.kovenant.*
import java.util.concurrent.*

/**
 * 아무일도 하지 않는 빈 [Runnable] 객체입니다.
 */
val EMPTY_RUNNABLE: Runnable = Runnable { }

inline fun runnable(crossinline action: () -> Unit): Runnable {
  return Runnable { action() }
}

inline fun <T> callable(crossinline func: () -> T): Callable<T> {
  return Callable { func() }
}

infix inline fun <T, V> T.future(crossinline block: T.() -> V): Promise<V, Exception> {
  return task { this.block() }
}

/**
 * [runnable] 인스턴스를 비동기 방식으로 작업하도록 합니다.
 */
fun future(runnable: Runnable): Promise<Unit, Exception> {
  return task { runnable.run() }
}

/**
 * [callable] 인스턴스를 비동기 방식으로 작업하도록 합니다.
 */
fun <V> future(callable: Callable<V>): Promise<V, Exception> {
  return task { callable.call() }
}

/**
 * 지정된 [func] 을 비동기 방식으로 작업하도록 합니다.
 */
fun <V> future(func: () -> V): Promise<V, Exception> {
  return task(body = func)
}

/**
 * 지정된 [func] 을 비동기 방식으로 작업하도록 합니다.
 */
fun <V> future(context: Context, func: () -> V): Promise<V, Exception> {
  return task(context, func)
}

/**
 * 지정된 [body] 을 비동기 방식으로 작업하도록 하고, 완료되면 [result] 를 반환하도록 합니다.
 */
fun <V> future(result: V,
               body: () -> V): Promise<V, Exception> {
  return task { body() }.thenApply { result }
}


/**
 * 지정된 [body] 을 비동기 방식으로 작업하도록 하고, 완료되면 [result] 를 반환하도록 합니다.
 */
fun <V> future(context: Context = Kovenant.context,
               result: V,
               body: () -> V): Promise<V, Exception> {
  return task(context, body).thenApply { result }
}


/**
 * 모든 task 들을 비동기 방식으로 실행하도록 합니다.
 */
fun <V> futureAll(tasks: Iterable<() -> V>): Collection<Promise<V, Exception>> {
  return tasks.map { task { it() } }
}

/**
 * 모든 task 들을 비동기 방식으로 실행하도록 합니다.
 */
fun <V> futureAll(context: Context = Kovenant.context,
                  tasks: Iterable<() -> V>): Collection<Promise<V, Exception>> {
  return tasks.map { task(context) { it() } }
}

fun <V> delayedFuture(delay: Long = 0L, timeunit: TimeUnit = TimeUnit.MILLISECONDS, body: () -> V): Promise<V, Exception> {
  if (delay > 0) {
    return task { body() }
  } else {
    return task {
      Thread.sleep(timeunit.toMillis(delay))
    } then {
      body()
    }
  }
}


/**
 * [Promise] 가 완료될 때까지 대기합니다.
 */
fun <V, E> Promise<V, E>.ready(): Promise<V, E> {
  val latch = CountDownLatch(1)
  this always { latch.countDown() }
  latch.await()
  return this
}

/**
 * [Promise] 배열의 모든 task 가 완료될 때까지 대기합니다.
 */
fun <V> readyAll(vararg promises: Promise<V, Exception>): Promise<List<V>, Exception>
    = all(*promises).ready()

/**
 * [Promise] 컬렉션의 모든 task 가 완료될 때까지 대기합니다.
 */
fun <V> Collection<Promise<V, Exception>>.readyAll(): Promise<List<V>, Exception>
    = all(*this.toTypedArray()).ready()

/**
 * [Promise] 중에 하나라도 완료되면 반환합니다.
 */
fun <V> readyAny(vararg promises: Promise<V, Exception>): Promise<V, List<Exception>>
    = any(*promises).ready()

/**
 * [Promise] 중에 하나라도 완료되면 반환합니다.
 */
fun <V> Collection<Promise<V, Exception>>.readyAny(): Promise<V, List<Exception>>
    = any(*this.toTypedArray()).ready()


/**
 * [Promise]이 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V, E> Promise<V, E>.result(): V = this.get()

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> resultAll(vararg promises: Promise<V, Exception>): List<V>
    = readyAll(*promises).get()

/**
 * [Promise] 컬렉션이 모두 완료될 때까지 기다렸다가 결과를 반환합니다.
 */
fun <V> Collection<Promise<V, Exception>>.resultAll(): List<V>
    = this.readyAll().get()

/**
 * [Promise] 중에 하나라도 완료되면 결과를 반환합니다.
 */
fun <V> resultAny(vararg promises: Promise<V, Exception>): V
    = any(*promises).ready().get()

/**
 * [Promise] 중에 하나라도 완료되면 결과를 반환합니다.
 */
fun <V> Collection<Promise<V, Exception>>.resultAny(): V
    = any(*this.toTypedArray()).ready().get()

