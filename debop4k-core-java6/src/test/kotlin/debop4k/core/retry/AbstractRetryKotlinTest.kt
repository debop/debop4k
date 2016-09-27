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

package debop4k.core.retry

import com.nhaarman.mockito_kotlin.mock
import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.uninitialized
import org.junit.Before
import org.mockito.BDDMockito.anyLong
import org.mockito.BDDMockito.given
import org.mockito.Matchers
import org.mockito.Matchers.eq
import org.mockito.Matchers.notNull
import java.util.concurrent.*

/**
 * AbstractRetryKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class AbstractRetryKotlinTest : AbstractCoreKotlinTest() {

  val DON_T_PANIC = "Don't panic!"

  val schedulerMock = mock<ScheduledExecutorService>()
  val serviceMock = mock<FaultyService>()

  @Before open fun setup(): Unit {
    setupMocks()
  }

  private fun setupMocks() {
    given(schedulerMock.schedule(notNullRunnable(), anyLong(), eq(TimeUnit.MILLISECONDS)))
        .willAnswer { invocation ->
          (invocation.arguments[0] as Runnable).run()
          return@willAnswer uninitialized()
        }
  }

  open protected fun notNullRunnable(): Runnable? = notNull() as Runnable?

  open protected fun notNullRetryContext(): RetryContext? = notNull() as RetryContext?

  open protected fun millis(): TimeUnit? = Matchers.eq(TimeUnit.MILLISECONDS)

  open protected fun anyRetry(): RetryContext = retry(1)

  open protected fun retry(times: Int): RetryContext
      = AsyncRetryContext(RetryPolicy.DEFAULT, times, Exception())
}