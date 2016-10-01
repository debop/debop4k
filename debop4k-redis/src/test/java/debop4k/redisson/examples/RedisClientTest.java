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

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.redisson.api.RFuture;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisConnection;
import org.redisson.client.RedisPubSubConnection;
import org.redisson.client.RedisPubSubListener;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.client.protocol.pubsub.PubSubType;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class RedisClientTest {

  @Test
  public void testConnectionAsync() throws InterruptedException, ExecutionException {
    RedisClient c = new RedisClient("localhost", 6379);
    RFuture<RedisConnection> future = c.connectAsync();
    final CountDownLatch latch = new CountDownLatch(1);

    future.addListener(new FutureListener<RedisConnection>() {
      @Override
      public void operationComplete(Future<RedisConnection> future) throws Exception {
        RedisConnection conn = future.get();
        assertThat(conn.isActive()).isTrue();
        assertThat(conn.isOpen()).isTrue();

        latch.countDown();
      }
    });

    // Java 8
//    future.addListener((Future<RedisConnection> fc) -> {
//      RedisConnection conn = fc.get();
//      assertThat(conn.isActive()).isTrue();
//      assertThat(conn.isOpen()).isTrue();
//
//      latch.countDown();
//    });

    latch.await();
    ChannelFuture cf = future.get().closeAsync();
    cf.get();
  }

  @Test
  @SneakyThrows({InterruptedException.class})
  public void testSubscribe() {
    RedisClient redis = new RedisClient("localhost", 6379);
    RedisPubSubConnection pubSubConnection = redis.connectPubSub();
    final CountDownLatch latch = new CountDownLatch(2);

    pubSubConnection.addListener(new RedisPubSubListener<Object>() {
      @Override
      public void onMessage(String channel, Object msg) {
        log.debug("onMessage. channel={}, msg={}", channel, msg);
      }

      @Override
      public boolean onStatus(PubSubType type, String channel) {
        log.debug("Status. type={}, channel={}", type, channel);
        assertThat(type).isEqualTo(PubSubType.SUBSCRIBE);
        assertThat(Arrays.asList("test1", "test2")).contains(channel);
        latch.countDown();
        return true;
      }

      @Override
      public void onPatternMessage(String pattern, String channel, Object message) {
        log.debug("onPatternMessage. pattern={}, channel={}, message={}", pattern, channel, message);
      }
    });

    log.debug("subscribe test1, test2");
    pubSubConnection.subscribe(StringCodec.INSTANCE, "test1", "test2");

    latch.await();
  }

  @Test
  @SneakyThrows
  public void testAsyncCommands() {
    int COUNT = 100000;

    RedisClient redis = new RedisClient("localhost", 6379);
    final RedisConnection conn = redis.connect();

    conn.sync(StringCodec.INSTANCE, RedisCommands.SET, "test", 0);

    for (int i = 0; i < COUNT; i++) {
      conn.async(StringCodec.INSTANCE, RedisCommands.INCR, "test");
    }

    Long value = conn.sync(LongCodec.INSTANCE, RedisCommands.GET, "test");
    assertThat(value).isEqualTo(COUNT);

    conn.sync(RedisCommands.FLUSHDB);
  }
}
