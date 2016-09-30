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
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CollectionExTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class MapExTest extends AbstractCoreTest {

  int COUNT = 1000;
  Map<String, Integer> map = Maps.mutable.of(); //new HashMap<>();

  @Before
  public void before() {
    map.clear();
    for (int i = 0; i < COUNT; i++) {
      map.put(String.format("KEY-%08d", i), COUNT - i);
    }
  }

  @Test
  public void sortByKey() {
    Map<String, Integer> sorted = Collectionx.<String, Integer>sortByKey(map);
    assertThat(sorted.keySet()).isEqualTo(FastList.newList(sorted.keySet()).sortThis().toSet());
    assertThat(sorted.values()).isNotEqualTo(FastList.newList(sorted.values()).toSortedList());
  }

  @Test
  public void sortByKeyDesc() {
    Map<String, Integer> reversed = Collectionx.<String, Integer>sortByKeyDesc(map);
    assertThat(reversed.keySet()).isEqualTo(FastList.newList(reversed.keySet()).reverseThis().toSet());
    assertThat(reversed.values()).isNotEqualTo(FastList.newList(reversed.values()).reverseThis());
  }

  @Test
  public void sortByValue() {
    Map<String, Integer> sorted = Collectionx.sortByValue(map);
    assertThat(FastList.newList(sorted.values())).isEqualTo(FastList.newList(sorted.values()).sortThis());
    assertThat(FastList.newList(sorted.keySet())).isNotEqualTo(FastList.newList(sorted.keySet()).sortThis());
  }

  @Test
  public void sortByValueDesc() {
    Map<String, Integer> reversed = Collectionx.sortByValueDesc(map);
    assertThat(FastList.newList(reversed.values())).isEqualTo(FastList.newList(reversed.values()).sortThis().reverseThis());
    assertThat(FastList.newList(reversed.keySet())).isNotEqualTo(FastList.newList(reversed.keySet()).reverseThis());
  }

  @Test
  public void minMaxValue() {
    assertThat(Collectionx.<String, Integer>minByValue(map).getSecond()).isEqualTo(1);
    assertThat(Collectionx.<String, Integer>maxByValue(map).getSecond()).isEqualTo(COUNT);
  }
}
