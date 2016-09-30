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
import nl.komponents.kovenant.Promise;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * AsyncRetryExecutorOneFailureTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class AsyncRetryExecutorOneFailureTest extends AbstractRetryTest {

  @Test
  public void shouldNotRetryIfAbortThrown() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails()).willThrow(AbortRetryException.class);

    //when
    executor.getWithRetry(serviceMock::sometimesFails);

    //then
    verify(serviceMock).sometimesFails();
  }

  @Test
  public void shouldRethrowAbortExceptionIfFirstIterationThrownIt() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails()).willThrow(AbortRetryException.class);

    //when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    //then
//    assertThat(promise.isFailure()).isTrue();
//    try {
//      promise.get();
//      failBecauseExceptionWasNotThrown(ExecutionException.class);
//    } catch (ExecutionException e) {
//      assertThat(e.getCause()).isInstanceOf(AbortRetryException.class);
//    }
  }

  @Test
  public void shouldCompleteWithExceptionIfFirstIterationThrownIt() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).dontRetry();
    given(serviceMock.sometimesFails()).willThrow(new IllegalStateException(DON_T_PANIC));

    //when
    final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

    //then
    AtomicReference<Throwable> error = new AtomicReference<>();
    promise.fail(exception -> {
      error.set(exception);
      return null;
    });
    Asyncs.ready(promise);
    assertThat(error.get())
        .isNotNull()
        .isInstanceOf(IllegalStateException.class)
        .hasMessage(DON_T_PANIC);
  }

  @Test
  public void shouldRethrowLastThrownExceptionWhenAbortedInSubsequentIteration() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    given(serviceMock.sometimesFails()).willThrow(
        new IllegalArgumentException("First"),
        new IllegalStateException("Second"),
        new AbortRetryException());


    try {
      //when
      final Promise<String, Throwable> promise = executor.getWithRetry(serviceMock::sometimesFails);

      //then
      Asyncs.ready(promise);
      assertThat(promise.isFailure()).isTrue();
      try {
        promise.get();
        failBecauseExceptionWasNotThrown(ExecutionException.class);
      } catch (ExecutionException e) {
        assertThat(e.getCause()).isInstanceOf(IllegalStateException.class);
        assertThat(e.getCause().getMessage()).isEqualTo("Second");
      }
    } catch (Exception e) {
      log.error("Error", e);
    }
  }
}
