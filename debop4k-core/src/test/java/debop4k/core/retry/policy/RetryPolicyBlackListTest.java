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

package debop4k.core.retry.policy;

import debop4k.core.NotSupportedException;
import debop4k.core.retry.AbstractRetryTest;
import debop4k.core.retry.RetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.exception.RuntimeIOException;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RetryPolicyBlackListTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RetryPolicyBlackListTest extends AbstractRetryPolicyTest {

  @Test
  public void shouldAbortOnSpecifiedException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(ConnectException.class);

    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
  }

  @Test
  public void shouldRetryIfExceptionNotAborting() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(ConnectException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isTrue();
    assertThat(shouldRetryOn(policy, new RuntimeException())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new SocketException())).isTrue();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isTrue();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalArgumentException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(policy, new TimeoutException())).isTrue();
  }

  @Test
  public void shouldRetryIfErrorNotAborting() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(ConnectException.class);

    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isTrue();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isTrue();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isTrue();
    assertThat(shouldRetryOn(policy, new RuntimeIOException(AbstractRetryTest.DON_T_PANIC))).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(policy, new NotSupportedException())).isTrue();

  }

  @Test
  public void shouldAbortIfBlackListedException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().
                                                    abortOn(NullPointerException.class);

    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
  }

  @Test
  public void shouldAbortOnSubclassesOfBlackListedException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(IOException.class);

    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isFalse();
    assertThat(shouldRetryOn(policy, new SocketException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
  }

  @Test
  public void shouldAbortOnAnyBlackListedExceptions() throws Exception {
    final RetryPolicy policy = new RetryPolicy()
        .abortOn(NullPointerException.class)
        .abortOn(OutOfMemoryError.class)
        .abortOn(StackOverflowError.class);

    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
  }

  @Test
  public void shouldAbortOnAnyBlackListedExceptionsInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy()
        .abortOn(NullPointerException.class, OutOfMemoryError.class, StackOverflowError.class);

    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
  }

  @Test
  public void shouldAbortOnSubclassesOfAnyOfBlackListedExceptions() throws Exception {
    final RetryPolicy policy = new RetryPolicy()
        .abortOn(IOException.class)
        .abortOn(RuntimeException.class);

    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
  }

  @Test
  public void shouldAbortOnSubclassesOfAnyOfBlackListedExceptionsInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy()
        .abortOn(IOException.class, RuntimeException.class);

    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
  }
}
