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
package debop4k.core.asyncs

import java.util.concurrent.*
import java.util.concurrent.atomic.*

/**
 * 이름을 가지는 Thread 를 생성하는 Factory
 *
 * @author sunghyouk.bae@gmail.com
 */
class NamedThreadFactory(val prefix: String?, val isDaemon: Boolean = false) : ThreadFactory {

  val name: String
    get() = if (!prefix.isNullOrBlank()) prefix!! else "thread"

  val group: ThreadGroup by lazy { ThreadGroup(Thread.currentThread().threadGroup, name) }
  val threadNumber = AtomicInteger(1)

  /**
   * 새로운 스레드를 생성합니다.
   */
  override fun newThread(runnable: Runnable): Thread {

    val threadName = name + "-" + threadNumber.andIncrement
    val thread = Thread(group, runnable, threadName)
    thread.isDaemon = isDaemon
    thread.priority = Thread.NORM_PRIORITY

    return thread
  }

  /**
   * 새로운 스레드를 생성합니다.
   */
  inline fun newThread(crossinline body: () -> Unit): Thread {
    return newThread(Runnable { body() })
  }
}