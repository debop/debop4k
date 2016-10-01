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

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RSetMultimap;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedissonSetMultimapTest extends AbstractRedissonTest {

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SimpleKey extends AbstractValueObject {
    private static final long serialVersionUID = -1603777276269647162L;
    private String key;

    @Override
    public int hashCode() {
      return Hashx.compute(key);
    }

    @Override
    public ToStringHelper buildStringHelper() {
      return super.buildStringHelper().add("key", key);
    }
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class SimpleValue extends AbstractValueObject {
    private static final long serialVersionUID = -6797071867982606907L;
    private String value;

    @Override
    public int hashCode() {
      return Hashx.compute(value);
    }

    @Override
    public ToStringHelper buildStringHelper() {
      return super.buildStringHelper().add("value", value);
    }
  }

  @Test
  public void testSize() {
    RSetMultimap<SimpleKey, SimpleValue> map = redisson.getSetMultimap("test1");
    map.put(new SimpleKey("0"), new SimpleValue("1"));
    map.put(new SimpleKey("0"), new SimpleValue("2"));

    assertThat(map.size()).isEqualTo(2);

    Set<SimpleValue> s = map.get(new SimpleKey("0"));
    assertThat(s).hasSize(2);

    map.fastRemove(new SimpleKey("0"));
    s = map.get(new SimpleKey("0"));
    assertThat(s).isEmpty();
    assertThat(map.size()).isEqualTo(0);
  }

  @Test
  public void testPut() {
    RSetMultimap<SimpleKey, SimpleValue> map = redisson.getSetMultimap("test1");
    map.put(new SimpleKey("0"), new SimpleValue("1"));
    map.put(new SimpleKey("0"), new SimpleValue("2"));
    map.put(new SimpleKey("0"), new SimpleValue("3"));
    map.put(new SimpleKey("0"), new SimpleValue("3"));
    map.put(new SimpleKey("3"), new SimpleValue("4"));

    assertThat(map.size()).isEqualTo(4);

    Set<SimpleValue> s1 = map.get(new SimpleKey("0"));
    assertThat(s1).containsOnly(new SimpleValue("1"),
                                new SimpleValue("2"),
                                new SimpleValue("3"));

    Set<SimpleValue> allValues = map.getAll(new SimpleKey("0"));
    assertThat(allValues).containsOnly(new SimpleValue("1"),
                                       new SimpleValue("2"),
                                       new SimpleValue("3"));

    Set<SimpleValue> s2 = map.get(new SimpleKey("3"));
    assertThat(s2).containsOnly(new SimpleValue("4"));
  }

  @Test
  public void testRemoveAll() {
    RSetMultimap<SimpleKey, SimpleValue> map = redisson.getSetMultimap("test1");
    map.put(new SimpleKey("0"), new SimpleValue("1"));
    map.put(new SimpleKey("0"), new SimpleValue("2"));
    map.put(new SimpleKey("0"), new SimpleValue("3"));

    assertThat(map.size()).isEqualTo(3);
    Set<SimpleValue> values = map.removeAll(new SimpleKey("0"));
    assertThat(values).containsOnly(new SimpleValue("1"),
                                    new SimpleValue("2"),
                                    new SimpleValue("3"));
    assertThat(map.size()).isEqualTo(0);

    Set<SimpleValue> values2 = map.removeAll(new SimpleKey("0"));
    assertThat(values2).isEmpty();
  }

  @Test
  public void testFastRemove() {
    RSetMultimap<SimpleKey, SimpleValue> map = redisson.getSetMultimap("test1");
    assertThat(map.put(new SimpleKey("0"), new SimpleValue("1"))).isTrue();
    assertThat(map.put(new SimpleKey("0"), new SimpleValue("2"))).isTrue();
    assertThat(map.put(new SimpleKey("0"), new SimpleValue("2"))).isFalse();
    assertThat(map.put(new SimpleKey("0"), new SimpleValue("3"))).isTrue();

    long removed = map.fastRemove(new SimpleKey("0"), new SimpleKey("1"));
    assertThat(removed).isEqualTo(1);
    assertThat(map.size()).isEqualTo(0);
  }
}
