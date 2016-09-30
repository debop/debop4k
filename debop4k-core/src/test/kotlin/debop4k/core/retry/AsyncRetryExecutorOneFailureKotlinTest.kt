/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.retry

import debop4k.core.asyncs.ready
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import java.util.concurrent.*
import java.util.concurrent.atomic.*

/**
 * AsyncRetryExecutorOneFailureKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryExecutorOneFailureKotlinTest : AbstractRetryKotlinTest() {

  @Test
  @Throws(Exception::class)
  fun shouldNotRetryIfAbortThrown() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    given<String>(serviceMock.sometimesFails()).willThrow(AbortRetryException::class.java)

    //when
    executor.getWithRetry { serviceMock.sometimesFails() }

    //then
    verify<FaultyService>(serviceMock).sometimesFails()
  }

  @Test
  @Throws(Exception::class)
  fun shouldRethrowAbortExceptionIfFirstIterationThrownIt() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).withMaxRetry(3)
    given<String>(serviceMock.sometimesFails())
        .willThrow(RuntimeException::class.java)
//        .willThrow(AbortRetryException::class.java)

    //when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    promise.success {
      println("Success")
    }

    //then
//    assertThat(promise.isFailure()).isTrue()
//    try {
//      promise.get()
//      failBecauseExceptionWasNotThrown(ExecutionException::class.java)
//    } catch (e: ExecutionException) {
//      assertThat(e.cause).isInstanceOf(AbortRetryException::class.java)
//    }
  }

  @Test
  @Throws(Exception::class)
  fun shouldCompleteWithExceptionIfFirstIterationThrownIt() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock).dontRetry()
    given<String>(serviceMock.sometimesFails()).willThrow(IllegalStateException(DON_T_PANIC))

    //when
    val promise = executor.getWithRetry { serviceMock.sometimesFails() }

    //then
    val error = AtomicReference<Throwable>()
    promise.fail { exception -> error.set(exception) }
    promise.ready()
    assertThat(error.get())
        .isNotNull()
        .isInstanceOf(IllegalStateException::class.java)
        .hasMessage(DON_T_PANIC)
  }

  @Test
  @Throws(Exception::class)
  fun shouldRethrowLastThrownExceptionWhenAbortedInSubsequentIteration() {
    //given
    val executor = AsyncRetryExecutor(schedulerMock)
    given<String>(serviceMock.sometimesFails()).willThrow(
        IllegalArgumentException("First"),
        IllegalStateException("Second"),
        AbortRetryException())


    try {
      //when
      val promise = executor.getWithRetry { serviceMock.sometimesFails() }

      //then
      promise.ready()
      assertThat(promise.isFailure()).isTrue()
      try {
        promise.get()
        failBecauseExceptionWasNotThrown(ExecutionException::class.java)
      } catch (e: ExecutionException) {
        assertThat(e.cause)
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessage("Second")
      }

    } catch (e: Exception) {
      log.error("Error", e)
    }

  }
}