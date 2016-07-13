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

package debop4k.core.asyncs

import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.joda.time.Duration
import java.util.concurrent.*


/**
 * 비동기 작업에 필요한 메소드를 제공하는 Object 입니다.
 * @author sunghyouk.bae@gmail.com
 */
object Asyncs {

  val PROCESS_COUNT = Runtime.getRuntime().availableProcessors()
  val DEFAULT_EXECUTOR: ExecutorService by lazy { Executors.newFixedThreadPool(PROCESS_COUNT * 2) }
  val EMPTY_RUNNABLE: Runnable = Runnable { }
  val duration: Duration = Duration.standardMinutes(15)

  fun <T> async(body: () -> T): Promise<T, Exception> {
    return task { body() }
  }

  fun <T> await(promise: Promise<T, *>): Unit {
    ready(promise)
  }

  fun <T> ready(promise: Promise<T, *>): Unit {
    val latch = CountDownLatch(1)
    promise always { latch.countDown() }
    latch.await()
  }

  fun <T> result(promise: Promise<T, *>): T {
    ready(promise)
    return promise.get()
  }
}

