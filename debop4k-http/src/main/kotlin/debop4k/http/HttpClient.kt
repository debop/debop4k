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

package debop4k.http

import debop4k.core.json.JacksonSerializer
import org.apache.http.Header
import org.apache.http.HttpHost
import org.apache.http.NameValuePair
import org.apache.http.client.methods.*
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.DefaultProxyRoutePlanner
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.slf4j.LoggerFactory
import java.net.URI
import java.nio.charset.Charset

/**
 * HttpClient
 * @author debop sunghyouk.bae@gmail.com
 */
open class HttpClient : AutoCloseable {

  private val log = LoggerFactory.getLogger(javaClass)
  private val serializer by lazy { JacksonSerializer() }
  private val connectionManager by lazy { PoolingHttpClientConnectionManager() }

  var proxy: HttpHost? = null

  /**
   * HTTP GET 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param cs      응답 정보의 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun get(uri: URI, cs: Charset = Charsets.UTF_8, vararg headers: Header = emptyArray()): String {
    log.trace("Http GET 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpGet(uri)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP DELETE 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun delete(uri: URI, cs: Charset = Charsets.UTF_8, vararg headers: Header = emptyArray()): String {
    log.trace("Http DELETE 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpDelete(uri)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP POST 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param nvps    전송할 Name-Value 정보
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun post(uri: URI,
           nvps: Iterable<NameValuePair>,
           cs: Charset = Charsets.UTF_8,
           vararg headers: Header = emptyArray()): String {
    log.trace("Http POST 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPost(uri)
      httprequest.setEntity(nvps, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP POST 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param entity  전송할 객체
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun postJson(uri: URI,
               entity: Any,
               cs: Charset = Charsets.UTF_8,
               vararg headers: Header = emptyArray()): String {
    log.trace("Http POST 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPost(uri)
      httprequest.setEntityByJson(entity, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP PUT 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param nvps    전송할 Name-Value 정보
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun put(uri: URI,
          nvps: Iterable<NameValuePair>,
          cs: Charset = Charsets.UTF_8,
          vararg headers: Header = emptyArray()): String {
    log.trace("Http PUT 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPut(uri)
      httprequest.setEntity(nvps, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP PUT 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param entity  전송할 객체
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun putJson(uri: URI,
              entity: Any,
              cs: Charset = Charsets.UTF_8,
              vararg headers: Header = emptyArray()): String {
    log.trace("Http PUT 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPut(uri)
      httprequest.setEntityByJson(entity, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP PATCH 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param nvps    전송할 Name-Value 정보
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun patch(uri: URI, nvps: Iterable<NameValuePair>, cs: Charset = Charsets.UTF_8, vararg headers: Header): String {
    log.trace("Http PATCH 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPatch(uri)
      httprequest.setEntity(nvps, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  /**
   * HTTP PATCH 메소드를 실행합니다.
   *
   * @param uri     요청 URI
   * @param entity  전송할 객체
   * @param cs      응답정보 Charset
   * @param headers 요청 Header 에 추가할 Header
   * @return 응답 문자열
   */
  @JvmOverloads
  fun patchJson(uri: URI, entity: Any, cs: Charset = Charsets.UTF_8, vararg headers: Header): String {
    log.trace("Http PATCH 을 요청합니다. uri={}, cs={}, headers={}", uri, cs, headers)

    newHttpClient().use { client ->
      val httprequest = HttpPatch(uri)
      httprequest.setEntityByJson(entity, cs)
      httprequest.addHeaders(*headers)

      client.execute(httprequest).use { response ->
        return response.toString(cs)
      }
    }
  }

  open protected fun newHttpClient(): CloseableHttpClient {
    val builder = HttpClients.custom().setConnectionManager(connectionManager)
    proxy?.let {
      builder.setRoutePlanner(DefaultProxyRoutePlanner(it))
    }
    return builder.build()
  }

  /**
   * HttpClient 사용을 중지합니다.
   */
  override fun close() {
    log.trace("Start to shutdown ConnectionManager...")
    try {
      connectionManager.shutdown()
      log.debug("Success to shutdown ConnectionManager")
    } catch(ignored: Exception) {
      log.warn("Fail to shutdown connectionManager", ignored)
    }
  }
}