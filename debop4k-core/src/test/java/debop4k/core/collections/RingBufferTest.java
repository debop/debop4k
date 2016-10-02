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

package debop4k.core.collections;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertionx.assertThatThrownBy;

/**
 * RingBufferTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class RingBufferTest extends AbstractCoreTest {

  @Test
  public void testEmpty() {
    final RingBuffer<String> ring = new RingBuffer<String>(4);

    assertThat(ring.size()).isEqualTo(0);
    assertThat(ring.isEmpty()).isTrue();
    assertThat(ring.iterator().hasNext()).isFalse();

//    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
//      @Override
//      public void call() throws Throwable {
//        ring.get(0);
//      }
//    }).isInstanceOf(IndexOutOfBoundsException.class);
//    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
//      @Override
//      public void call() throws Throwable {
//        ring.next();
//      }
//    }).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  public void singleElement() {
    RingBuffer<String> ring = new RingBuffer<String>(4);
    ring.add("a");
    assertThat(ring.isEmpty()).isFalse();
    assertThat(ring.size()).isEqualTo(1);
    assertThat(ring.get(0)).isEqualTo("a");
    // assertThat(ring.toList()).contains("a");
    assertThat(ring.toList()).isEqualTo(FastList.newListWith("a"));
  }

  @Test
  public void multipleElement() {
    final RingBuffer<String> ring = new RingBuffer<String>(4);
    ring.addAll(Arrays.asList("a", "b", "c"));

    assertThat(ring.size()).isEqualTo(3);
    assertThat(ring.get(0)).isEqualTo("a");
    assertThat(ring.get(1)).isEqualTo("b");
    assertThat(ring.get(2)).isEqualTo("c");

    assertThat(ring.toList()).isEqualTo(FastList.newListWith("a", "b", "c"));

    assertThat(ring.next()).isEqualTo("a");
    assertThat(ring.size()).isEqualTo(2);
    assertThat(ring.next()).isEqualTo("b");
    assertThat(ring.size()).isEqualTo(1);
    assertThat(ring.next()).isEqualTo("c");
    assertThat(ring.size()).isEqualTo(0);

//    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
//      @Override
//      public void call() throws Throwable {
//        ring.next();
//      }
//    }).isInstanceOf(NoSuchElementException.class);
  }

  @Test
  public void handleOverwrite() {
    RingBuffer<String> ring = new RingBuffer<String>(4);
    ring.addAll(Arrays.asList("a", "b", "c", "d", "t", "f"));
    assertThat(ring.size()).isEqualTo(4);
    assertThat(ring.get(0)).isEqualTo("c");
    assertThat(ring.get(3)).isEqualTo("f");
    assertThat(ring.toList()).isEqualTo(FastList.newListWith("c", "d", "t", "f"));
  }

  @Test
  public void removeWhere() {
    RingBuffer<Integer> ring = new RingBuffer<Integer>(6);
    ring.addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    assertThat(ring.toList()).isEqualTo(FastList.newListWith(4, 5, 6, 7, 8, 9));

    ring.removeIf(new Predicate<Integer>() {
      @Override
      public boolean accept(Integer each) {
        return each % 3 == 0;
      }
    });
    assertThat(ring.toList()).isEqualTo(FastList.newListWith(4, 5, 7, 8));
  }
}
