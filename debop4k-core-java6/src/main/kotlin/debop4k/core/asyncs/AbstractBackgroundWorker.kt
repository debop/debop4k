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

package debop4k.core.asyncs

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.*

/**
 * AbstractBackgroundWorker
 */
abstract class AbstractBackgroundWorker(override val name: String) : BackgroundWorker {

  private val log = LoggerFactory.getLogger(javaClass)

  protected val running = AtomicBoolean(false)
  protected var workerThread: Thread? = null

  /** 구현 해야 합니다 */
  abstract fun newWorkerThread(): Thread

  override val isRunning: Boolean
    get() = running.get()

  @Synchronized
  override open fun start(): Unit {
    if (isRunning)
      return

    log.debug("{} 작업을 위한 백그라운드 스레드를 생성하고 시작합니다.", name)

    try {
      workerThread = newWorkerThread()
      workerThread?.start()

      if (workerThread != null && workerThread!!.isAlive)
        running.compareAndSet(false, true)
    } catch(e: Exception) {
      log.error("{} 작업 스레드를 시작하는데 실패했습니다.", e)
      throw RuntimeException(e)
    }
  }

  @Synchronized
  override open fun stop(): Unit {
    if (!isRunning)
      return

    log.debug("{} 작업을 종료합니다...", name)

    try {
      workerThread?.interrupt()
      try {
        workerThread?.join(500)
      } catch(ie: InterruptedException) {
        log.trace("작업이 중단되었습니다.")
      }
    } catch(e: Exception) {
      log.warn("{} 작업용 스레드를 종료하는데 실패했습니다.", e)
    } finally {
      workerThread = null
      running.set(false)
    }
  }
}