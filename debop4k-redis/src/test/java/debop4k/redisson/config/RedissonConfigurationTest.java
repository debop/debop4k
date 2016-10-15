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

package debop4k.redisson.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisConnection;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.RedisStrictCommand;
import org.redisson.client.protocol.decoder.StringMapDataDecoder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedissonConfiguration.class})
public class RedissonConfigurationTest {

  @Inject RedissonClient redisson;
  @Inject RedissonReactiveClient redissonReactive;
  @Inject RedisClient redis;

  @Test
  public void testInjection() {
    assertThat(redisson).isNotNull();
    assertThat(redissonReactive).isNotNull();
    assertThat(redis).isNotNull();
  }

  @Test
  public void testRedisConnection() {

    RedisConnection conn = redis.connect();
    assertThat(conn.sync(RedisCommands.PING)).isEqualTo("PONG");

    RedisStrictCommand<Map<String, String>> INFO_ALL =
        new RedisStrictCommand<Map<String, String>>("INFO", null, new StringMapDataDecoder());
    Map<String, String> infos = conn.sync(INFO_ALL);
    for (Map.Entry<String, String> entry : infos.entrySet()) {
      log.debug("{}={}", entry.getKey(), entry.getValue());
    }
    conn.closeAsync();
  }

  @Test
  public void testBucket() {
    redisson.getKeys().flushdb();

    RBucket<String> bucket = redisson.getBucket("a");
    bucket.delete();
    assertThat(bucket.trySet("b")).isTrue();
    assertThat(bucket.delete()).isTrue();
    assertThat(redisson.getBucket("a").delete()).isFalse();
  }
}
