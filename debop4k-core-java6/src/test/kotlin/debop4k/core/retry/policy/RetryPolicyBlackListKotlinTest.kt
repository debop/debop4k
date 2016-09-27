/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
import org.assertj.core.api.exception.RuntimeIOException
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.util.concurrent.*

/**
 * RetryPolicyBlackListKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyBlackListKotlinTest : AbstractRetryPolicyKotlinTest() {

  @Test
  fun shouldAbortOnSpecifiedException() {
    val policy = RetryPolicy().abortOn(ConnectException::class.java)

    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
  }

  @Test
  fun shouldRetryIfExceptionNotAborting() {
    val policy = RetryPolicy().abortOn(ConnectException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isTrue()
    assertThat(shouldRetryOn(policy, RuntimeException())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, SocketException())).isTrue()
    assertThat(shouldRetryOn(policy, ClassCastException())).isTrue()
    assertThat(shouldRetryOn(policy, NullPointerException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalArgumentException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isTrue()
    assertThat(shouldRetryOn(policy, TimeoutException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryIfErrorNotAborting() {
    val policy = RetryPolicy().abortOn(ConnectException::class.java)

    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isTrue()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isTrue()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isTrue()
    assertThat(shouldRetryOn(policy, RuntimeIOException(DON_T_PANIC))).isTrue()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isTrue()
  }

  @Test
  fun shouldAbortIfBlackListedException() {
    val policy = RetryPolicy().abortOn(NullPointerException::class.java)

    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
  }

  @Test
  fun shouldAbortOnSubclassesOfBlackListedException() {
    val policy = RetryPolicy().abortOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, FileNotFoundException())).isFalse()
    assertThat(shouldRetryOn(policy, SocketException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
  }

  @Test
  fun shouldAbortOnAnyBlackListedExceptions() {
    val policy = RetryPolicy()
        .abortOn(NullPointerException::class.java)
        .abortOn(OutOfMemoryError::class.java)
        .abortOn(StackOverflowError::class.java)

    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
  }

  @Test
  fun shouldAbortOnAnyBlackListedExceptionsInOneList() {
    val policy = RetryPolicy().abortOn(NullPointerException::class.java,
                                       OutOfMemoryError::class.java,
                                       StackOverflowError::class.java)

    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
  }

  @Test
  fun shouldAbortOnSubclassesOfAnyOfBlackListedExceptions() {
    val policy = RetryPolicy()
        .abortOn(IOException::class.java)
        .abortOn(RuntimeException::class.java)

    assertThat(shouldRetryOn(policy, FileNotFoundException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
  }

  @Test
  fun shouldAbortOnSubclassesOfAnyOfBlackListedExceptionsInOneList() {
    val policy = RetryPolicy().abortOn(IOException::class.java,
                                       RuntimeException::class.java)

    assertThat(shouldRetryOn(policy, FileNotFoundException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
  }
}