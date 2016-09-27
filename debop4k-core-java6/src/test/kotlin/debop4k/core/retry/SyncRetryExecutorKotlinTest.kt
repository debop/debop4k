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

import debop4k.core.asyncs.ready
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.concurrent.atomic.*

/**
 * SyncRetryExecutorKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class SyncRetryExecutorKotlinTest : AbstractRetryKotlinTest() {

  private val executor: RetryExecutor = SyncRetryExecutor

  @Test
  fun shouldReturnPromiseWhenDoWithRetryCalled() {
    // given
    val mainThread = Thread.currentThread().name
    val poolThread = AtomicReference<String>()

    // when
    val promise = executor.doWithRetry { ctx -> poolThread.set(Thread.currentThread().name) }
    promise.get()
    assertThat(poolThread.get()).isEqualTo(mainThread)
  }

  @Test
  fun shouldWrapExceptionInPromiseRatherThanThrowingIt() {

    val promise = executor.doWithRetry { ctx -> throw IllegalArgumentException(DON_T_PANIC) }

    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException::class.java)
        .hasMessage(DON_T_PANIC)
  }

  @Test
  fun shouldReturnCompletedFutureWhenGetWithRetryCalled() {
    //given
    val mainThread = Thread.currentThread().name

    //when
    val promise = executor.getWithRetry { Thread.currentThread().name }

    //then
    assertThat(promise.get()).isEqualTo(mainThread)
  }

  @Test
  fun shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetry() {
    //given
    val block = { throw IllegalArgumentException(DON_T_PANIC) }
    val promise = executor.getWithRetry { ctx -> block() }

    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException::class.java)
        .hasMessage(DON_T_PANIC)
  }

  @Test
  fun shouldReturnCompletedFutureWhenGetWithRetryCalledContext() {
    //given
    val mainThread = Thread.currentThread().name

    //when
    val promise = executor.getWithRetry { ctx -> Thread.currentThread().name }

    //then
    assertThat(promise.get()).isEqualTo(mainThread)
  }

  @Test
  fun shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetryContext() {
    //given
    val block = { ctx: RetryContext -> throw IllegalArgumentException(DON_T_PANIC) }
    val promise = executor.getWithRetry { block.invoke(it) }

    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException::class.java)
        .hasMessage(DON_T_PANIC)
  }

  @Test
  fun shouldReturnCompletedFutureWhenGetWithRetryOnFutureCalled() {
    //given
    val mainThread = Thread.currentThread().name

    //when
    val result = executor.getFutureWithRetry { ctx -> Promise.of(Thread.currentThread().name) }

    //then
    assertThat(result.get()).isEqualTo(mainThread)
  }

  @Test
  fun shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetryOnFuture() {
    //given
    val block = { ctx: RetryContext ->
      val defer = deferred<String, Exception>(Kovenant.context)
      defer.reject(IllegalArgumentException(DON_T_PANIC))
      defer.promise
    }

    val promise = executor.getFutureWithRetry<String> { block(it) }

    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException::class.java)
        .hasMessage(DON_T_PANIC)
  }
}