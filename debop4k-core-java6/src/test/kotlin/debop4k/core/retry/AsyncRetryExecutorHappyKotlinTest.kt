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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Matchers
import org.mockito.Mockito

/**
 * AsyncRetryExecutorHappyKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryExecutorHappyKotlinTest : AbstractRetryKotlinTest() {

  @Test
  fun shouldNotRetryIfCompletesAfterFirstExecution() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)

    // when
    executor.doWithRetry { ctx -> serviceMock.alwaysSucceeds(); }

    // then
    Mockito.verify(schedulerMock).schedule(notNullRunnable(), Matchers.eq(0L), millis())
    Mockito.verifyNoMoreInteractions(schedulerMock)
  }

  @Test
  fun shouldCallUserTaskOnlyOnceIfItDoesntFail() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)

    // when
    executor.doWithRetry { ctx -> serviceMock.alwaysSucceeds() }

    // then
    Mockito.verify(serviceMock).alwaysSucceeds()
  }

  @Test
  @Throws(Exception::class)
  fun shouldReturnResultOfFirstSucessfulCall() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock)
    BDDMockito.given(serviceMock.alwaysSucceeds()).willReturn(42)

    // when
    val promise = executor.getWithRetry { serviceMock.alwaysSucceeds() }

    // then
    assertThat(promise.get()).isEqualTo(42)
  }

  @Test
  @Throws(Exception::class)
  fun shouldReturnEvenIfNoRetryPolicy() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock).dontRetry()
    BDDMockito.given(serviceMock.alwaysSucceeds()).willReturn(42)

    // when
    val promise = executor.getWithRetry { serviceMock.alwaysSucceeds() }

    // then
    assertThat(promise.get()).isEqualTo(42)
  }
}