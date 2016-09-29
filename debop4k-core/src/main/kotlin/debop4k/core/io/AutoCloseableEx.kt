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

package debop4k.core.io

/**
 * Executes the given [block] function on this resource and then closes it down correctly whether an exception
 * is thrown or not.
 *
 * @param block a function to process this closable resource.
 * @return the result of [block] function on this closable resource.
 */
inline fun <T : AutoCloseable?, R> T.use(block: (T) -> R): R {
  var closed = false
  try {
    return block(this)
  } catch (e: Throwable) {
    closed = true
    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    this?.closeSuppressed(e)
    throw e
  } finally {
    if (this != null && !closed) {
      close()
    }
  }
}

/**
 * Closes this [AutoCloseable] suppressing possible exception or error thrown by [AutoCloseable.close] function.
 * The suppressed exception is added to the list of suppressed exceptions of [cause] exception.
 */
internal fun AutoCloseable.closeSuppressed(cause: Throwable): Unit {
  try {
    close()
  } catch (closeException: Throwable) {
    cause.addSuppressed(closeException)
  }
}

/*
// seems to have marginal use cases
public fun AutoCloseable.closeQuietly(): Throwable? {
    return try {
        close()
        null
    }
    catch (e: Throwable) {
        e
    }
}
*/