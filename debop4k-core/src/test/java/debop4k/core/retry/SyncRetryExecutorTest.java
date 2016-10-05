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

import debop4k.core.asyncs.Asyncx;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Deferred;
import nl.komponents.kovenant.Kovenant;
import nl.komponents.kovenant.KovenantApi;
import nl.komponents.kovenant.Promise;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SyncRetryExecutorTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class SyncRetryExecutorTest extends AbstractRetryTest {

  private final RetryExecutor executor = SyncRetryExecutor.INSTANCE;

  @Test
  public void shouldReturnPromiseWhenDoWithRetryCalled() throws Exception {
    // given
    String mainThread = Thread.currentThread().getName();
    AtomicReference<String> poolThread = new AtomicReference<>();

    // when
    Promise<Unit, Throwable> promise = executor.doWithRetry(ctx -> {
      poolThread.set(Thread.currentThread().getName());
      return null;
    });
    promise.get();
    assertThat(poolThread.get()).isEqualTo(mainThread);
  }

  @Test
  public void shouldWrapExceptionInPromiseRatherThanThrowingIt() throws Exception {

    Promise<Unit, Throwable> promise = executor.doWithRetry(ctx -> {
      throw new IllegalArgumentException(DON_T_PANIC);
    });

    Asyncx.ready(promise);
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(DON_T_PANIC);
  }

  @Test
  public void shouldReturnCompletedFutureWhenGetWithRetryCalled() throws Exception {
    //given
    String mainThread = Thread.currentThread().getName();

    //when
    Promise<String, Throwable> promise = executor.getWithRetry(() -> Thread.currentThread().getName());

    //then
    assertThat(promise.get()).isEqualTo(mainThread);
  }

  @Test
  public void shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetry() throws Exception {
    //given
    Callable<Unit> block = () -> {
      throw new IllegalArgumentException(DON_T_PANIC);
    };
    Promise<Unit, Throwable> promise = executor.getWithRetry(block);

    Asyncx.ready(promise);
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(DON_T_PANIC);
  }

  @Test
  public void shouldReturnCompletedFutureWhenGetWithRetryCalledContext() throws Exception {
    //given
    String mainThread = Thread.currentThread().getName();

    //when
    Promise<String, Throwable> promise = executor.getWithRetry(ctx -> Thread.currentThread().getName());

    //then
    assertThat(promise.get()).isEqualTo(mainThread);
  }

  @Test
  public void shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetryContext() throws Exception {
    //given
    RetryCallable<Unit> block = (ctx) -> {
      throw new IllegalArgumentException(DON_T_PANIC);
    };
    Promise<Unit, Throwable> promise = executor.getWithRetry(block::call);

    Asyncx.ready(promise);
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(DON_T_PANIC);
  }

  @Test
  public void shouldReturnCompletedFutureWhenGetWithRetryOnFutureCalled() throws Exception {
    //given
    String mainThread = Thread.currentThread().getName();

    //when
    Promise<String, Throwable> result = executor.getFutureWithRetry(ctx -> {
      return Promise.Companion.of(Thread.currentThread().getName(), Kovenant.INSTANCE.getContext());
    });

    //then
    assertThat(result.get()).isEqualTo(mainThread);
  }

  @Test
  public void shouldWrapExceptionInFutureRatherThanThrowingItInGetWithRetryOnFuture() throws Exception {
    //given
    RetryCallable<Promise<String, Throwable>> block = ctx -> {
      final Deferred<String, Throwable> defer = KovenantApi.deferred(Kovenant.INSTANCE.getContext());
      defer.reject(new IllegalArgumentException(DON_T_PANIC));
      return defer.getPromise();
    };

    Promise<String, Throwable> promise = executor.getFutureWithRetry(block::call);

    Asyncx.ready(promise);
    assertThat(promise.isFailure()).isTrue();
    assertThat(promise.getError())
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(DON_T_PANIC);
  }
}
