/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
 *
 */

package debop4k.core.asyncs;

import debop4k.core.AbstractCoreTest;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.Callable;

import static debop4k.core.asyncs.Asyncx.ready;
import static debop4k.core.asyncs.Asyncx.result;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AsyncExConfiguration.class})
public class AsyncxKtTest extends AbstractCoreTest {

  private Runnable runnable = new Runnable() {
    @Override
    public void run() {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  };

  private Callable<Integer> callable = new Callable<Integer>() {
    @Override
    public Integer call() {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 1;
    }
  };

  @Test
  public void emptyRunnable() {
    Promise<Unit, Exception> future = Asyncx.future(Asyncx.getEMPTY_RUNNABLE());
    ready(future);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testReady() {
    Promise<Unit, Exception> future = Asyncx.future(runnable);
    ready(future);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testReadyAll() {
    List<Promise<Unit, Exception>> futures = FastList.newList();
    for (int i = 0; i < 100; i++) {
      futures.add(Asyncx.future(runnable));
    }
    Asyncx.readyAll(futures);
    assertThat(futures.parallelStream().allMatch(promise -> promise.isDone())).isTrue();
  }

  @Test
  public void testResult() {
    Promise<Integer, Exception> future = Asyncx.future(callable);
    assertThat(result(future)).isEqualTo(1);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testResultAll() {
    List<Promise<Integer, Exception>> futures = FastList.newList();
    for (int i = 0; i < 100; i++) {
      futures.add(Asyncx.future(callable));
    }
    Iterable<Integer> results = Asyncx.resultAll(futures);

    for (Promise<Integer, Exception> future : futures) {
      assertThat(future.isDone()).isTrue();
    }
    for (Integer result : results) {
      assertThat(result).isEqualTo(1);
    }
  }
}
