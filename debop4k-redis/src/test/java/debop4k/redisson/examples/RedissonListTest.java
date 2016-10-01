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

import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;
import org.redisson.api.RList;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class RedissonListTest extends AbstractRedissonTest {

  @Test
  public void testTrim() {
    RList<String> list = redisson.getList("list1");
    list.add("1");
    list.add("2");
    list.add("3");
    list.add("4");
    list.add("5");
    list.add("6");

    list.trim(0, 3);
    assertThat(list).containsExactly("1", "2", "3", "4");
  }

  @Test
  public void testAllBigList() {
    int COUNT = 10000;
    List<String> newList = FastList.newList(COUNT);
    for (int i = 0; i < COUNT; i++) {
      newList.add("" + i);
    }

    RList<String> list = redisson.getList("list1");
    list.addAll(newList);

    list.add(3, "123123");

    assertThat(list.size()).isEqualTo(COUNT + 1);
  }
}
