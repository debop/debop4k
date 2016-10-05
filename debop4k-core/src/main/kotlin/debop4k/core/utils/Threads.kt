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

/**
 * Threads
 * @author sunghyouk.bae@gmail.com
 */
object Threads {

  @JvmStatic
  @JvmOverloads
  fun sleep(ms: Long = Long.MAX_VALUE) {
    try {
      Thread.sleep(ms)
    } catch(ignored: InterruptedException) {
      // ignore
    }
  }

  @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
  @JvmStatic
  @JvmOverloads
  fun wait(obj: Any, timeout: Long = Long.MAX_VALUE) {
    synchronized(obj) {
      try {
        (obj as java.lang.Object).wait(timeout)
      } catch(ignored: InterruptedException) {
        // ignored
      }
    }
  }

  @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
  @JvmStatic
  fun notify(obj: Any) {
    synchronized(obj) {
      try {
        (obj as java.lang.Object).notify()
      } catch(ignored: InterruptedException) {
        // ignored
      }
    }
  }

  @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
  @JvmStatic
  fun notifyAll(obj: Any) {
    synchronized(obj) {
      try {
        (obj as java.lang.Object).notify()
      } catch(ignored: InterruptedException) {
        // ignored
      }
    }
  }

  @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
  @JvmStatic
  fun join(thread: Thread) {
    try {
      thread.join()
    } catch(ignored: InterruptedException) {
      // ignored
    }
  }

  @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
  @JvmStatic
  @JvmOverloads
  fun join(thread: Thread, millis: Long, nanos: Int = 0) {
    try {
      thread.join(millis, nanos)
    } catch(ignored: InterruptedException) {
      // ignored
    }
  }
}