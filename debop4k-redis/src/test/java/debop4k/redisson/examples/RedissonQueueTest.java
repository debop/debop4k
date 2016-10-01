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
import org.redisson.api.RQueue;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedissonQueueTest extends AbstractRedissonTest {

  @Test
  public void testAddOfferOrigin() {
    Queue<Integer> queue = new LinkedList<Integer>();
    queue.add(1);
    queue.offer(2);
    queue.add(3);
    queue.offer(4);

    assertThat(queue).containsExactly(1, 2, 3, 4);
    assertThat(queue.poll()).isEqualTo(1);
    assertThat(queue).containsExactly(2, 3, 4);
    assertThat(queue.element()).isEqualTo(2);
  }

  @Test
  public void testAddOffer() {
    RQueue<Integer> queue = redisson.getQueue("queue");
    queue.add(1);
    queue.offer(2);
    queue.add(3);
    queue.offer(4);

    assertThat(queue).containsExactly(1, 2, 3, 4);
    assertThat(queue.poll()).isEqualTo(1);
    assertThat(queue).containsExactly(2, 3, 4);
    assertThat(queue.element()).isEqualTo(2);
  }

  @Test
  public void testRemove() {
    RQueue<Integer> queue = redisson.getQueue("queue");
    queue.add(1);
    queue.add(2);
    queue.add(3);
    queue.add(4);

    queue.remove();
    queue.remove();

    assertThat(queue).containsExactly(3, 4);
    queue.remove();
    queue.remove();

    assertThat(queue.isEmpty()).isTrue();
  }

  @Test(expected = NoSuchElementException.class)
  public void testRemoveEmpty() {
    RQueue<Integer> queue = redisson.getQueue("queueEmpty");
    queue.remove();
  }
}
