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

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.*
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * 백그라운에서 수행되는 Worker 를 표현합니다.
 *
 * @author debop sunghyouk.bae@gmail.com
 */
interface BackgroundWorker {

  /** Worker name */
  val name: String

  /** Worker 가 작업 중인가? */
  val isRunning: Boolean

  /** Worker를 시작합니다. */
  @PostConstruct
  fun start(): Unit

  /** Worker 를 종료합니다. */
  @PreDestroy
  fun stop(): Unit

}

/**
 * 백그라운드에서 수행되는 Worker 의 추상 클래스입니다.
 * 이 클래스를 상속 받고, 백그라운드에서 수행할 작업을 [newWorkerThread] 메소드를 재정의하면 됩니다.
 *
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class AbstractBackgroundWorker(override val name: String) : BackgroundWorker {

  private val log = LoggerFactory.getLogger(javaClass)

  protected val running = AtomicBoolean(false)
  @Volatile protected var workerThread: Thread? = null

  /** 구현 해야 합니다 */
  abstract fun newWorkerThread(): Thread

  override val isRunning: Boolean
    get() = running.get()

  /**
   * 백그라운드 작업을 시작합니다.
   * Spring Bean으로 등록하면 [PostConstruct] annotation 지정에 따라 자동으로 시작합니다.
   */
  @PostConstruct
  @Synchronized
  override fun start(): Unit {
    if (isRunning)
      return

    log.debug("{} 작업을 위한 백그라운드 스레드를 생성하고 시작합니다.", name)

    try {
      workerThread = newWorkerThread()
      workerThread?.let {
        it.run()
        running.compareAndSet(false, true)
      }
    } catch(e: Exception) {
      log.error("{} 작업 스레드를 시작하는데 실패했습니다.", name, e)
      throw RuntimeException(e)
    }
  }

  /**
   * 백그라운드 작업을 종료합니다.
   * Spring Bean으로 등록하면 [PreDestroy] annotation 지정에 따라 자동으로 종료합니다.
   */
  @PreDestroy
  @Synchronized
  override fun stop(): Unit {
    if (!isRunning)
      return

    log.debug("{} 작업을 종료합니다...", name)

    try {
      workerThread?.let {
        if (it.isAlive) {
          it.interrupt()
          try {
            it.join(500)
          } catch(ie: InterruptedException) {
            log.debug("작업 스레드가 종료되었습니다.")
          }
        }
      }
//      if (workerThread != null && workerThread!!.isAlive) {
//        workerThread!!.interrupt()
//        try {
//          workerThread!!.join(500)
//        } catch(ie: InterruptedException) {
//          log.debug("작업이 중단되었습니다.")
//        }
//      }
    } catch(e: Exception) {
      log.warn("{} 작업용 스레드를 종료하는데 실패했습니다.", e)
    } finally {
      workerThread = null
      running.set(false)
    }
  }
}
