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

package debop4k.redisson.examples;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RBatch;
import org.redisson.api.RListAsync;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class RedissonBatchTest extends AbstractRedissonTest {

  @Test
  public void testBatchNPE() {
    RBatch batch = redisson.createBatch();
    batch.getBucket("A1").setAsync("001");
    batch.getBucket("A2").setAsync("001");
    batch.getBucket("A3").setAsync("001");
    batch.getKeys().deleteAsync("A1");
    batch.getKeys().deleteAsync("A2");

    List<?> result = batch.execute();
    for (Object item : result) {
      log.debug("item={}", item);
    }
  }

  @Test
  public void testBatchList() {
    RBatch b = redisson.createBatch();
    RListAsync<Integer> listAsync = b.getList("list");

    for (int i = 0; i < 540; i++) {
      listAsync.addAsync(i);
    }
    List<?> res = b.execute();
    assertThat(res.size()).isEqualTo(540);
  }

  @Test
  public void testBatchBigRequest() {
    RBatch batch = redisson.createBatch();

    for (int i = 0; i < 210; i++) {
      batch.getMap("test").fastPutAsync("1", "2");
      batch.getMap("test").fastPutAsync("2", "3");
      batch.getMap("test").putAsync("2", "5");
      batch.getAtomicLong("counter").incrementAndGetAsync();
      batch.getAtomicLong("counter").incrementAndGetAsync();
    }
    List<?> res = batch.execute();
    assertThat(res).hasSize(210 * 5);
  }
}
