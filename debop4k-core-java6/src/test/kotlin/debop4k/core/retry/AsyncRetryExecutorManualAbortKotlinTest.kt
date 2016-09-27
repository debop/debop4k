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

import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import debop4k.core.asyncs.ready
import debop4k.core.retry.backoff.Backoffs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import java.math.BigDecimal

/**
 * AsyncRetryExecutorManualAbortKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryExecutorManualAbortKotlinTest : AbstractRetryKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun shouldRethrowIfFirstExecutionThrowsAnExceptionAndNoRetry() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock).dontRetry()
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(IllegalStateException(DON_T_PANIC))

    // when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    // then
    promise.ready()
    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError()).isInstanceOf(IllegalStateException::class.java)
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryAfterOneExceptionAndReturnValue() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(IllegalStateException::class.java).willReturn("Foo")

    // when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    // then
    assertThat(promise.get()).isEqualTo("Foo")
  }

  @Test
  @Throws(Exception::class)
  fun shouldSucceedWhenOnlyOneRetryAllowed() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock).withMaxRetry(1)
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(IllegalStateException::class.java).willReturn("Foo")

    // when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    // then
    assertThat(promise.get()).isEqualTo("Foo")
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryOnceIfFirstExecutionThrowsException() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(IllegalStateException::class.java).willReturn("Foo")

    // when
    executor.getWithRetry { serviceMock.sometimesFails() }

    // then
    verify<FaultyService>(serviceMock, times(2)).sometimesFails()
  }

  @Test
  @Throws(Exception::class)
  fun shouldScheduleRetryWithDefaultDelay() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(IllegalStateException::class.java).willReturn("Foo")

    // when
    executor.getWithRetry { serviceMock.sometimesFails() }

    // then
    val order = inOrder(schedulerMock)
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis())
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(Backoffs.DEFAULT_PERIOD_MILLIS), millis())
    order.verifyNoMoreInteractions()
  }

  @Test
  @Throws(Exception::class)
  fun shouldPassCorrectRetryCountToEachInvocationInContext() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.calculateSum(0)).willThrow(IllegalStateException::class.java)
    BDDMockito.given(serviceMock.calculateSum(1)).willReturn(BigDecimal.ONE)

    //when
    executor.getWithRetry { ctx -> serviceMock.calculateSum(ctx.retryCount) }

    //then
    val order = inOrder(serviceMock)
    order.verify(serviceMock).calculateSum(0)
    order.verify(serviceMock).calculateSum(1)
    order.verifyNoMoreInteractions()
  }

}