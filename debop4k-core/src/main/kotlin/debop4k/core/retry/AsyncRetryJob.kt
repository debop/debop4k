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

import nl.komponents.kovenant.*
import java.lang.Exception

/**
 * 비동기 Retry Job 을 표현합니다.
 * @author debop sunghyouk.bae@gmail.com
 */
open class AsyncRetryJob<V>
@JvmOverloads constructor(val userTask: (RetryContext) -> Promise<V, Throwable>,
                          parent: AsyncRetryExecutor,
                          context: AsyncRetryContext = AsyncRetryContext(parent.retryPolicy),
                          deferred: Deferred<V, Throwable> = deferred<V, Throwable>()) :
    RetryJob<V>(context, parent, deferred) {

  override fun run(startTime: Long) {
    try {
      var stopTime = 0L
      userTask(context) always {
        stopTime = System.currentTimeMillis() - startTime
      } fail { e ->
        handleThrowable(e, stopTime)
      } success { result ->
        complete(result, stopTime)
      }
    } catch(e: Exception) {
      handleThrowable(e, System.currentTimeMillis() - startTime)
    }
  }

  override fun nextTask(nextRetryContext: AsyncRetryContext): RetryJob<V> {
    return AsyncRetryJob<V>(userTask, parent, nextRetryContext, deferred)
  }
}