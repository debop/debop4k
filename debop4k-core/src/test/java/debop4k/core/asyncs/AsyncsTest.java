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

package debop4k.core.asyncs;

import debop4k.core.AbstractCoreTest;
import kotlin.Unit;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.Callable;

import static debop4k.core.asyncs.Asyncs.ready;
import static debop4k.core.asyncs.Asyncs.result;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {AsyncExConfiguration.class})
public class AsyncsTest extends AbstractCoreTest {

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
    Promise<Unit, Exception> future = Asyncs.future(Asyncs.getEMPTY_RUNNABLE());
    ready(future);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testReady() {
    Promise<Unit, Exception> future = Asyncs.future(runnable);
    ready(future);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testReadyAll() {
    List<Promise<Unit, Exception>> futures = FastList.newList();
    for (int i = 0; i < 100; i++) {
      futures.add(Asyncs.future(runnable));
    }
    Asyncs.readyAll(futures);
    assertThat(futures.parallelStream().allMatch(promise -> promise.isDone())).isTrue();
  }

  @Test
  public void testResult() {
    Promise<Integer, Exception> future = Asyncs.future(callable);
    assertThat(result(future)).isEqualTo(1);
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void testResultAll() {
    List<Promise<Integer, Exception>> futures = FastList.newList();
    for (int i = 0; i < 100; i++) {
      futures.add(Asyncs.future(callable));
    }
    Iterable<Integer> results = Asyncs.resultAll(futures);

    for (Promise<Integer, Exception> future : futures) {
      assertThat(future.isDone()).isTrue();
    }
    for (Integer result : results) {
      assertThat(result).isEqualTo(1);
    }
  }
}
