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

import debop4k.core.collections.fastListOf
import debop4k.core.loggerOf

/**
 * Runtimes
 * @author sunghyouk.bae@gmail.com
 */
object Runtimes {

  private val log = loggerOf(Runtimes::class.java)

  private val runtime: Runtime get() = Runtime.getRuntime()

  val availableProcessors: Int = runtime.availableProcessors()

  val availableMemory: Long
    get() = runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory())

  val availableMemoryPercent: Double
    get() = availableMemory.toDouble() * 100.0 / runtime.maxMemory()

  /**
   * 엄청 큰 메모리를 할당한 후, Garbage collection 을 실행합니다.
   */
  fun compactMemory() {
    log.debug("Compact memory...")
    try {
      val unused = fastListOf<ByteArray>()
      for (i in 0..127)
        unused.add(ByteArray(2000000000))
    } catch(ignored: OutOfMemoryError) {

    }
    System.gc()
  }

}