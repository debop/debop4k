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
import debop4k.core.retry.backoff.Backoff
import nl.komponents.kovenant.*
import org.joda.time.DateTime
import java.lang.Exception
import java.util.concurrent.*

/**
 * RetryJob
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class RetryJob<V>
@JvmOverloads constructor(val context: AsyncRetryContext,
                          val parent: AsyncRetryExecutor,
                          val deferred: Deferred<V, Throwable> = deferred<V, Throwable> {}) : Runnable {

  private val log = loggerOf(javaClass)

  val promise: Promise<V, Throwable>
    get() = deferred.promise

  final override fun run(): Unit {
    run(System.currentTimeMillis())
  }

  abstract fun run(startTime: Long): Unit

  abstract fun nextTask(nextRetryContext: AsyncRetryContext): RetryJob<V>

  protected fun complete(result: V, duration: Long): Unit {
    logSuccess(context, result, duration)
    deferred.resolve(result)
  }

  fun handleManualAbort(abortException: AbortRetryException): Unit {
    logAbort(context)
    deferred.reject(context.lastThrowable ?: abortException)
  }

  fun handleThrowable(t: Throwable, duration: Long): Unit {
    if (t is AbortRetryException) {
      handleManualAbort(t)
    } else {
      handleUserThrowable(t, duration)
    }
  }

  fun handleUserThrowable(t: Throwable, duration: Long): Unit {
    val nextRetryContext = context.nextRetry(t)

    try {
      retryOrAbort(t, duration, nextRetryContext)
    } catch(predicateError: Exception) {
      log.error("Threw while trying to decide on retry {} after {}",
                nextRetryContext.retryCount, duration, predicateError)
      deferred.reject(predicateError)
    }
  }

  fun retryOrAbort(t: Throwable, duration: Long, nextRetryContext: AsyncRetryContext): Unit {
    log.trace("retry or abort!!!...")

    if (parent.retryPolicy.shouldContinue(nextRetryContext)) {
      val delay = calculateNextDelay(duration, nextRetryContext, parent.backoff)
      retryWithDelay(nextRetryContext, delay, duration)
    } else {
      logFailure(nextRetryContext, duration)
      deferred.reject(t)
    }
  }

  fun calculateNextDelay(taskDurationMillis: Long, nextRetryContext: AsyncRetryContext, backoff: Backoff): Long {
    val delay = backoff.delayMillis(nextRetryContext)
    return delay - if (parent.fixedDelay) taskDurationMillis else 0L
  }

  fun retryWithDelay(nextRetryContext: AsyncRetryContext, delay: Long, duration: Long): Unit {
    val nextJob = nextTask(nextRetryContext)
    parent.scheduler.schedule(nextJob, delay, TimeUnit.MILLISECONDS)
    logRetry(nextRetryContext, delay, duration)
  }


  protected fun logSuccess(context: RetryContext, result: V, duration: Long): Unit {
    log.info("Successful after {} retries. took {} ms and returned:{}", context.retryCount, duration, result)
  }

  protected fun logAbort(context: RetryContext) {
    log.info("Aborted by user after {} retries", context.retryCount + 1)
  }

  protected fun logFailure(nextRetryContext: AsyncRetryContext, duration: Long) {
    log.info("Giving up after {} retries, last run took: {} ms, last exceptions: {}",
             context.retryCount, duration, nextRetryContext.lastThrowable)
  }

  protected fun logRetry(nextRetryContext: AsyncRetryContext, delay: Long, duration: Long): Unit {
    val nextRunDate = DateTime(System.currentTimeMillis() + delay)
    log.trace("Retry {} failed after {}ms, scheduled next retry in {} ms ({})",
              context.retryCount, duration, delay, nextRunDate, nextRetryContext.lastThrowable)
  }
}