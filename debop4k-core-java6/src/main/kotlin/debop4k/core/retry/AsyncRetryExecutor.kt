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

package debop4k.core.retry

import debop4k.core.retry.backoff.*
import nl.komponents.kovenant.Promise
import org.slf4j.LoggerFactory
import java.util.concurrent.*

/**
 * AsyncRetryExecutor
 * @author debop sunghyouk.bae@gmail.com
 */
open class AsyncRetryExecutor(val scheduler: ScheduledExecutorService,
                              val retryPolicy: RetryPolicy = RetryPolicy.DEFAULT,
                              val backoff: Backoff = DEFAULT_BACKOFF,
                              val fixedDelay: Boolean = false) : RetryExecutor {

  constructor(scheduler: ScheduledExecutorService) : this(scheduler, RetryPolicy.DEFAULT, DEFAULT_BACKOFF, false)

  private val log = LoggerFactory.getLogger(javaClass)

  override fun doWithRetry(action: (RetryContext) -> Unit): Promise<Unit, Throwable> {
    return getWithRetry { action(it) }
  }

  override fun <V> getWithRetry(callable: Callable<V>): Promise<V, Throwable> {
    return getWithRetry { callable.call() }
  }

  override fun <V> getWithRetry(func: (RetryContext) -> V): Promise<V, Throwable> {
    return scheduleImmediately(createTask(func))
  }

  override fun <V> getFutureWithRetry(func: (RetryContext) -> Promise<V, Throwable>): Promise<V, Throwable> {
    return scheduleImmediately(createFutureTask(func))
  }

  /**
   * 작업을 수행합니다.
   */
  protected fun <V> scheduleImmediately(job: RetryJob<V>): Promise<V, Throwable> {
    scheduler.schedule(job, 0, TimeUnit.MILLISECONDS)
    return job.promise
  }

  private fun <V> createTask(func: (RetryContext) -> V): RetryJob<V>
      = SyncRetryJob(func, this)

  private fun <V> createFutureTask(func: (RetryContext) -> Promise<V, Throwable>): RetryJob<V>
      = AsyncRetryJob<V>(func, this)

  fun withScheduler(scheduler: ScheduledExecutorService): AsyncRetryExecutor
      = AsyncRetryExecutor(scheduler, retryPolicy, backoff, fixedDelay)

  fun withRetryPolicy(policy: RetryPolicy): AsyncRetryExecutor
      = AsyncRetryExecutor(scheduler, policy, backoff, fixedDelay)

  fun withBackoff(backoff: Backoff): AsyncRetryExecutor
      = AsyncRetryExecutor(scheduler, retryPolicy, backoff, fixedDelay)

  @JvmOverloads
  fun withExponentialBackoff(initialDelayMillis: Long, multiplier: Double = DEFAULT_MULTIPLIER): AsyncRetryExecutor {
    return withBackoff(ExponentialDelayBackoff(initialDelayMillis, multiplier))
  }

  @JvmOverloads
  fun withFixedBackoff(delayMillis: Long = DEFAULT_PERIOD_MILLIS): AsyncRetryExecutor {
    return withBackoff(FixedIntervalBackoff(delayMillis))
  }

  @JvmOverloads
  fun withFixedRate(fixedDelay: Boolean = true): AsyncRetryExecutor {
    return AsyncRetryExecutor(scheduler, retryPolicy, backoff, fixedDelay)
  }

  @JvmOverloads
  fun withUniformJitter(range: Long = DEFAULT_RANDOM_RANGE_MILLIS): AsyncRetryExecutor
      = withBackoff(backoff.withUniformJitter(range))

  @JvmOverloads
  fun withProportionalJitter(multiplier: Double = DEFAULT_MULTIPLIER): AsyncRetryExecutor
      = withBackoff(backoff.withProportionalJitter(multiplier))

  @JvmOverloads
  fun withMinDelay(minDelayMillis: Long = DEFAULT_MIN_DELAY_MILLIS): AsyncRetryExecutor
      = withBackoff(backoff.withMinDelay(minDelayMillis))

  @JvmOverloads
  fun withMaxDelay(maxDelayMillis: Long = DEFAULT_MAX_DELAY_MILLIS): AsyncRetryExecutor
      = withBackoff(backoff.withMaxDelay(maxDelayMillis))

  fun withMaxRetry(times: Int): AsyncRetryExecutor
      = withRetryPolicy(retryPolicy.withMaxRetry(times))

  fun dontRetry(): AsyncRetryExecutor
      = withRetryPolicy(retryPolicy.dontRetry())

  fun retryInfinitely(): AsyncRetryExecutor
      = withMaxRetry(Int.MAX_VALUE)

  fun withNoDelay(): AsyncRetryExecutor
      = withBackoff(FixedIntervalBackoff(0L))

  fun firstRetryNoDelay(): AsyncRetryExecutor
      = withBackoff(backoff.withFirstRetryNoDelay())


  fun retryOn(vararg retryOnExceptions: Class<out Exception>): AsyncRetryExecutor
      = this.withRetryPolicy(retryPolicy.retryOn(*retryOnExceptions))

  fun abortOn(vararg abortOnExceptions: Class<out Exception>): AsyncRetryExecutor
      = this.withRetryPolicy(retryPolicy.abortOn(*abortOnExceptions))

  fun retryIf(predicate: (Throwable?) -> Boolean): AsyncRetryExecutor
      = this.withRetryPolicy(retryPolicy.retryIf(predicate))

  fun abortIf(predicate: (Throwable?) -> Boolean): AsyncRetryExecutor
      = this.withRetryPolicy(retryPolicy.abortIf(predicate))
}