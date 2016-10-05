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

package debop4k.core.retry

import debop4k.core.loggerOf

/**
 * RetryPolicy
 * @author debop sunghyouk.bae@gmail.com
 */
open class RetryPolicy
@JvmOverloads constructor(val maxRetries: Int = Int.MAX_VALUE,
                          val retryOn: Set<Class<out Throwable>> = emptySet(),
                          val abortOn: Set<Class<out Throwable>> = emptySet(),
                          val retryPredicate: (Throwable?) -> Boolean = { false },
                          val abortPredicate: (Throwable?) -> Boolean = { false }) {

  private val log = loggerOf(javaClass)

  companion object {
    @JvmField val DEFAULT: RetryPolicy = RetryPolicy()

    private fun matches(exception: Class<out Throwable>, set: Set<Class<out Throwable>>): Boolean
        = set.isEmpty() || set.any { it.isAssignableFrom(exception) }
  }

  fun retryOn(vararg retryOnExceptions: Class<out Throwable>): RetryPolicy {
    val retries = retryOn + retryOnExceptions
    return RetryPolicy(maxRetries, retries, abortOn, retryPredicate, abortPredicate)
  }

  fun abortOn(vararg abortOnExceptions: Class<out Throwable>): RetryPolicy {
    val aborts = abortOn + abortOnExceptions
    log.debug("add abortOn. aborts={}", aborts)
    return RetryPolicy(maxRetries, retryOn, aborts, retryPredicate, abortPredicate)
  }

  fun retryIf(retryPredicate: (Throwable?) -> Boolean): RetryPolicy
      = RetryPolicy(maxRetries, retryOn, abortOn, retryPredicate, abortPredicate)

  fun abortIf(abortPredicate: (Throwable?) -> Boolean): RetryPolicy
      = RetryPolicy(maxRetries, retryOn, abortOn, retryPredicate, abortPredicate)

  fun dontRetry(): RetryPolicy
      = RetryPolicy(0, retryOn, abortOn, retryPredicate, abortPredicate)

  fun withMaxRetry(times: Int): RetryPolicy
      = RetryPolicy(times, retryOn, abortOn, retryPredicate, abortPredicate)

  fun shouldContinue(context: RetryContext): Boolean {
    if (tooManyRetries(context)) {
      return false
    }
    if (abortPredicate(context.lastThrowable)) {
      return false
    }
    if (retryPredicate(context.lastThrowable)) {
      return true
    }
    return exceptionClassRetryable(context)
  }

  private fun tooManyRetries(context: RetryContext): Boolean
      = context.retryCount > maxRetries


  private fun exceptionClassRetryable(context: RetryContext): Boolean {
    if (context.lastThrowable == null)
      return false

    val e = context.lastThrowable!!.javaClass

    if (abortOn.isEmpty()) {
      return matches(e, retryOn)
    }

    return !matches(e, abortOn) && matches(e, retryOn)
  }
}