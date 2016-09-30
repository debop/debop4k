/*
 * Copyright 2015-2020 KESTI s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.utils;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.tuple.Tuples;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
public class HashExTest extends AbstractCoreTest {

  @Test
  public void primitiveHashs() {
    int v12 = Hashx.compute(1, 2);
    int v21 = Hashx.compute(2, 1);
    int v22 = Hashx.compute(2, 2);
    assertThat(v12).isNotEqualTo(v21);
    assertThat(v12).isNotEqualTo(v22);
    assertThat(v21).isNotEqualTo(v22);
  }

  @Test
  public void referenceHashs() {
    int h1 = Hashx.compute("1", null);
    int h2 = Hashx.compute(null, "1");
    log.debug("h1={}, h2={}", h1, h2);
    assertThat(h1).isNotEqualTo(h2);
  }

  @Test
  public void referenceHash2() {
    Date date = new Date();
    int h1 = Hashx.compute(date, null);
    int h2 = Hashx.compute(null, date);
    assertThat(h1).isNotEqualTo(h2);
  }


  // NOTE: Objects.hash 와 Arrays.hash 는 버그가 있다.
  //
  @Test(expected = AssertionError.class)
  public void primitiveHashByObjectsHash() {
    int h1 = Objects.hash(1, 0);
    int h2 = Objects.hash(0, 31);
    assertThat(h1).isNotEqualTo(h2);
  }

  @Test
  public void primitiveHashEx() {
    int h1 = Hashx.compute(1, 0);
    int h2 = Hashx.compute(0, 31);

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2);
    assertThat(h1).isNotEqualTo(h2);
  }

  @Test
  public void primitiveHashEx3() {
    int h1 = Hashx.compute(1, 0, 0);
    int h2 = Hashx.compute(0, 31, 0, 0);

    // NOTE: integer 값 배열 중에 같은 경우가 있으므로, 이런 경우에는 문자열로 바꾸어서 수행해야 합니다.
    log.trace("h1={}, h2={}", h1, h2);
    assertThat(h1).isNotEqualTo(h2);
  }

  @Test
  public void primitiveHashEx4() {
    Map<Integer, Pair<Integer, Integer>> hashMap = Maps.mutable.of();

    for (int i = 0; i < 100; i++) {
      for (int j = 0; j < 100; j++) {
        int hash = Hashx.compute(i, j);
        if (hashMap.containsKey(hash)) {
          log.debug("중복!!!!! i={}, j={}, pair={}", i, j, hashMap.get(hash));
          fail("중복");
        }
        hashMap.put(hash, Tuples.pair(i, j));
      }
    }
  }
}
