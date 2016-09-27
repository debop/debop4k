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

import com.nhaarman.mockito_kotlin.mock
import debop4k.core.retry.AsyncRetryContext
import debop4k.core.retry.RetryContext
import debop4k.core.retry.RetryPolicy
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given

/**
 * RetryPolicyPredicatesKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyPredicatesKotlinTest : AbstractRetryPolicyKotlinTest() {

  private val retryContextMock: RetryContext = mock()

  @Test
  @Throws(Exception::class)
  fun shouldAbortIfAbortPredicateTrue() {
    //given
    val retryPolicy = RetryPolicy().abortIf { t -> true }

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryIfRetryPredicateTrue() {
    //given
    val retryPolicy = RetryPolicy().retryIf { t -> true }

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryIfBothPredicatesAbstainButClassShouldRetry() {
    //given
    val retryPolicy = RetryPolicy()
        .retryIf { t -> false }
        .abortIf { t -> false }
    given<Throwable>(retryContextMock.lastThrowable).willReturn(RuntimeException())

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortIfBothPredicatesAbstainButClassShouldAbort() {
    //given
    val retryPolicy = RetryPolicy()
        .abortOn(NullPointerException::class.java)
        .retryIf { t -> false }.abortIf { t -> false }

    given<Throwable>(retryContextMock.lastThrowable).willReturn(NullPointerException())

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRetryIfPredicateTrueEvenIfClassShouldAbort() {
    //given
    val retryPolicy = RetryPolicy()
        .abortOn(NullPointerException::class.java)
        .retryIf { t -> true }

    given<Throwable>(retryContextMock.lastThrowable).willReturn(NullPointerException())

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortIfPredicateTrueEvenIfClassShouldRetry() {
    //given
    val retryPolicy = RetryPolicy()
        .retryOn(NullPointerException::class.java)
        .abortIf { t -> true }

    given<Throwable>(retryContextMock.lastThrowable).willReturn(NullPointerException())

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun whenAbortAndRetryPredicatesBothYieldTrueThenAbortWins() {
    //given
    val retryPolicy = RetryPolicy()
        .retryOn(NullPointerException::class.java)
        .retryIf { t -> t?.message?.contains("Foo") ?: false }
        .abortIf { t -> t?.message?.contains("Foo") ?: false }

    given<Throwable>(retryContextMock.lastThrowable).willReturn(NullPointerException("Foo"))

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldProceedIfPredicateFalseAndChildAccepts() {
    //given
    val retryPolicy = RetryPolicy().abortIf { t -> false }
    given<Throwable>(retryContextMock.lastThrowable).willReturn(RuntimeException())

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isTrue()
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortIfPredicateFalseButShouldNotRetry() {
    //given
    val retryPolicy = RetryPolicy().abortIf { t -> false }.dontRetry()

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldAbortIfPredicateTrueButShouldNotRetry() {
    //given
    val retryPolicy = RetryPolicy().retryIf { t -> true }.dontRetry()
    given<Throwable>(retryContextMock.lastThrowable).willReturn(NullPointerException())
    given<Int>(retryContextMock.retryCount).willReturn(1)

    //when
    val shouldRetry = retryPolicy.shouldContinue(retryContextMock)

    //then
    assertThat(shouldRetry).isFalse()
  }

  @Test
  @Throws(Exception::class)
  fun shouldExamineExceptionAndDecide() {
    //given
    val retryPolicy = RetryPolicy().abortIf { t -> t?.message?.contains("abort") ?: false }

    //when
    val abort = retryPolicy.shouldContinue(AsyncRetryContext(retryPolicy, 1, RuntimeException("abort")))
    val retry = retryPolicy.shouldContinue(AsyncRetryContext(retryPolicy, 1, RuntimeException("normal")))

    //then
    assertThat(abort).isFalse()
    assertThat(retry).isTrue()
  }
}