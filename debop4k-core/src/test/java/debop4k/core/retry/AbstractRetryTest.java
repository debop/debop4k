/*
 *  Copyright (c) 2016. KESTI co, ltd
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

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

/**
 * AbstractRetryTest
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public abstract class AbstractRetryTest {

  public static final String DON_T_PANIC = "Don't panic!";

  @Mock
  protected ScheduledExecutorService schedulerMock;

  @Mock
  protected FaultyService serviceMock;

  @Before
  public void injectMocks() {
    MockitoAnnotations.initMocks(this);
    setupMocks();
  }

  private void setupMocks() {
    given(schedulerMock.schedule(notNullRunnable(), anyLong(), eq(TimeUnit.MILLISECONDS))).willAnswer(invocation -> {
      ((Runnable) invocation.getArguments()[0]).run();
      return null;
    });
  }

  protected Runnable notNullRunnable() {
    return (Runnable) notNull();
  }

  protected RetryContext notNullRetryContext() {
    return (RetryContext) notNull();
  }

  protected TimeUnit millis() {
    return eq(TimeUnit.MILLISECONDS);
  }

  protected RetryContext anyRetry() {
    return retry(1);
  }

  protected RetryContext retry(int ret) {
    return new AsyncRetryContext(RetryPolicy.DEFAULT, ret, new Exception());
  }

}
