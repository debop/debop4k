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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RetryPolicyBothBlackAndWhiteTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RetryPolicyBothBlackAndWhiteTest extends AbstractRetryPolicyTest {

  @Test
  public void shouldRetryOnGivenException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(IOException.class)
                                                .abortOn(NullPointerException.class);

    assertThat(shouldRetryOn(policy, new NullPointerException())).isFalse();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new ConnectException())).isTrue();
  }

  @Test
  public void shouldAbortOnGivenException() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(IOException.class)
                                                .retryOn(NullPointerException.class);

    assertThat(shouldRetryOn(policy, new NullPointerException())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
  }

  @Test
  public void shouldRetryUnlessGivenSubclass() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(IOException.class)
                                                .abortOn(FileNotFoundException.class);

    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new SocketException())).isTrue();
    assertThat(shouldRetryOn(policy, new ConnectException())).isTrue();
    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isFalse();
  }

  @Test
  public void shouldRetryUnlessGivenSubclassWithReversedDeclarationOrder() throws Exception {
    final RetryPolicy policy = new RetryPolicy().abortOn(FileNotFoundException.class)
                                                .retryOn(IOException.class);

    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new SocketException())).isTrue();
    assertThat(shouldRetryOn(policy, new ConnectException())).isTrue();
    assertThat(shouldRetryOn(policy, new FileNotFoundException())).isFalse();
  }

  @Test
  public void shouldUnderstandManyWhiteAndBlackListedExceptions() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(Exception.class)
                                                .retryOn(LinkageError.class)
                                                .abortOn(IncompatibleClassChangeError.class)
                                                .abortOn(ClassCastException.class)
                                                .abortOn(ConnectException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isTrue();
    assertThat(shouldRetryOn(policy, new UnsupportedClassVersionError())).isTrue();

    assertThat(shouldRetryOn(policy, new NoSuchFieldError())).isFalse();
    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
  }

  @Test
  public void shouldUnderstandManyWhiteAndBlackListedExceptionsInOneList() throws Exception {
    final RetryPolicy policy = new RetryPolicy().retryOn(Exception.class, LinkageError.class)
                                                .abortOn(IncompatibleClassChangeError.class,
                                                         ClassCastException.class,
                                                         ConnectException.class);

    assertThat(shouldRetryOn(policy, new Exception())).isTrue();
    assertThat(shouldRetryOn(policy, new IOException())).isTrue();
    assertThat(shouldRetryOn(policy, new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(policy, new NoClassDefFoundError())).isTrue();
    assertThat(shouldRetryOn(policy, new UnsupportedClassVersionError())).isTrue();

    assertThat(shouldRetryOn(policy, new NoSuchFieldError())).isFalse();
    assertThat(shouldRetryOn(policy, new OutOfMemoryError())).isFalse();
    assertThat(shouldRetryOn(policy, new ClassCastException())).isFalse();
    assertThat(shouldRetryOn(policy, new ConnectException())).isFalse();
  }

}
