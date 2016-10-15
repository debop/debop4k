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

package debop4k.redisson.spring.boot.autoconfigure;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedissonSpringBootApplication.class)
public class RedissonAutoConfigurationTest {

  @Autowired RedissonClient redissonClient;

  @Test
  public void testAutoConfiguration() throws Exception {
    Assertions.assertThat(redissonClient).isNotNull();

    RBucket<String> bucket = redissonClient.getBucket("spring-boot");
    bucket.set("ok");
    Assertions.assertThat(bucket.get()).isEqualTo("ok");
    bucket.delete();
  }
}
