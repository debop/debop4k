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

package debop4k.redisson.logback;

import debop4k.redisson.AbstractRedissonTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * 이 테스트를 진행하려면, logback-test.xml 에 RedissonAppender 를 정의해주고, appender 로 로그가 쌓이게 해줘야 합니다.
 */
@Slf4j
public class RedissonAppenderTest extends AbstractRedissonTest {

  @Test
  public void testLoggingMessage() {
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 100; j++) {
        log.trace("appender test [{}]", j);
      }
    }
  }

  @Test
  public void clearLog() {
    redisson.getKeys().delete(RedisLogAppender.DEFAULT_KEY);
  }
}
