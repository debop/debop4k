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

package debop4k.core

import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.*

/**
 * Java 7 에서 새로 제공하는 AutoCloseable 을 Java 6 에서도 사용할 수 있도록 하기 위해 정의
 *
 * @author debop sunghyouk.bae@gmail.com
 */
interface AutoCloseable {

  @Throws(Exception::class)
  fun close(): Unit
}


/**
 * Java 7 에서 새로 제공하는 AutoCloseable 을 Java 6 에서도 사용할 수 있도록 하기 위해
 */
open class AutoCloseableHandler(val closeHandler: () -> Unit = DUMMY_CLOSE_ACTION) : AutoCloseable {

  private val log by lazy { LoggerFactory.getLogger(javaClass) }

  private val closed = AtomicBoolean(false)

  override fun close() {
    if (closed.compareAndSet(false, true)) {
      try {
        closeHandler()
      } catch(ignored: Exception) {
        log.warn("AutoCloseable 인스턴스의 close() 호출에서 예외가 발생했습니다. 무시합니다.", ignored)
      }
    }
  }

  companion object {
    val DUMMY_CLOSE_ACTION: () -> Unit = { }
  }
}