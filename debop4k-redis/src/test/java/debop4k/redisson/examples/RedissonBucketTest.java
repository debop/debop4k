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
import org.junit.Test;
import org.redisson.api.RBucket;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedissonBucketTest extends AbstractRedissonTest {

  @Test
  public void testCompareAndSet() {
    RBucket<List<String>> r1 = redisson.getBucket("testCompareAndSet");
    assertThat(r1.compareAndSet(null, asList("81"))).isTrue();
    assertThat(r1.compareAndSet(null, asList("12"))).isFalse();

    assertThat(r1.compareAndSet(asList("81"), asList("0"))).isTrue();
    assertThat(r1.get()).isEqualTo(asList("0"));

    assertThat(r1.compareAndSet(asList("1"), asList("2"))).isFalse();
    assertThat(r1.get()).isEqualTo(asList("0"));

    assertThat(r1.compareAndSet(asList("0"), null)).isTrue();
    assertThat(r1.get()).isNull();
    assertThat(r1.isExists()).isFalse();
  }

  @Test
  public void testGetAndSet() {
    RBucket<List<String>> r1 = redisson.getBucket("testGetAndSet");
    assertThat(r1.getAndSet(asList("01"))).isNull();
    assertThat(r1.getAndSet(asList("1"))).isEqualTo(asList("01"));
    assertThat(r1.get()).isEqualTo(asList("1"));

    assertThat(r1.getAndSet(null)).isEqualTo(asList("1"));
    assertThat(r1.get()).isNull();
    assertThat(r1.isExists()).isFalse();
  }
}
