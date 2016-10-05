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

import nl.komponents.kovenant.Promise
import java.util.concurrent.*

/**
 * 동기 방식의 작업 재시도 실행자
 *
 * @author debop sunghyouk.bae@gmail.com
 */
object SyncRetryExecutor : RetryExecutor {

  @JvmField val RETRY_CONTEXT = AsyncRetryContext(RetryPolicy.DEFAULT)

  override fun doWithRetry(action: (RetryContext) -> Unit): Promise<Unit, Throwable> {
    try {
      action(RETRY_CONTEXT)
      return Promise.ofSuccess(Unit)
    } catch(e: Exception) {
      return failedFuture(e)
    }
  }

  override fun <V> getWithRetry(callable: Callable<V>): Promise<V, Throwable> {
    try {
      return Promise.of(callable.call())
    } catch(e: Exception) {
      return failedFuture(e)
    }
  }

  override fun <V> getWithRetry(func: (RetryContext) -> V): Promise<V, Throwable> {
    try {
      return Promise.of(func(RETRY_CONTEXT))
    } catch(e: Exception) {
      return failedFuture(e)
    }
  }

  override fun <V> getFutureWithRetry(func: (RetryContext) -> Promise<V, Throwable>): Promise<V, Throwable> {
    try {
      return func(RETRY_CONTEXT)
    } catch(e: Exception) {
      return failedFuture(e)
    }
  }

  fun <T> failedFuture(e: Throwable): Promise<T, Throwable> {
    return Promise.ofFail(e)
  }
}