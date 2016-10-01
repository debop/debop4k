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
@file:JvmName("AsyncHttpx")

package debop4k.http

import debop4k.core.asyncs.result
import debop4k.core.collections.parMap
import nl.komponents.kovenant.Promise
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

private val log = LoggerFactory.getLogger("AsyncHttpEx")

internal val asyncClient: AsyncHttpClient by lazy { AsyncHttpClient() }
internal val asyncSslClient: AsyncHttpSslClient by lazy { AsyncHttpSslClient() }

/**
 * 비동기 방식으로 HTTP 통신을 수행합니다.
 * [HttpRequestBuilder] 를 이용하여 request 를 빌드하면 됩니다
 * @see [HttpRequestBuilder]
 */
fun executeAsync(request: HttpUriRequest): Promise<HttpResponse?, Exception>
    = asyncClient.execute(request)

/**
 * HTTP 요청을 병렬로 비동기 방식 HTTP 통신을 수행합니다.
 * [HttpRequestBuilder] 를 이용하여 request 를 빌드하면 됩니다
 * @see [HttpRequestBuilder]
 */
fun executeAsyncAsParallel(requests: Iterable<HttpUriRequest>): List<Promise<HttpResponse?, Exception>>
    = requests.parMap { asyncClient.execute(it) }.toList()

@JvmOverloads
fun getContent(promise: Promise<HttpResponse?, Exception>, cs: Charset = Charsets.UTF_8): String {
  val response = promise.result()
  return EntityUtils.toString(response?.entity ?: null, cs)
}

@JvmOverloads
fun getContents(promises: List<Promise<HttpResponse?, Exception>>, cs: Charset = Charsets.UTF_8): List<String?> {
  return promises.parMap { promise -> getContent(promise, cs) }.toList()
}