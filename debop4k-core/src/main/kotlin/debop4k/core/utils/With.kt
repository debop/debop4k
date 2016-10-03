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

package debop4k.core.utils

import debop4k.core.asyncs.NamedThreadFactory
import debop4k.core.loggerOf
import org.slf4j.Logger
import java.util.concurrent.*

/**
 * With
 * @author sunghyouk.bae@gmail.com
 */
object With {

  val log: Logger = loggerOf(javaClass)

  @JvmStatic
  fun stopwatch(runnable: Runnable): Unit {
    stopwatch { runnable.run() }
  }

  @JvmStatic
  inline fun stopwatch(action: () -> Unit): Unit {
    val sw = Stopwatch.createStarted()
    try {
      action()
    } finally {
      log.info("stopwatch result={} msec.", sw.stop().elapsed(TimeUnit.MILLISECONDS))
    }
  }

  @JvmStatic
  fun stopwatch(msg: String, runnable: Runnable): Unit {
    stopwatch(msg) { runnable.run() }
  }

  @JvmStatic
  inline fun stopwatch(msg: String, action: () -> Unit): Unit {
    val sw = Stopwatch.createStarted(msg)
    try {
      action.invoke()
    } finally {
      log.info("{}={} msec.", msg, sw.stop().elapsed(TimeUnit.MILLISECONDS))
    }
  }

  @JvmStatic
  fun joinThread(thread: Thread): Unit {
    try {
      thread.join()
    } catch(ie: InterruptedException) {
      log.debug("스레드 종료 대기 중에 interrupted exception이 발생했습니다. 스레드를 종료합니다.")
    }
  }

  @JvmStatic
  fun thread(action: Runnable): Unit {
    val t = Thread(action)
    t.start()
    joinThread(t)
  }

  @JvmStatic
  fun thread(prefix: String, action: Runnable): Unit {
    val t = NamedThreadFactory(prefix).newThread(action)
    t.start()
    joinThread(t)
  }
}