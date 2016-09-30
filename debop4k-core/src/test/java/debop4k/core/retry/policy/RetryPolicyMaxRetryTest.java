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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RetryPolicyMaxRetryTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
public class RetryPolicyMaxRetryTest extends AbstractRetryPolicyTest {


  @Test
  public void shouldStopAfterConfiguredNumberOfRetries() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().withMaxRetry(7);

    //when
    final boolean firstRetry = retryPolicy.shouldContinue(retry(1));
    final boolean lastRetry = retryPolicy.shouldContinue(retry(7));
    final boolean tooManyRetries = retryPolicy.shouldContinue(retry(8));

    //then
    assertThat(firstRetry).isTrue();
    assertThat(lastRetry).isTrue();
    assertThat(tooManyRetries).isFalse();
  }

}
