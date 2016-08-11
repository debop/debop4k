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
@file:JvmName("Asyncs")

package debop4k.examples.asyncs

import java.util.concurrent.*

inline fun Executor.runAsync(crossinline action: () -> Unit) {
  execute { action() }
}

fun Executor.runAsyncAll(actions: List<() -> Unit>) {
  actions.forEach { action -> execute { action() } }
}


inline fun <T> ExecutorService.runAsync(crossinline func: () -> T): Future<T>
    = this.submit(Callable { func() })

fun <T> ExecutorService.runAllAsync(funcs: Iterable<() -> T>): List<Future<T>> {
  val tasks = funcs.map { Callable { it() } }
  return this.invokeAll(tasks)
}

fun <T> ExecutorService.runAnyAsync(funcs: Iterable<() -> T>): T {
  val tasks = funcs.map { Callable { it() } }
  return this.invokeAny(tasks)
}


