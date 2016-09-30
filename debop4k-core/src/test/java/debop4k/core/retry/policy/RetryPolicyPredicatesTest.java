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

import debop4k.core.retry.AsyncRetryContext;
import debop4k.core.retry.RetryContext;
import debop4k.core.retry.RetryPolicy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * RetryPolicyPredicatesTest
 *
 * @author debop sunghyouk.bae@gmail.com
 */
@Slf4j
@SuppressWarnings("unchecked")
public class RetryPolicyPredicatesTest extends AbstractRetryPolicyTest {

  @Mock
  private RetryContext retryContextMock;

  @Test
  public void shouldAbortIfAbortPredicateTrue() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortIf(t -> true);

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void shouldRetryIfRetryPredicateTrue() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().retryIf(t -> true);

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isTrue();
  }

  @Test
  public void shouldRetryIfBothPredicatesAbstainButClassShouldRetry() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().retryIf(t -> false)
                                                     .abortIf(t -> false);
    given(retryContextMock.getLastThrowable()).willReturn(new RuntimeException());

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isTrue();
  }

  @Test
  public void shouldAbortIfBothPredicatesAbstainButClassShouldAbort() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortOn(NullPointerException.class)
                                                     .retryIf(t -> false)
                                                     .abortIf(t -> false);
    given(retryContextMock.getLastThrowable()).willReturn(new NullPointerException());

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void shouldRetryIfPredicateTrueEvenIfClassShouldAbort() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortOn(NullPointerException.class)
                                                     .retryIf(t -> true);
    given(retryContextMock.getLastThrowable()).willReturn(new NullPointerException());

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isTrue();
  }

  @Test
  public void shouldAbortIfPredicateTrueEvenIfClassShouldRetry() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().retryOn(NullPointerException.class)
                                                     .abortIf(t -> true);
    given(retryContextMock.getLastThrowable()).willReturn(new NullPointerException());

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void whenAbortAndRetryPredicatesBothYieldTrueThenAbortWins() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().retryOn(NullPointerException.class)
                                                     .retryIf(t -> t.getMessage().contains("Foo"))
                                                     .abortIf(t -> t.getMessage().contains("Foo"));
    given(retryContextMock.getLastThrowable()).willReturn(new NullPointerException("Foo"));

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void shouldProceedIfPredicateFalseAndChildAccepts() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortIf(t -> false);
    given(retryContextMock.getLastThrowable()).willReturn(new RuntimeException());

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isTrue();
  }

  @Test
  public void shouldAbortIfPredicateFalseButShouldNotRetry() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortIf(t -> false).dontRetry();

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void shouldAbortIfPredicateTrueButShouldNotRetry() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().retryIf(t -> true)
                                                     .dontRetry();
    given(retryContextMock.getLastThrowable()).willReturn(new NullPointerException());
    given(retryContextMock.getRetryCount()).willReturn(1);

    //when
    final boolean shouldRetry = retryPolicy.shouldContinue(retryContextMock);

    //then
    assertThat(shouldRetry).isFalse();
  }

  @Test
  public void shouldExamineExceptionAndDecide() throws Exception {
    //given
    final RetryPolicy retryPolicy = new RetryPolicy().abortIf(t -> t.getMessage().contains("abort"));

    //when
    final boolean abort = retryPolicy.shouldContinue(new AsyncRetryContext(retryPolicy, 1, new RuntimeException("abort")));
    final boolean retry = retryPolicy.shouldContinue(new AsyncRetryContext(retryPolicy, 1, new RuntimeException("normal")));

    //then
    assertThat(abort).isFalse();
    assertThat(retry).isTrue();
  }

}
