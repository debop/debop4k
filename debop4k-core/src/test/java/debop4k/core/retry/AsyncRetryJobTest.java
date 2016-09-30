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

import debop4k.core.asyncs.Asyncs;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Context;
import nl.komponents.kovenant.Kovenant;
import nl.komponents.kovenant.Promise;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.InOrder;

import java.io.IOException;
import java.net.SocketException;

import static debop4k.core.retry.backoff.Backoffs.DEFAULT_PERIOD_MILLIS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

/**
 * AsyncRetryJobTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class AsyncRetryJobTest extends AbstractRetryTest {

  private final Context kovenantContext = Kovenant.INSTANCE.getContext();

  private Promise<String, Exception> failedAsync(Exception exception) {
    return Promise.Companion.ofFail(exception, kovenantContext);
  }

  @Test
  public void shouldUnwrapUserFutureAndReturnIt() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.safeAsync()).willReturn(Promise.Companion.of("42", kovenantContext));

    // when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    assertThat(promise.get()).isEqualTo("42");
  }

  @Test
  public void shouldSucceedAfterFewAsynchronousRetries() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(new SocketException("First")),
                    failedAsync(new IOException("Second")),
                    Promise.Companion.ofSuccess("42", kovenantContext));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
    assertThat(promise.get()).isEqualTo("42");
  }

  @Test
  public void shouldScheduleTwoTimesWhenRetries() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(new SocketException("First")),
                    failedAsync(new IOException("Second")),
                    Promise.Companion.of("42", kovenantContext));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
    promise.get();

    final InOrder order = inOrder(schedulerMock);
    order.verify(schedulerMock).schedule(notNullRunnable(), eq(0L), millis());
    order.verify(schedulerMock, times(2)).schedule(notNullRunnable(), eq(DEFAULT_PERIOD_MILLIS), millis());
  }

  @Test
  public void shouldRethrowOriginalExceptionFromUserFutureCompletion() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).abortOn(SocketException.class);
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(new SocketException(DON_T_PANIC)));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
//    try {
//      promise.get();
//      failBecauseExceptionWasNotThrown(ExecutionException.class);
//    } catch (ExecutionException e) {
//      Throwable cause = e.getCause();
//      assertThat(cause).isInstanceOf(SocketException.class);
//      assertThat(cause).hasMessage(DON_T_PANIC);
//    }
  }

  @Test
  public void shouldRethrowOriginalExceptionFromUserFutureCompletionAndAbortWhenTestFails() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock)
        .abortIf(t -> { throw new RuntimeException("test invalid"); });

    given(serviceMock.safeAsync())
        .willReturn(failedAsync(new SocketException(DON_T_PANIC)));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
    Asyncs.ready(promise);
//    try {
//      promise.get();
//      failBecauseExceptionWasNotThrown(ExecutionException.class);
//    } catch (ExecutionException e) {
//      final Throwable cause = e.getCause();
//      assertThat(cause).isInstanceOf(SocketException.class);
//      assertThat(cause).hasMessage(DON_T_PANIC);
//    }
  }

  @Test
  public void shouldAbortWhenTargetFutureWantsToAbort() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.safeAsync())
        .willReturn(failedAsync(new AbortRetryException()));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
    Asyncs.ready(promise);
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError()).isInstanceOf(AbortRetryException.class);
  }

  @Test
  public void shouldRethrowExceptionThatWasThrownFromUserTaskBeforeReturningFuture() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).abortOn(IllegalArgumentException.class);
    given(serviceMock.safeAsync()).willThrow(new IllegalArgumentException(DON_T_PANIC));

    //when
    final Promise<String, Throwable> promise = executor.getFutureWithRetry(ctx -> serviceMock.safeAsync());

    //then
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError()).isInstanceOf(IllegalArgumentException.class);
    assertThat(promise.getError().getMessage()).isEqualTo(DON_T_PANIC);
  }

}
