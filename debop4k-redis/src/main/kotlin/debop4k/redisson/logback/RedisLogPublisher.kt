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

package debop4k.redisson.logback

import ch.qos.logback.classic.spi.LoggingEvent
import debop4k.core.logback.LogDocument
import debop4k.redisson.DEFAULT_LOGBACK_CHANNEL

/**
 * 로그 정보를 Redis PUB/SUB Channel 로 publish 합니다.
 * @author sunghyouk.bae@gmail.com
 */
class RedisLogPublisher : RedisLogAppender() {

  var logbackChannel: String? = DEFAULT_LOGBACK_CHANNEL

  override fun append(eventObject: LoggingEvent?) {
    eventObject?.let {
      val doc = createLogDocument(it)
      val topic = redisson?.getTopic<LogDocument>(logbackChannel ?: DEFAULT_LOGBACK_CHANNEL)
      topic?.publishAsync(doc)
    }
  }
}