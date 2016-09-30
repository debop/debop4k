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

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RetryPolicyDefaultsTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RetryPolicyDefaultsTest extends AbstractRetryPolicyTest {


  @Test
  public void byDefaultShouldRetryOnAllExceptions() throws Exception {
    assertThat(shouldRetryOn(new RetryPolicy(), new Exception())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new RuntimeException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new IOException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new ClassCastException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new NullPointerException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new IllegalArgumentException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new IllegalStateException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new TimeoutException())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new SocketException())).isTrue();
  }

  @Test
  public void byDefaultShouldRetryOnAllThrowables() throws Exception {
    assertThat(shouldRetryOn(new RetryPolicy(), new OutOfMemoryError())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new StackOverflowError())).isTrue();
    assertThat(shouldRetryOn(new RetryPolicy(), new NoClassDefFoundError())).isTrue();
  }

}
