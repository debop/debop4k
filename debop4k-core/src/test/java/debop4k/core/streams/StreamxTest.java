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

package debop4k.core.streams;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.Test;

import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static debop4k.core.streams.Streamx.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.utility.internal.primitive.IntIterableIterate.forEach;

@Slf4j
public class StreamxTest extends AbstractCoreTest {

  @Test
  public void testAsIntArrayList() {
    IntArrayList arrayList = toIntArrayList(IntStream.range(0, 1000));
    assertThat(arrayList).isNotNull();
    assertThat(arrayList.size()).isEqualTo(1000);
  }

  @Test
  public void testAsLongArrayList() {
    LongArrayList arrayList = toLongArrayList(LongStream.range(0, 1000));
    assertThat(arrayList).isNotNull();
    assertThat(arrayList.size()).isEqualTo(1000);
  }


  @Test
  public void testAsDoubleArrayList() {
    DoubleArrayList arrayList = toDoubleArrayList(DoubleStream.of(0, 1, 2, 3, 4));
    assertThat(arrayList).isNotNull();
    assertThat(arrayList.size()).isEqualTo(5);
  }


  @Test
  public void testForEach() {
    int[] sum = new int[]{0};
    forEach(IntInterval.fromTo(0, 10), i -> sum[0] += i);
    assertThat(sum[0]).isEqualTo(55);
  }
}
