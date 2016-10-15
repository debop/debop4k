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

package debop4k.redisson.spring.factory;

import debop4k.redisson.spring.RedissonReactiveTemplate;
import debop4k.redisson.spring.RedissonTemplate;
import debop4k.redisson.spring.boot.autoconfigure.RedissonSpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedissonSpringBootApplication.class)
public class RedissonTemplateFactoryBeanTest {

  @Autowired RedissonClient redissonClient;
  @Autowired RedissonTemplate redissonTemplate;
  @Autowired RedissonReactiveTemplate redissonReactiveTemplate;

  @Test
  public void testFactoryBean() {
    assertThat(redissonClient).isNotNull();
    assertThat(redissonTemplate).isNotNull();
    assertThat(redissonReactiveTemplate).isNotNull();
  }

  @Test
  public void testClient() {
    RBucket<String> bucket = redissonClient.getBucket("rbucket");
    bucket.set("value1");

    String value = bucket.get();
    assertThat(value).isEqualTo("value1");

    bucket.delete();
  }

  @Test
  public void testTemplate() {
    long keyCount = redissonTemplate.getKeys().count();
    assertThat(keyCount).isGreaterThanOrEqualTo(0);
  }

//  @Test
//  public void testReactiveTemplate() {
//    redissonReactiveTemplate.getKeys().randomKey().subscribe(new Subscriber<String>() {
//      @Override
//      public void onSubscribe(Subscription s) {}
//
//      @Override
//      public void onNext(String s) {
//        log.debug("random Key={}", s);
//        redissonTemplate.getKeys().delete(s);
//      }
//
//      @Override
//      public void onError(Throwable t) {}
//
//      @Override
//      public void onComplete() {}
//    });
//  }
}
