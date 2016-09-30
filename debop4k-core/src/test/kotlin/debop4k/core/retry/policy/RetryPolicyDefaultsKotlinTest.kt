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

package debop4k.core.retry.policy

import debop4k.core.retry.RetryPolicy
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException
import java.net.SocketException
import java.util.concurrent.*

/**
 * RetryPolicyDefaultsKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyDefaultsKotlinTest : AbstractRetryPolicyKotlinTest() {


  @Test
  fun byDefaultShouldRetryOnAllExceptions() {
    assertThat(shouldRetryOn(RetryPolicy(), Exception())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), RuntimeException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), IOException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), ClassCastException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), NullPointerException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), IllegalArgumentException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), IllegalStateException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), TimeoutException())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), SocketException())).isTrue()
  }

  @Test
  fun byDefaultShouldRetryOnAllThrowables() {
    assertThat(shouldRetryOn(RetryPolicy(), OutOfMemoryError())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), StackOverflowError())).isTrue()
    assertThat(shouldRetryOn(RetryPolicy(), NoClassDefFoundError())).isTrue()
  }

}