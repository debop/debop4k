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

package debop4k.mongodb.logback

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.classic.spi.ThrowableProxyUtil
import ch.qos.logback.core.CoreConstants
import ch.qos.logback.core.UnsynchronizedAppenderBase
import com.mongodb.MongoClient
import com.mongodb.ServerAddress
import debop4k.core.asyncs.future
import org.joda.time.DateTime
import org.springframework.data.authentication.UserCredentials
import org.springframework.data.mongodb.core.MongoTemplate

/**
 * logback 에서 생성되는 로그를 MongoDB 에 저장하는 Log Appender 입니다
 * @author sunghyouk.bae@gmail.com
 */
class MongodbLogAppender : UnsynchronizedAppenderBase<LoggingEvent>() {

  companion object {
    @JvmField val DEFAULT_DB_NAME = "logDB"
    @JvmField val DEFAULT_COLLECTION_NAME = "logs"
  }

  private var template: MongoTemplate? = null
  private var client: MongoClient? = null

  private val serverName: String? = null
  private val applicationName: String? = null
  private val host = ServerAddress.defaultHost()
  private val port = ServerAddress.defaultPort()
  private val dbName = DEFAULT_DB_NAME
  private var collectionName = DEFAULT_COLLECTION_NAME
  private val username: String? = null
  private val password: String? = null

  override fun start() {
    try {
      connect()
      super.start()
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun append(eventObject: LoggingEvent?) {
    if (eventObject == null)
      return

    future {
      val doc = newLogDocument(eventObject)
      template?.save(doc, collectionName)
    }
  }

  private fun connect() {
    client = MongoClient(host, port)

    val credentials = if (username != null && password != null)
      UserCredentials(username, password)
    else
      UserCredentials.NO_CREDENTIALS

    template = MongoTemplate(client!!, dbName, credentials)

    if (collectionName.isNullOrBlank())
      collectionName = DEFAULT_COLLECTION_NAME

    if (!template!!.collectionExists(collectionName))
      template!!.createCollection(collectionName)
  }

  private fun newLogDocument(event: ILoggingEvent): MongodbLogDocument {

    val doc = MongodbLogDocument().apply {
      serverName = this@MongodbLogAppender.serverName ?: ""
      applicationName = this@MongodbLogAppender.applicationName ?: ""
      logger = event.loggerName
      levelInt = event.level.levelInt
      levelStr = event.level.levelStr
      threadName = event.threadName
      timestamp = DateTime(event.timeStamp)
      message = event.formattedMessage
    }

    if (event.marker != null) {
      doc.marker = event.marker.name
    }

    event.throwableProxy?.let { tp ->
      val str = ThrowableProxyUtil.asString(tp)
      val stacktrace = str.replace("\t", "")
          .split(CoreConstants.LINE_SEPARATOR)
          .toMutableList()

      if (stacktrace.size > 0)
        doc.exception = stacktrace[0]
      if (stacktrace.size > 1) {
        stacktrace.removeAt(1)
        doc.stackTrace = stacktrace
      }
    }

    return doc
  }
}