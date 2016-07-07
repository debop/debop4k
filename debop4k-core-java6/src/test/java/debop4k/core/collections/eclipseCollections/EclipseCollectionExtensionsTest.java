/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package debop4k.core.collections.eclipseCollections;

import kotlin.Pair;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.DoubleArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.FloatArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.list.mutable.primitive.LongArrayList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class EclipseCollectionExtensionsTest {

  @Test
  public void testArrayListOf() {
    IntArrayList intArrayList = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.intArrayListOf(1, 2, 3, 4, 5);
    assertThat(intArrayList.size()).isEqualTo(5);
    assertThat(intArrayList.contains(4)).isTrue();
    assertThat(intArrayList.contains(0)).isFalse();

    LongArrayList longArrayList = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.longArrayListOf(1, 2, 3, 4, 5);
    assertThat(longArrayList.size()).isEqualTo(5);
    assertThat(longArrayList.contains(4)).isTrue();
    assertThat(longArrayList.contains(0)).isFalse();

    FloatArrayList floatArrayList = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.floatArrayListOf(1, 2, 3, 4, 5);
    assertThat(floatArrayList.size()).isEqualTo(5);
    assertThat(floatArrayList.contains(4)).isTrue();
    assertThat(floatArrayList.contains(0)).isFalse();

    DoubleArrayList doubleArrayList = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.doubleArrayListOf(1, 2, 3, 4, 5);
    assertThat(doubleArrayList.size()).isEqualTo(5);
    assertThat(doubleArrayList.contains(4)).isTrue();
    assertThat(doubleArrayList.contains(0)).isFalse();
  }

  @Test
  public void testAsList() {
    List<Integer> ints = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.asList(debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.intArrayListOf(1, 2, 3, 4, 5));
    assertThat(ints).hasSize(5).contains(1, 2, 3, 4, 5);

    List<Long> longs = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.asList(debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.longArrayListOf(1, 2, 3, 4, 5));
    assertThat(longs).hasSize(5).contains(1L, 2L, 3L, 4L, 5L);
  }

  @Test
  public void testFastListOf() {
    FastList<Object> empty = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.fastListOf();
    assertThat(empty).isEmpty();

    FastList<Integer> ints = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.fastListOf(1, 2, 3, 4, 5);
    assertThat(ints).isNotEmpty().contains(1, 2, 3, 4, 5);
  }

  @Test
  public void testUnifiedSetOf() {
    UnifiedSet<Integer> set = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.unifiedSetOf(1, 2, 3, 3, 3);
    assertThat(set).hasSize(3).contains(1, 2, 3);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testUnifiedMapOf() {
    UnifiedMap<Integer, String> map = debop4k.core.collections.eclipseCollections.PrimitiveArraysKt.unifiedMapOf(new Pair(1, "a"), new Pair(2, "b"), new Pair(3, "c"));
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1)).isEqualTo("a");
    assertThat(map.get(2)).isEqualTo("b");
    assertThat(map.get(3)).isEqualTo("c");
  }
}
