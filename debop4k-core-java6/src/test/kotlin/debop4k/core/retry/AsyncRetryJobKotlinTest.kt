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
import debop4k.core.retry.backoff.Backoffs.DEFAULT_PERIOD_MILLIS
import nl.komponents.kovenant.Promise
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.*
import java.io.IOException
import java.net.SocketException

/**
 * AsyncRetryJobKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryJobKotlinTest : AbstractRetryKotlinTest() {

  private fun failedAsync(exception: Exception): Promise<String, Exception> {
    return Promise.ofFail<String, Exception>(exception)
  }

  @Test
  fun shouldUnwrapUserFutureAndReturnIt() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)
    given(serviceMock.safeAsync()).willReturn(Promise.of("42"))

    // when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    assertThat(promise.get()).isEqualTo("42")
  }

  @Test
  @Throws(Exception::class)
  fun shouldSucceedAfterFewAsynchronousRetries() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(SocketException("First")),
                    failedAsync(IOException("Second")),
                    Promise.ofSuccess("42"))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    //then
    assertThat(promise.get()).isEqualTo("42")
  }

  @Test
  @Throws(Exception::class)
  fun shouldScheduleTwoTimesWhenRetries() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(SocketException("First")),
                    failedAsync(IOException("Second")),
                    Promise.of("42"))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    //then
    promise.get()

    val order = inOrder(schedulerMock)
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis())
    order.verify(schedulerMock, times(2)).schedule(notNullRunnable(), eq(DEFAULT_PERIOD_MILLIS), millis())
  }

  @Test
  @Throws(Exception::class)
  fun shouldRethrowOriginalExceptionFromUserFutureCompletion() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).abortOn(SocketException::class.java)

    given(serviceMock.safeAsync())
        .willReturn(failedAsync(SocketException(DON_T_PANIC)))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    promise success {
      println("Success")
    }

    //then
//    promise.ready()
//    assertThat(promise.isFailure()).isTrue()
//    try {
//      promise.get()
//      failBecauseExceptionWasNotThrown(ExecutionException::class.java)
//    } catch (e: ExecutionException) {
//      val cause = e.cause
//      assertThat(cause)
//          .isInstanceOf(SocketException::class.java)
//          .hasMessage(DON_T_PANIC)
//    }
  }

  @Test
  @Throws(Exception::class)
  fun shouldRethrowOriginalExceptionFromUserFutureCompletionAndAbortWhenTestFails() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).abortIf { t -> throw RuntimeException("test invalid") }

    given(serviceMock.safeAsync())
        .willReturn(failedAsync(SocketException(DON_T_PANIC)))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    promise success {
      println("Success")
    }

    //then
//    promise.ready()
//    assertThat(promise.isFailure()).isTrue()
//    try {
//      promise.get()
//      failBecauseExceptionWasNotThrown(ExecutionException::class.java)
//    } catch(e: ExecutionException) {
//      assertThat(e.cause)
//          .isInstanceOf(SocketException::class.java)
//          .hasMessage(DON_T_PANIC)
//    }
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortWhenTargetFutureWantsToAbort() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    given(serviceMock.safeAsync()).willReturn(failedAsync(AbortRetryException()))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    //then
    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError()).isInstanceOf(AbortRetryException::class.java)
  }

  @Test
  @Throws(Exception::class)
  fun shouldRethrowExceptionThatWasThrownFromUserTaskBeforeReturningFuture() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).abortOn(IllegalArgumentException::class.java)
    given(serviceMock.safeAsync())
        .willThrow(IllegalArgumentException(DON_T_PANIC))

    //when
    val promise = executor.getFutureWithRetry { ctx -> serviceMock.safeAsync() }

    //then
    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException::class.java)
        .hasMessage(DON_T_PANIC)
  }
}