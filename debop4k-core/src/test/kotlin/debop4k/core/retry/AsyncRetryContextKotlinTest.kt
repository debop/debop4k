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

package debop4k.core.retry

import com.nhaarman.mockito_kotlin.doThrow
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.times
import debop4k.core.asyncs.ready
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Matchers.anyBoolean
import java.util.concurrent.*

/**
 * AsyncRetryContextKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncRetryContextKotlinTest : AbstractRetryKotlinTest() {

  val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(8)

  @Test
  fun shouldNotRetryIfRetriesForbidden() {
    // given
    val executor = AsyncRetryExecutor(scheduler).dontRetry()

    var retry = true
    // when
    val promise = executor.doWithRetry { ctx -> retry = ctx.willRetry }
    promise.ready()

    log.debug("ctx.willRetry={}", retry)
    assertThat(retry).isFalse()
  }

  @Test
  fun shouldSayItWillRetryIfUnlimitedNumberOfRetries() {
    // given
    val executor = AsyncRetryExecutor(scheduler)

    var retry = false
    val promise = executor.doWithRetry { ctx -> retry = ctx.willRetry }
    promise.ready()

    assertThat(retry).isTrue()
  }

  @Test
  fun shouldSayItWillRetryOnFirstFewCases() {
    // given
    val executor = AsyncRetryExecutor(schedulerMock).withMaxRetry(2)
    doThrow(IllegalStateException::class).`when`(serviceMock).withFlag(anyBoolean())

    // when
    val promise = executor.doWithRetry { ctx -> serviceMock.withFlag(ctx.willRetry) }

    promise.ready()

    // then
    val order = inOrder(serviceMock)
    order.verify(serviceMock, times(2)).withFlag(true)
    order.verify(serviceMock).withFlag(false)
    order.verifyNoMoreInteractions()

  }
}