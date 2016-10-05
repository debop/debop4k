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

package debop4k.core.retry;

import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static debop4k.core.retry.backoff.Backoffx.DEFAULT_PERIOD_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * AsyncRetryExecutorManualAbortTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class AsyncRetryExecutorManualAbortTest extends AbstractRetryTest {

  @Test
  public void shouldRethrowIfFirstExecutionThrowsAnExceptionAndNoRetry() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).dontRetry();
    BDDMockito.given(serviceMock.sometimesFails()).willThrow(new IllegalStateException(DON_T_PANIC));

    // when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    // then
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError()).isInstanceOf(IllegalStateException.class);
  }

  @Test
  public void shouldRetryAfterOneExceptionAndReturnValue() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.sometimesFails())
              .willThrow(IllegalStateException.class)
              .willReturn("Foo");

    // when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    // then
    assertThat(promise.get()).isEqualTo("Foo");
  }

  @Test
  public void shouldSucceedWhenOnlyOneRetryAllowed() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).withMaxRetry(1);
    BDDMockito.given(serviceMock.sometimesFails())
              .willThrow(IllegalStateException.class)
              .willReturn("Foo");

    // when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    // then
    assertThat(promise.get()).isEqualTo("Foo");
  }

  @Test
  public void shouldRetryOnceIfFirstExecutionThrowsException() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.sometimesFails())
              .willThrow(IllegalStateException.class)
              .willReturn("Foo");

    // when
    executor.getWithRetry(serviceMock::sometimesFails);

    // then
    verify(serviceMock, times(2)).sometimesFails();
  }

  @Test
  public void shouldScheduleRetryWithDefaultDelay() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.sometimesFails())
              .willThrow(IllegalStateException.class)
              .willReturn("Foo");

    // when
    executor.getWithRetry(serviceMock::sometimesFails);

    // then
    final InOrder order = inOrder(schedulerMock);
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis());
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(DEFAULT_PERIOD_MILLIS), millis());
    order.verifyNoMoreInteractions();
  }

  @Test
  public void shouldPassCorrectRetryCountToEachInvocationInContext() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.calculateSum(0)).willThrow(IllegalStateException.class);
    BDDMockito.given(serviceMock.calculateSum(1)).willReturn(BigDecimal.ONE);

    //when
    executor.getWithRetry(ctx -> serviceMock.calculateSum(ctx.getRetryCount()));

    //then
    final InOrder order = Mockito.inOrder(serviceMock);
    order.verify(serviceMock).calculateSum(0);
    order.verify(serviceMock).calculateSum(1);
    order.verifyNoMoreInteractions();
  }
}
