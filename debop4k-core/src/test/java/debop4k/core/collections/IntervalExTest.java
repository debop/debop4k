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

package debop4k.core.collections;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.list.primitive.IntInterval;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * IntervalExFunSuite
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class IntervalExTest extends AbstractCoreTest {

  @Test
  public void intInterval() {
    assertThat(Intervals.Ints.range(0, 100).size()).isEqualTo(101);

    List<IntArrayList> xs = Intervals.Ints.grouped(IntInterval.fromTo(0, 99), 10).toList();
    assertThat(xs.size()).isEqualTo(10);
    assertThat(xs.get(0)).isEqualTo(Intervals.Ints.range(0, 9));
    assertThat(xs.get(1)).isEqualTo(Intervals.Ints.range(10, 19));
    assertThat(xs.get(2)).isEqualTo(Intervals.Ints.range(20, 29));
    assertThat(xs.get(9)).isEqualTo(Intervals.Ints.range(90, 99));
  }

  @Test
  public void longInterval() {
    assertThat(Intervals.Longs.range(0, 100).size()).isEqualTo(101);

    List<LongArrayList> xs = Intervals.Longs.group(IntInterval.fromTo(0, 99), 10).toList();

    assertThat(xs.size()).isEqualTo(10);
    assertThat(xs.get(0)).isEqualTo(Intervals.Longs.range(0, 9));
    assertThat(xs.get(1)).isEqualTo(Intervals.Longs.range(10, 19));
    assertThat(xs.get(2)).isEqualTo(Intervals.Longs.range(20, 29));
    assertThat(xs.get(9)).isEqualTo(Intervals.Longs.range(90, 99));
  }
}
