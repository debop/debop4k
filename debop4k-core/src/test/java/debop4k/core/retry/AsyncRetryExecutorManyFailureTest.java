/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core.retry;

import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static debop4k.core.retry.backoff.Backoffs.DEFAULT_PERIOD_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * AsyncRetryExecutorManyFailureTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class AsyncRetryExecutorManyFailureTest extends AbstractRetryTest {

  @Test
  public void shouldRethrowIfFirstFewExecutionsThrow() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).withMaxRetry(2);
    given(serviceMock.sometimesFails()).willThrow(new IllegalStateException(DON_T_PANIC));

    //when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    //then
//    assertThat(promise.isFailure()).isTrue();
//    try {
//      promise.get();
//      Assertions.failBecauseExceptionWasNotThrown(IllegalStateException.class);
//    } catch (ExecutionException t) {
//      final Throwable actualCause = t.getCause();
//      assertThat(actualCause).isInstanceOf(IllegalStateException.class);
//      assertThat(actualCause.getMessage()).isEqualToIgnoringCase(DON_T_PANIC);
//    }
  }

  @Test
  public void shouldRetryAfterManyExceptionsAndReturnValue() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails())
        .willThrow(IllegalStateException.class, IllegalStateException.class, IllegalStateException.class)
        .willReturn("Foo");

    //when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    //then
    assertThat(promise.get()).isEqualTo("Foo");
  }

  @Test
  public void shouldSucceedWhenTheSameNumberOfRetriesAsFailuresAllowed() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).withMaxRetry(3);
    given(serviceMock.sometimesFails())
        .willThrow(IllegalStateException.class, IllegalStateException.class, IllegalStateException.class)
        .willReturn("Foo");

    //when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    //then
    assertThat(promise.get()).isEqualTo("Foo");
  }

  @Test
  public void shouldRetryManyTimesIfFirstExecutionsThrowException() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails())
        .willThrow(IllegalStateException.class, IllegalStateException.class, IllegalStateException.class)
        .willReturn("Foo");

    //when
    executor.getWithRetry(serviceMock::sometimesFails);

    //then
    verify(serviceMock, times(4)).sometimesFails();
  }

  @Test
  public void shouldScheduleRetryWithDefaultDelay() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails())
        .willThrow(IllegalStateException.class, IllegalStateException.class, IllegalStateException.class)
        .willReturn("Foo");

    //when
    executor.getWithRetry(serviceMock::sometimesFails);

    //then
    final InOrder inOrder = inOrder(schedulerMock);
    inOrder.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis());
    inOrder.verify(schedulerMock, times(3)).schedule(notNullRunnable(), eq(DEFAULT_PERIOD_MILLIS), millis());
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void shouldPassCorrectRetryCountToEachInvocationInContext() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.calculateSum(0)).willThrow(IllegalStateException.class);
    given(serviceMock.calculateSum(1)).willThrow(IllegalStateException.class);
    given(serviceMock.calculateSum(2)).willThrow(IllegalStateException.class);
    given(serviceMock.calculateSum(3)).willReturn(BigDecimal.ONE);

    //when
    executor.getWithRetry(ctx -> serviceMock.calculateSum(ctx.getRetryCount()));

    //then
    final InOrder order = Mockito.inOrder(serviceMock);
    order.verify(serviceMock).calculateSum(0);
    order.verify(serviceMock).calculateSum(1);
    order.verify(serviceMock).calculateSum(2);
    order.verify(serviceMock).calculateSum(3);
    order.verifyNoMoreInteractions();
  }
}
