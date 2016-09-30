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

@file:JvmName("AutoCloseablex")

package debop4k.core

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.*

val DUMMY_CLOSE_ACTION: Runnable = Runnable { }

/**
 * Java 7 에서 새로 제공하는 AutoCloseable 을 Java 6 에서도 사용할 수 있도록 하기 위해
 */
open class AutoCloseableHandler(val closeHandler: Runnable = DUMMY_CLOSE_ACTION) : AutoCloseable {

  private val log: Logger by lazy { LoggerFactory.getLogger(javaClass) }

  private val closed = AtomicBoolean(false)

  override fun close() {
    if (closed.compareAndSet(false, true)) {
      try {
        closeHandler.run()
      } catch(ignored: Exception) {
        log.warn("AutoCloseable 인스턴스의 close() 호출에서 예외가 발생했습니다. 무시합니다.", ignored)
      }
    }
  }
}

/** Java 7 [AutoCloseable] 에 대해 close 메소드를 수행하도록 합니다 */
inline infix fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
  var closed = false

  try {
    return block(this)
  } catch(e: Throwable) {
    closed = true
    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    this?.closeSuppressed(e)
    throw e
  } finally {
    if (this != null && !closed)
      close()
  }
}

fun AutoCloseable.closeSafe(): Unit {
  try {
    close()
  } catch(e: Throwable) {
    // Ignore error
  }
}

internal fun AutoCloseable.closeSuppressed(cause: Throwable): Unit {
  try {
    close()
  } catch(ce: Throwable) {
  }
}