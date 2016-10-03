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

import debop4k.core.collections.parMap
import debop4k.core.loggerOf
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.task
import org.apache.http.HttpResponse
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.concurrent.FutureCallback
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import java.util.concurrent.*

/**
 * 비동기 Http Client
 *
 * @author debop sunghyouk.bae@gmail.com
 */
open class AsyncHttpClient {

  private val log = loggerOf(javaClass)

  val requestConfig: RequestConfig by lazy {
    RequestConfig.custom()
        .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)
        .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)
        .build()
  }

  @JvmOverloads
  fun execute(request: HttpUriRequest,
              callback: FutureCallback<HttpResponse>? = null): Promise<HttpResponse?, Exception> {

    val client = newAsyncHttpClient()
    client.start()

    val promise = task {
      val javafuture = client.execute(request, callback)
      javafuture.get(DEFAULT_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
    }

    promise.always {
      client.close()
    }
    return promise
  }

  fun executeAsParallel(requests: Iterable<HttpUriRequest>): List<Promise<HttpResponse?, Exception>> {
    return requests.parMap { request -> execute(request) }.toList()
  }

  open protected fun newAsyncHttpClient(): CloseableHttpAsyncClient {
    return HttpAsyncClients.custom()
        .setDefaultRequestConfig(requestConfig)
        .build()
  }

  companion object {
    const val DEFAULT_CONNECT_TIMEOUT = 90
    const val DEFAULT_SOCKET_TIMEOUT = 30000
  }
}