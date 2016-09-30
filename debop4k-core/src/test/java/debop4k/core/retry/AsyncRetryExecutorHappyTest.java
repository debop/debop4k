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
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AsyncRetryExecutorHappyKotlinTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class AsyncRetryExecutorHappyTest extends AbstractRetryTest {

  @Test
  public void shouldNotRetryIfCompletesAfterFirstExecution() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);

    // when
    executor.doWithRetry(ctx -> {
      serviceMock.alwaysSucceeds();
      return null;
    });

    // then
    Mockito.verify(schedulerMock).schedule(notNullRunnable(), Matchers.eq(0L), millis());
    Mockito.verifyNoMoreInteractions(schedulerMock);
  }

  @Test
  public void shouldCallUserTaskOnlyOnceIfItDoesntFail() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);

    // when
    executor.doWithRetry(ctx -> {
      serviceMock.alwaysSucceeds();
      return null;
    });

    // then
    Mockito.verify(serviceMock).alwaysSucceeds();
  }

  @Test
  public void shouldReturnResultOfFirstSucessfulCall() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);
    BDDMockito.given(serviceMock.alwaysSucceeds()).willReturn(42);

    // when
    final Promise<Integer, Throwable> promise = executor.getWithRetry(serviceMock::alwaysSucceeds);

    // then
    assertThat(promise.get()).isEqualTo(42);
  }

  @Test
  public void shouldReturnEvenIfNoRetryPolicy() throws Exception {
    // given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).dontRetry();
    BDDMockito.given(serviceMock.alwaysSucceeds()).willReturn(42);

    // when
    final Promise<Integer, Throwable> promise = executor.getWithRetry(serviceMock::alwaysSucceeds);

    // then
    assertThat(promise.get()).isEqualTo(42);
  }
}
