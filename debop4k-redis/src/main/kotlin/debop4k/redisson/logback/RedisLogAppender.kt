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
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.CoreConstants
import ch.qos.logback.core.UnsynchronizedAppenderBase
import debop4k.core.json.DefaultJacksonSerializer
import debop4k.core.json.JsonSerializer
import debop4k.core.logback.LogDocument
import debop4k.core.uninitialized
import debop4k.core.utils.EMPTY_STRING
import debop4k.redisson.*
import org.joda.time.DateTime
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config

/**
 * logback 로그 정보를 Redis list 자료구조를 이용하여 저장하는 appender 입니다.
 * @author sunghyouk.bae@gmail.com
 */
open class RedisLogAppender : UnsynchronizedAppenderBase<LoggingEvent>() {

  companion object {
    @JvmField val DEFAULT_KEY = RedisLogAppender::class.java.`package`.name + ".logs"
  }

  val serializer: JsonSerializer = DefaultJacksonSerializer

  @Volatile protected var redisson: RedissonClient? = uninitialized()

  private var host: String = DEFAULT_HOST
  private var port: Int = DEFAULT_PORT
  private var timeout: Int = DEFAULT_TIMEOUT
  private var password: String? = null
  private var database: Int = DEFAULT_DATABASE

  private var key: String? = DEFAULT_KEY
  private var serverName: String = ""
  private var applicationName: String = ""

  @Synchronized
  override fun start() {
    println("start Redis logging appender")

    if (redisson == null) {
      val cfg = Config()
      cfg.useSingleServer()
          .setAddress(host + ":" + port)
          .setTimeout(timeout)
          .setPassword(password)
          .setDatabase(database)

      redisson = Redisson.create(cfg)
    }
  }

  @Synchronized
  override fun stop() {
    super.stop()
    redisson = null
  }

  override fun append(eventObject: LoggingEvent?) {
    eventObject?.let {
      val queue = redisson?.getDeque<LogDocument>(key)
      val doc = createLogDocument(it)
      queue?.addFirstAsync(doc)
    }
  }

  open protected fun createLogDocument(event: LoggingEvent): LogDocument {
    val doc = LogDocument().apply {
      serverName = this@RedisLogAppender.serverName
      applicationName = this@RedisLogAppender.applicationName
      logger = event.loggerName
      levelInt = event.level.levelInt
      levelStr = event.level.levelStr
      threadName = event.threadName
      timestamp = DateTime(event.timeStamp)
      message = event.message
    }

    doc.marker = event.marker?.name ?: EMPTY_STRING

    event.throwableProxy?.let { tp ->
      val tpStr = ThrowableProxyUtil.asString(tp)
      val stacktrace: List<String> = tpStr.replace("\t", "").split(CoreConstants.LINE_SEPARATOR)
      if (stacktrace.size > 0) {
        doc.exception = stacktrace[0]
      }
      if (stacktrace.size > 1) {
        doc.stackTrace = stacktrace.toMutableList().apply { removeAt(0) }
        //  FastList.newList(stacktrace).subList(1, stacktrace.size - 1)
      }
    }

    return doc
  }
}