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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 * AbstractRedissonTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 9. 15.
 */
public abstract class AbstractRedissonTest {

  protected static RedissonClient redisson;

  @BeforeClass
  public static void beforeClass() {
    redisson = createInstance();
  }

  @AfterClass
  public static void afterClass() {
    redisson.shutdown();
  }

  public static Config createConfig() {
    String redisAddress = "127.0.0.1:6379";

    Config config = new Config();
    config.useSingleServer().setAddress(redisAddress);

    return config;
  }

  public static RedissonClient createInstance() {
    Config config = createConfig();
    return Redisson.create(config);
  }

  @Before
  public void before() {
    redisson.getKeys().flushall();
  }
}
