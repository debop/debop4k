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
import debop4k.core.retry.backoff.Backoffs
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import java.math.BigDecimal
import java.util.concurrent.*

/**
 * AsyncRetryExecutorManyFailuresKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryExecutorManyFailuresKotlinTest : AbstractRetryKotlinTest() {
  @Test
  @Throws(Exception::class)
  fun shouldRethrowIfFirstFewExecutionsThrow() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).withMaxRetry(2)
    BDDMockito
        .given<String>(serviceMock.sometimesFails())
        .willThrow(IllegalStateException(DON_T_PANIC))

    //when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }
    promise.success {
      println("Success")
    }

//    //then
//    promise.ready()
//    assertThat(promise.isFailure()).isTrue()
//    try {
//      promise.get()
//      failBecauseExceptionWasNotThrown(IllegalStateException::class.java)
//    } catch (t: ExecutionException) {
//      val actualCause = t.cause!!
//      assertThat(actualCause).isInstanceOf(IllegalStateException::class.java)
//      assertThat(actualCause.message).isEqualTo(DON_T_PANIC)
//    }
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryAfterManyExceptionsAndReturnValue() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito
        .given<String>(serviceMock.sometimesFails())
        .willThrow(IllegalStateException::class.java,
                   IllegalStateException::class.java,
                   IllegalStateException::class.java)
        .willReturn("Foo")

    //when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    //then
    assertThat(promise.get()).isEqualTo("Foo")
  }

  @Test
  @Throws(Exception::class)
  fun shouldSucceedWhenTheSameNumberOfRetriesAsFailuresAllowed() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).withMaxRetry(3)
    BDDMockito
        .given<String>(serviceMock.sometimesFails())
        .willThrow(IllegalStateException::class.java,
                   IllegalStateException::class.java,
                   IllegalStateException::class.java)
        .willReturn("Foo")

    //when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    //then
    assertThat(promise.get()).isEqualTo("Foo")
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryManyTimesIfFirstExecutionsThrowException() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito
        .given<String>(serviceMock.sometimesFails())
        .willThrow(IllegalStateException::class.java,
                   IllegalStateException::class.java,
                   IllegalStateException::class.java)
        .willReturn("Foo")

    //when
    executor.getWithRetry { serviceMock.sometimesFails() }

    //then
    verify<FaultyService>(serviceMock, times(4)).sometimesFails()
  }

  @Test
  @Throws(Exception::class)
  fun shouldScheduleRetryWithDefaultDelay() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito
        .given<String>(serviceMock.sometimesFails())
        .willThrow(IllegalStateException::class.java,
                   IllegalStateException::class.java,
                   IllegalStateException::class.java)
        .willReturn("Foo")

    //when
    executor.getWithRetry<String>(Callable<String> { serviceMock.sometimesFails() })

    //then
    val inOrder = inOrder(schedulerMock)
    inOrder.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis())
    inOrder.verify(schedulerMock, times(3)).schedule(notNullRunnable(), eq(Backoffs.DEFAULT_PERIOD_MILLIS), millis())
    inOrder.verifyNoMoreInteractions()
  }

  @Test
  @Throws(Exception::class)
  fun shouldPassCorrectRetryCountToEachInvocationInContext() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.calculateSum(0)).willThrow(IllegalStateException::class.java)
    BDDMockito.given(serviceMock.calculateSum(1)).willThrow(IllegalStateException::class.java)
    BDDMockito.given(serviceMock.calculateSum(2)).willThrow(IllegalStateException::class.java)
    BDDMockito.given(serviceMock.calculateSum(3)).willReturn(BigDecimal.ONE)

    //when
    executor.getWithRetry { ctx -> serviceMock.calculateSum(ctx.retryCount) }

    //then
    val order = inOrder(serviceMock)
    order.verify(serviceMock).calculateSum(0)
    order.verify(serviceMock).calculateSum(1)
    order.verify(serviceMock).calculateSum(2)
    order.verify(serviceMock).calculateSum(3)
    order.verifyNoMoreInteractions()
  }
}