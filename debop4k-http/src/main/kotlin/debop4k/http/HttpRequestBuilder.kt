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

import debop4k.core.collections.fastListOf
import org.apache.http.Header
import org.apache.http.HttpEntityEnclosingRequest
import org.apache.http.NameValuePair
import org.apache.http.client.methods.HttpUriRequest
import java.net.URI
import java.nio.charset.Charset

/**
 * HttpRequestBuilder
 * @author debop sunghyouk.bae@gmail.com
 */
class HttpRequestBuilder {

  private var nvps: Iterable<NameValuePair>? = null
  private var method: String = "GET"
  private var uri: String = ""
  private var cs: Charset = Charsets.UTF_8

  private var entity: Any? = null
  private val headers = fastListOf<Header>()

  fun setMethod(method: String): HttpRequestBuilder {
    this.method = method
    return this
  }

  fun setUri(uri: URI): HttpRequestBuilder {
    this.uri = uri.toString()
    return this
  }

  fun setUri(uri: String): HttpRequestBuilder {
    this.uri = uri
    return this
  }

  fun setNameValues(nvps: Iterable<NameValuePair>): HttpRequestBuilder {
    this.nvps = nvps
    return this
  }

  fun setCharset(cs: Charset): HttpRequestBuilder {
    this.cs = cs
    return this
  }

  fun setEntity(entity: Any?): HttpRequestBuilder {
    this.entity = entity
    return this
  }

  fun setHeaders(vararg headers: Header): HttpRequestBuilder {
    this.headers.addAll(headers)
    return this
  }

  fun build(): HttpUriRequest {
    val request = httpUriRequestOf(method, uri)

    // POST, PUT, PATCH 같은 경우
    if (request is HttpEntityEnclosingRequest) {
//      if (nvps != null) {
//        encRequest.setEntity(nvps!!, cs)
//      } else if (entity != null) {
//        encRequest.setEntityByJson(entity!!)
//      }

      nvps?.let { request.setEntity(it, cs) }
      ?: entity?.let { request.setEntityByJson(it) }
    }
    request.addHeaders(headers)
    return request
  }

  companion object {
    @JvmStatic
    fun of(): HttpRequestBuilder = HttpRequestBuilder()
  }
}