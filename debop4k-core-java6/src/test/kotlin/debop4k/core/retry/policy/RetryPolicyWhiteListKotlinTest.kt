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
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.util.concurrent.*

/**
 * RetryPolicyWhiteListKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyWhiteListKotlinTest : AbstractRetryPolicyKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun retryOnExceptionExplicitly() {
    val policy = RetryPolicy().retryOn(Exception::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isTrue()
    assertThat(shouldRetryOn(policy, RuntimeException())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, ClassCastException())).isTrue()
    assertThat(shouldRetryOn(policy, NullPointerException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalArgumentException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isTrue()
    assertThat(shouldRetryOn(policy, TimeoutException())).isTrue()
    assertThat(shouldRetryOn(policy, SocketException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun retryOnExceptionShouldNotRetryOnError() {
    val policy = RetryPolicy().retryOn(Exception::class.java)

    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryOnOnlyOneSpecificException() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java)

    assertThat(shouldRetryOn(policy, OptimisticLockException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnOtherExceptionsIfOneGivenExplicitly() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isFalse()
    assertThat(shouldRetryOn(policy, RuntimeException())).isFalse()
    assertThat(shouldRetryOn(policy, IOException())).isFalse()
    assertThat(shouldRetryOn(policy, ClassCastException())).isFalse()
    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalArgumentException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isFalse()
    assertThat(shouldRetryOn(policy, TimeoutException())).isFalse()
    assertThat(shouldRetryOn(policy, SocketException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnErrorsIfExceptionGivenExplicitly() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java)

    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryOnAnyOfProvidedExceptions() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java).retryOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, OptimisticLockException())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryOnAnyOfProvidedExceptionsInOneList() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java, IOException::class.java)

    assertThat(shouldRetryOn(policy, OptimisticLockException())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnOtherExceptionsIfFewGivenExplicitly() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java).retryOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isFalse()
    assertThat(shouldRetryOn(policy, RuntimeException())).isFalse()
    assertThat(shouldRetryOn(policy, ClassCastException())).isFalse()
    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalArgumentException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isFalse()
    assertThat(shouldRetryOn(policy, TimeoutException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnOtherExceptionsIfFewGivenExplicitlyInOneList() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java, IOException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isFalse()
    assertThat(shouldRetryOn(policy, RuntimeException())).isFalse()
    assertThat(shouldRetryOn(policy, ClassCastException())).isFalse()
    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalArgumentException())).isFalse()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isFalse()
    assertThat(shouldRetryOn(policy, TimeoutException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnErrorsIfFewExceptionsGivenExplicitly() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java).retryOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnErrorsIfFewExceptionsGivenExplicitlyInOneList() {
    val policy = RetryPolicy().retryOn(OptimisticLockException::class.java, IOException::class.java)

    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, StackOverflowError())).isFalse()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryWhenSubclassOfGivenExceptionThrown() {
    val policy = RetryPolicy().retryOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, FileNotFoundException())).isTrue()
    assertThat(shouldRetryOn(policy, SocketException())).isTrue()
    assertThat(shouldRetryOn(policy, ConnectException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnSiblilngExceptions() {
    val policy = RetryPolicy().retryOn(FileNotFoundException::class.java)

    assertThat(shouldRetryOn(policy, SocketException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryOnSuperClassesOfGivenClass() {
    val policy = RetryPolicy().retryOn(FileNotFoundException::class.java)

    assertThat(shouldRetryOn(policy, IOException())).isFalse()
  }
}