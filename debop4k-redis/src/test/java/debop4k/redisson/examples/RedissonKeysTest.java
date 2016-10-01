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
import org.eclipse.collections.impl.factory.Sets;
import org.junit.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedissonKeysTest extends AbstractRedissonTest {

  private static final Random random = new Random(new Date().getTime());

  @Test
  public void testKeysIterablePattern() {
    redisson.getBucket("test1").set("someValue");
    redisson.getBucket("test2").set("someValue");

    redisson.getBucket("test12").set("someValue");

    Iterator<String> iterator = redisson.getKeys().getKeysByPattern("test?").iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      assertThat(key).isIn("test1", "test2");
    }
  }

  @Test
  public void testKeysIterable() {
    Set<String> keys = Sets.mutable.of();
    for (int i = 0; i < 115; i++) {
      String key = "key-" + random.nextDouble();
      keys.add(key);
      RBucket<String> bucket = redisson.getBucket(key);
      bucket.set("someValue");
    }

    Iterator<String> iterator = redisson.getKeys().getKeys().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      keys.remove(key);
      iterator.remove();
    }

    assertThat(keys).hasSize(0);
    assertThat(redisson.getKeys().getKeys().iterator().hasNext()).isFalse();
  }

  @Test
  public void testRandomKey() {
    RBucket<String> bucket = redisson.getBucket("test1");
    bucket.set("someValue1");

    RBucket<String> bucket2 = redisson.getBucket("test2");
    bucket2.set("someValue2");

    assertThat(redisson.getKeys().randomKey()).isIn("test1", "test2");
    redisson.getKeys().delete("test1");
    assertThat(redisson.getKeys().randomKey()).isEqualTo("test2");
    redisson.getKeys().flushdb();
    assertThat(redisson.getKeys().randomKey()).isNullOrEmpty();
  }

  @Test
  public void testDeleteByPattern() {
    RBucket<String> bucket = redisson.getBucket("test0");
    bucket.set("someValue3");
    bucket.compareAndSet("someValue3", "someValueUpdated");
    assertThat(bucket.isExists()).isTrue();

    RBucket<String> bucket2 = redisson.getBucket("test9");
    bucket2.set("someValue4");
    assertThat(bucket2.isExists()).isTrue();

    RMap<String, String> map = redisson.getMap("test2");
    map.fastPut("1", "2");
    map.fastPutIfAbsent("2", "43242342342");
    assertThat(map.isExists()).isTrue();

    RMap<String, String> map2 = redisson.getMap("test3");
    map2.fastPut("1", "5");
    assertThat(map2.isExists()).isTrue();

    assertThat(redisson.getKeys().deleteByPattern("test?")).isEqualTo(4);
    assertThat(redisson.getKeys().deleteByPattern("test?")).isEqualTo(0);
  }
}
