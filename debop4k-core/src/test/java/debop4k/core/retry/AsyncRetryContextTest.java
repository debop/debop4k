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

package debop4k.core.retry;

import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;

/**
 * AsyncRetryContextTest
 *
 * @author sunghyouk.bae@gmail.com
 */
public class AsyncRetryContextTest extends AbstractRetryTest {

  @Test
  public void shouldNotRetryIfRetriesForbidden() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).dontRetry();

    //when
    executor.doWithRetry(context -> {
      serviceMock.withFlag(context.getWillRetry());
      return null;
    });

    //then
    verify(serviceMock).withFlag(false);
  }

  @Test
  public void shouldSayItWillRetryIfUnlimitedNumberOfRetries() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock);

    //when
    executor.doWithRetry(context -> {
      serviceMock.withFlag(context.getWillRetry());
      return null;
    });

    //then
    verify(serviceMock).withFlag(true);
  }

  @Test
  public void shouldSayItWillRetryOnFirstFewCases() throws Exception {
    //given
    final RetryExecutor executor = new AsyncRetryExecutor(schedulerMock).withMaxRetry(2);
    doThrow(IllegalStateException.class).when(serviceMock).withFlag(anyBoolean());

    //when
    executor.doWithRetry(context -> {
      serviceMock.withFlag(context.getWillRetry());
      return null;
    });

    //then
    final InOrder order = inOrder(serviceMock);
    order.verify(serviceMock, times(2)).withFlag(true);
    order.verify(serviceMock).withFlag(false);
    order.verifyNoMoreInteractions();
  }

}
