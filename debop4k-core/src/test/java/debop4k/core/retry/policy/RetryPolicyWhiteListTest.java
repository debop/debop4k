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

import debop4k.core.retry.RetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RetryPolicyWhiteListTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RetryPolicyWhiteListTest extends AbstractRetryPolicyTest {

  @Test
  public void retryOnExceptionExplicitly() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(Exception.class);

    assertThat(shouldRetryOn(policy, new Exception())).isTrue();
    assertThat(shouldRetryOn(policy, new RuntimeException())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isTrue();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalArgumentException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(policy, new TimeoutException())).isTrue();
    assertThat(shouldRetryOn(policy, new SocketException())).isTrue();
  }

  @Test
  public void retryOnExceptionShouldNotRetryOnError() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(Exception.class);

    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isFalse();
  }

  @Test
  public void shouldRetryOnOnlyOneSpecificException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class);

    assertThat(shouldRetryOn(policy, new OptimisticLockException())).isTrue();
  }

  @Test
  public void shouldNotRetryOnOtherExceptionsIfOneGivenExplicitly() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isFalse();
    assertThat(shouldRetryOn(policy, new RuntimeException())).isFalse();
    assertThat(shouldRetryOn(policy, new IOException())).isFalse();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isFalse();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalArgumentException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isFalse();
    assertThat(shouldRetryOn(policy, new TimeoutException())).isFalse();
    assertThat(shouldRetryOn(policy, new SocketException())).isFalse();
  }

  @Test
  public void shouldNotRetryOnErrorsIfExceptionGivenExplicitly() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class);

    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isFalse();
  }

  @Test
  public void shouldRetryOnAnyOfProvidedExceptions() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class)
                                                .retryOn(IOException.class);

    assertThat(shouldRetryOn(policy, new OptimisticLockException())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
  }

  @Test
  public void shouldRetryOnAnyOfProvidedExceptionsInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class, IOException.class);

    assertThat(shouldRetryOn(policy, new OptimisticLockException())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
  }

  @Test
  public void shouldNotRetryOnOtherExceptionsIfFewGivenExplicitly() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class)
                                                .retryOn(IOException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isFalse();
    assertThat(shouldRetryOn(policy, new RuntimeException())).isFalse();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isFalse();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalArgumentException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isFalse();
    assertThat(shouldRetryOn(policy, new TimeoutException())).isFalse();
  }

  @Test
  public void shouldNotRetryOnOtherExceptionsIfFewGivenExplicitlyInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class, IOException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isFalse();
    assertThat(shouldRetryOn(policy, new RuntimeException())).isFalse();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isFalse();
    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalArgumentException())).isFalse();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isFalse();
    assertThat(shouldRetryOn(policy, new TimeoutException())).isFalse();
  }

  @Test
  public void shouldNotRetryOnErrorsIfFewExceptionsGivenExplicitly() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class)
                                                .retryOn(IOException.class);

    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isFalse();
  }

  @Test
  public void shouldNotRetryOnErrorsIfFewExceptionsGivenExplicitlyInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(OptimisticLockException.class, IOException.class);

    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new StackOverflowError())).isFalse();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isFalse();
  }

  @Test
  public void shouldRetryWhenSubclassOfGivenExceptionThrown() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(IOException.class);

    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isTrue();
    assertThat(shouldRetryOn(policy, new SocketException())).isTrue();
    assertThat(shouldRetryOn(policy, new ConnectException())).isTrue();
  }

  @Test
  public void shouldNotRetryOnSiblilngExceptions() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(FileNotFoundException.class);

    assertThat(shouldRetryOn(policy, new SocketException())).isFalse();
  }

  @Test
  public void shouldNotRetryOnSuperClassesOfGivenClass() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(FileNotFoundException.class);

    assertThat(shouldRetryOn(policy, new IOException())).isFalse();
  }
}
