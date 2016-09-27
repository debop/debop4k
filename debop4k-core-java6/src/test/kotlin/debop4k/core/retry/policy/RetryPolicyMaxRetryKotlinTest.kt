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

/**
 * RetryPolicyMaxRetryKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class RetryPolicyMaxRetryKotlinTest : AbstractRetryPolicyKotlinTest() {

  @Test
  fun shouldStopAfterConfiguredNumberOfRetries() {
    //given
    val retryPolicy = RetryPolicy().withMaxRetry(7)

    //when
    val firstRetry = retryPolicy.shouldContinue(retry(1))
    val lastRetry = retryPolicy.shouldContinue(retry(7))
    val tooManyRetries = retryPolicy.shouldContinue(retry(8))

    //then
    assertThat(firstRetry).isTrue()
    assertThat(lastRetry).isTrue()
    assertThat(tooManyRetries).isFalse()
  }
}