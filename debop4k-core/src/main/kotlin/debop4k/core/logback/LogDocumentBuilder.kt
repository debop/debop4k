/*
 *  Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.logback

import org.joda.time.DateTime

/**
 * Kotlin 에서는 이런 Builder 패턴을 apply { } 메소드로 사용하는 것이 가장 좋다
 *
 * @author sunghyouk.bae@gmail.com
 */
class LogDocumentBuilder {

  private var _serverName: String = "localhost"
  private var _applicationName: String = ""
  private var _logger: String = ""
  private var _levelInt: Int = 0
  private var _levelStr: String = ""
  private var _threadName: String = ""
  private var _message: String = ""
  private var _timestamp: DateTime = DateTime.now()
  private var _marker: String? = null
  private var _exception: String? = null
  private var _stackTrace: List<String> = emptyList()


  /**
   * Server name 설정

   * @param serverName 설정할 server name
   * *
   * @return LogDocumentBuilder 인스턴스
   */
  fun serverName(serverName: String): LogDocumentBuilder {
    this._serverName = serverName
    return this
  }

  /**
   * Application name 설정

   * @param applicationName 설정할 application name
   * *
   * @return LogDocumentBuilder 인스턴스
   */
  fun applicationName(applicationName: String): LogDocumentBuilder {
    this._applicationName = applicationName
    return this
  }

  /**
   * Logger 명 설정

   * @param logger 설정할 logger 명
   * *
   * @return LogDocumentBuilder 인스턴스
   */
  fun logger(logger: String): LogDocumentBuilder {
    this._logger = logger
    return this
  }

  /**
   * Level 값 설정

   * @param levelInt 설정할 레벨 값 (참고 : [ch.qos.logback.classic.Level] )
   * *
   * @return LogDocumentBuilder 인스턴스
   */
  fun levelInt(levelInt: Int): LogDocumentBuilder {
    this._levelInt = levelInt
    return this
  }

  /**
   * Level 설정

   * @param levelStr 설정할 레벨 값 (참고 : [ch.qos.logback.classic.Level] )
   * *
   * @return LogDocumentBuilder 인스턴스
   */
  fun levelStr(levelStr: String): LogDocumentBuilder {
    this._levelStr = levelStr
    return this
  }

  fun threadName(threadName: String): LogDocumentBuilder {
    this._threadName = threadName
    return this
  }

  fun message(message: String): LogDocumentBuilder {
    this._message = message
    return this
  }

  fun timestamp(timestamp: DateTime): LogDocumentBuilder {
    this._timestamp = timestamp
    return this
  }

  fun marker(marker: String): LogDocumentBuilder {
    this._marker = marker
    return this
  }

  fun exception(exception: String): LogDocumentBuilder {
    this._exception = exception
    return this
  }

  fun stackTrace(stackTrace: List<String>): LogDocumentBuilder {
    this._stackTrace = stackTrace
    return this
  }

  fun build(): LogDocument {
    return LogDocument().apply {
      serverName = _serverName
      applicationName = _applicationName
      logger = _logger
      levelInt = _levelInt
      levelStr = _levelStr
      threadName = _threadName
      message = _message
      timestamp = _timestamp
      marker = _marker
      exception = _exception
      stackTrace = _stackTrace
    }
  }

}