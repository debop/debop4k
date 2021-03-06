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

import java.lang.Exception

/**
 * AsyncRetryContext
 * @author debop sunghyouk.bae@gmail.com
 */
open class AsyncRetryContext
@JvmOverloads constructor(val retryPolicy: RetryPolicy,
                          override val retryCount: Int = 0,
                          override val lastThrowable: Throwable? = null) : RetryContext {

  override val willRetry: Boolean
    get() = retryPolicy.shouldContinue(this.nextRetry(Exception()))

  fun nextRetry(cause: Throwable?): AsyncRetryContext
      = AsyncRetryContext(retryPolicy, retryCount + 1, cause)

  fun prevRetry(): AsyncRetryContext
      = AsyncRetryContext(retryPolicy, retryCount - 1, lastThrowable)

}