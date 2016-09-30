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
import debop4k.core.utils.Convertx;
import debop4k.core.utils.Stringx;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.function.primitive.LongFunction;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static debop4k.core.collections.Arrayx.isNullOrEmpty;
import static kotlin.collections.ArraysKt.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ArraysTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class ArrayxTest extends AbstractCoreTest {

  @Test
  public void testEmpty() {
    byte[] array = new byte[0];
    assertThat(isNullOrEmpty(array)).isTrue();

    byte[] array2 = new byte[]{1, 2};
    assertThat(isNullOrEmpty(array2)).isFalse();
  }

  @Test
  public void containsPrimitives() {
    int[] array = {1, 2, 3, 4, 5};
    assertThat(contains(array, 5)).isTrue();
    assertThat(contains(array, 9)).isFalse();
  }

  @Test
  public void containsObject() {
    String[] array = {"1", "2", "3", "4", "5"};
    assertThat(contains(array, "5")).isTrue();
    assertThat(contains(array, "9")).isFalse();
  }

  @Test
  public void indexOfPrimitivies() {
    int[] array = {1, 2, 3, 1, 2, 3, 4, 2, 5};
    assertThat(indexOf(array, 3)).isEqualTo(2);
    assertThat(indexOf(array, 0)).isEqualTo(-1);
  }

  @Test
  public void indexOfObjects() {
    String[] array = {"1", "2", "3", "1", "2", "3", "4", "2", "5"};
    assertThat(indexOf(array, "3")).isEqualTo(2);
    assertThat(indexOf(array, "0")).isEqualTo(-1);
  }

  @Test
  public void lastIndexOfPrimitivies() {
    int[] array = {1, 2, 3, 1, 2, 3, 4, 2, 5};
    assertThat(lastIndexOf(array, 3)).isEqualTo(5);
    assertThat(lastIndexOf(array, 2)).isEqualTo(7);
    assertThat(lastIndexOf(array, 0)).isEqualTo(-1);
  }

  @Test
  public void lastIndexOfObjects() {
    String[] array = {"1", "2", "3", "1", "2", "3", "4", "2", "5"};
    assertThat(lastIndexOf(array, "3")).isEqualTo(5);
    assertThat(lastIndexOf(array, "2")).isEqualTo(7);
    assertThat(lastIndexOf(array, "0")).isEqualTo(-1);
  }

  private List<Integer> makeBuffer(int size) {
    List<Integer> buffer = FastList.newList(100);
    for (int i = 0; i < size; i++) {
      buffer.add(i);
    }
    return buffer;
  }

  @Test
  public void iterableToIntArray() {
    List<Integer> buffer = makeBuffer(100);

    int[] array = Collectionx.asIntArray(buffer);
    assertThat(array.length).isEqualTo(buffer.size());
    assertThat(array[0]).isEqualTo(0);
    assertThat(array[50]).isEqualTo(50);
  }

  @Test
  public void iterableToArray() {
    List<Integer> buffer = makeBuffer(100);

    Object[] array = buffer.toArray(); // Collectionx.asArray(buffer);
    assertThat(array.length).isEqualTo(buffer.size());
    assertThat(array[0]).isEqualTo(0);
    assertThat(array[50]).isEqualTo(50);
  }

  @Test
  public void setToIntArray() {
    Set<Integer> set = Sets.mutable.of(1, 2, 3);
    int[] array = Collectionx.asIntArray(set);

    assertThat(array).hasSize(3);
    assertThat(array).contains(1, 2, 3);

    Object[] array2 = set.toArray();
    assertThat(array2).contains(1, 2, 3);
  }

  @Test
  public void setToArrayObject() {
    Set<String> set = Sets.mutable.of("a", "c", "b");

    Object[] array = set.toArray();

    assertThat(array).hasSize(3)
                     .contains("a", "b", "c");

    Object[] array2 = set.toArray();
    assertThat(array2).hasSize(3)
                      .contains("a", "b", "c");
  }

  @Test
  public void testAsString() {
    String str = Stringx.join(FastList.newListWith("a", "b", "c"));
    assertThat(str).isNotEmpty()
                   .isEqualTo("a,b,c");
  }

  @Test
  public void testGenerate() {
    int[] intArray = Arrayx.generateIntArray(100, new IntFunction<Integer>() {
      @Override
      public int intValueOf(Integer anObject) {
        return Convertx.asInt(anObject);
      }
    });
    assertThat(intArray).hasSize(100);
    assertThat(intArray[0]).isEqualTo(0);
    assertThat(intArray[99]).isEqualTo(99);

    long[] longArray = Arrayx.generateLongArray(100, new LongFunction<Integer>() {
      @Override
      public long longValueOf(Integer value) {
        return Convertx.asLong(value);
      }
    });
    assertThat(longArray).hasSize(100);
    assertThat(longArray[0]).isEqualTo(0);
    assertThat(longArray[99]).isEqualTo(99);

    int[] intArray2 = Arrayx.generateIntArray(100, new IntFunction<Integer>() {
      @Override
      public int intValueOf(Integer anObject) {
        return Convertx.asInt(anObject) * 2;
      }
    });
    assertThat(intArray2).hasSize(100);
    assertThat(intArray2[0]).isEqualTo(0);
    assertThat(intArray2[99]).isEqualTo(198);

  }
}
