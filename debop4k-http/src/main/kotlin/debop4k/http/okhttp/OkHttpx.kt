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
@file:JvmName("OkHttpx")

package debop4k.http.okhttp

import debop4k.core.loggerOf
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import okhttp3.*
import org.slf4j.Logger
import java.io.IOException
import java.io.InputStream

private val log: Logger = loggerOf("OkHttpx")

val okHttpClient by lazy { OkHttpClient() }

fun okHttpRequestOf(url: String): Request {
  return Request.Builder().url(url).build()
}

/**
 * HTTP 통신을 수행합니다. 실패 시에는 [retryCount] 만큼 재시도 합니다.
 */
@JvmOverloads
fun OkHttpClient.execute(request: Request, retryCount: Int = 3): Response = TODO()

/**
 * 비동기 방식으로 HTTP 통신을 수행합니다.
 * 통신을 취소하려면 다음과 같이 하면 됩니다.
 * <pre><code>
 *   Kovenant.cancel(promise, "Cancel message")
 * </code></pre>
 */
fun OkHttpClient.execAsync(request: Request,
                           callback: (Response) -> Unit): Promise<Response, Exception> {
  log.trace("비동기 Http 통신을 시작합니다...")

  val deferred = deferred<Response, Exception>() { e ->
    log.debug("Http 통신이 취소되었습니다.", e)
  }

  this.newCall(request).enqueue(object : Callback {
    override fun onFailure(call: Call, e: IOException) {
      log.trace("Http communication error.", e)
      deferred.reject(e)
    }

    override fun onResponse(call: Call, response: Response) {
      callback(response)
      if (response.isSuccessful)
        deferred.resolve(response)
      else
        deferred.reject(IOException("response status=${response.code()}"))
    }
  })

  return deferred.promise
}

/**
 * Http [Response] 의 body 를 [InputStream] 으로 가져온다
 */
fun Response.bodyStream(): InputStream = this.body().byteStream()

/**
 * Http [Response] 의 body 를 [byteArray] 으로 가져온다
 */
fun Response.bodyBytes(): ByteArray = this.body().bytes()

/**
 * Http [Response] 의 body 를 문자열로 가져온다
 */
fun Response.bodyString(): String = this.body().string()
