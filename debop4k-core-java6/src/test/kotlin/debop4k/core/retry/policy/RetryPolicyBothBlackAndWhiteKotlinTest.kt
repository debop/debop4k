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

/**
 * RetryPolicyBothBlackAndWhiteKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyBothBlackAndWhiteKotlinTest : AbstractRetryPolicyKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun shouldRetryOnGivenException() {
    val policy = RetryPolicy()
        .retryOn(IOException::class.java)
        .abortOn(NullPointerException::class.java)

    assertThat(shouldRetryOn(policy, NullPointerException())).isFalse()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, ConnectException())).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortOnGivenException() {
    val policy = RetryPolicy()
        .abortOn(IOException::class.java)
        .retryOn(NullPointerException::class.java)

    assertThat(shouldRetryOn(policy, NullPointerException())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryUnlessGivenSubclass() {
    val policy = RetryPolicy()
        .retryOn(IOException::class.java)
        .abortOn(FileNotFoundException::class.java)

    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, SocketException())).isTrue()
    assertThat(shouldRetryOn(policy, ConnectException())).isTrue()
    assertThat(shouldRetryOn(policy, FileNotFoundException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryUnlessGivenSubclassWithReversedDeclarationOrder() {
    val policy = RetryPolicy()
        .abortOn(FileNotFoundException::class.java)
        .retryOn(IOException::class.java)

    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, SocketException())).isTrue()
    assertThat(shouldRetryOn(policy, ConnectException())).isTrue()
    assertThat(shouldRetryOn(policy, FileNotFoundException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldUnderstandManyWhiteAndBlackListedExceptions() {
    val policy = RetryPolicy()
        .retryOn(Exception::class.java)
        .retryOn(LinkageError::class.java)
        .abortOn(IncompatibleClassChangeError::class.java)
        .abortOn(ClassCastException::class.java)
        .abortOn(ConnectException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isTrue()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isTrue()
    assertThat(shouldRetryOn(policy, UnsupportedClassVersionError())).isTrue()

    assertThat(shouldRetryOn(policy, NoSuchFieldError())).isFalse()
    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, ClassCastException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldUnderstandManyWhiteAndBlackListedExceptionsInOneList() {
    val policy = RetryPolicy()
        .retryOn(Exception::class.java,
                 LinkageError::class.java)
        .abortOn(IncompatibleClassChangeError::class.java,
                 ClassCastException::class.java,
                 ConnectException::class.java)

    assertThat(shouldRetryOn(policy, Exception())).isTrue()
    assertThat(shouldRetryOn(policy, IOException())).isTrue()
    assertThat(shouldRetryOn(policy, IllegalStateException())).isTrue()
    assertThat(shouldRetryOn(policy, NoClassDefFoundError())).isTrue()
    assertThat(shouldRetryOn(policy, UnsupportedClassVersionError())).isTrue()

    assertThat(shouldRetryOn(policy, NoSuchFieldError())).isFalse()
    assertThat(shouldRetryOn(policy, OutOfMemoryError())).isFalse()
    assertThat(shouldRetryOn(policy, ClassCastException())).isFalse()
    assertThat(shouldRetryOn(policy, ConnectException())).isFalse()
  }

}