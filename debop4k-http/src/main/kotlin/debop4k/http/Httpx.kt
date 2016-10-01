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

@file:JvmName("Httpx")

package debop4k.http

import debop4k.core.NotSupportedException
import debop4k.core.json.JacksonSerializer
import debop4k.core.loggerOf
import org.apache.http.*
import org.apache.http.client.HttpResponseException
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.*
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import java.net.URI
import java.nio.charset.Charset

private val log = loggerOf("HttpExtensions")

private val jsonSerializer by lazy { JacksonSerializer() }

fun httpUriRequestOf(method: String, uri: String): HttpUriRequest
    = httpUriRequestOf(method, URI(uri))

fun httpUriRequestOf(method: String, uri: URI): HttpUriRequest = when (method.toUpperCase()) {

  HttpHead.METHOD_NAME -> HttpHead(uri)
  HttpGet.METHOD_NAME -> HttpGet(uri)
  HttpPost.METHOD_NAME -> HttpPost(uri)
  HttpPut.METHOD_NAME -> HttpPut(uri)
  HttpPatch.METHOD_NAME -> HttpPut(uri)
  HttpOptions.METHOD_NAME -> HttpOptions(uri)
  HttpDelete.METHOD_NAME -> HttpDelete(uri)
  HttpTrace.METHOD_NAME -> HttpTrace(uri)

  else -> throw NotSupportedException("지원하지 않는 메소드입니다. method=$method")

}

fun HttpUriRequest.addHeaders(headers: Iterable<Header>): Unit {
  headers.forEach { this.addHeader(it) }
}

fun HttpUriRequest.addHeaders(vararg headers: Header): Unit {
  headers.forEach { this.addHeader(it) }
}

fun HttpEntityEnclosingRequest.setEntity(nvps: Iterable<NameValuePair>, cs: Charset = Charsets.UTF_8): Unit {
  this.entity = UrlEncodedFormEntity(nvps, cs)
  this.addHeader("Content-Type", "application/x-www-form-urlencoded")
}

fun HttpEntityEnclosingRequest.setEntityByJson(entity: Any, cs: Charset = Charsets.UTF_8): Unit {
  val jsonText = jsonSerializer.toString(entity)
  this.entity = StringEntity(jsonText, cs)
  this.addHeader("Content-Type", "application/json")
  this.addHeader("Accept", "application/json")
}

@JvmOverloads
fun HttpResponse.toString(cs: Charset = Charsets.UTF_8): String {
  this.assertStatus()
  return EntityUtils.toString(this.entity, cs)
}

/**
 * [HttpResponse] 의 상태 코드가 성공인지 파악한다. 아니라면 [HttpResponseException] 예외를 발생시킨다
 */
@Throws(HttpResponseException::class)
fun HttpResponse.assertStatus(): Unit {
  val status = this.statusLine.statusCode
  if (status != HttpStatus.SC_OK) {
    throw HttpResponseException(status, "HttpStatus 상태가 200 이 아닙니다.")
  }
}