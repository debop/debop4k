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

@file:JvmName("Retryx")

package debop4k.core.retry

import debop4k.core.uninitialized

/**
 * 재시도 작업
 */
@FunctionalInterface
interface RetryRunnable {
  fun run(context: RetryContext): Void
}

/**
 * 재시도 작업
 */
@FunctionalInterface
interface RetryCallable<V> {
  fun call(context: RetryContext): V
}

/**
 * 지정된 함수를 재시도 작업용 [retryRunnable]로 빌드합니다
 */
inline fun retryRunnable(crossinline action: (RetryContext) -> Unit): RetryRunnable {
  return object : RetryRunnable {
    override fun run(context: RetryContext): Void {
      action(context)
      return uninitialized()
    }
  }
}

/**
 * 지정된 함수를 재시도 작업용 [RetryCallable]로 빌드합니다
 */
inline fun <V> retryCallable(crossinline func: (RetryContext) -> V): RetryCallable<V> {
  return object : RetryCallable<V> {
    override fun call(context: RetryContext): V {
      return func.invoke(context)
    }
  }
}

