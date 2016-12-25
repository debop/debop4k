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
 */
@file:JvmName("OkHttpx")

package debop4k.http.okhttp

import debop4k.core.asyncs.result
import debop4k.core.loggerOf
import debop4k.core.retry.AsyncRetryExecutor
import debop4k.core.retry.backoff.FirstRetryNoDelayBackoff
import debop4k.core.retry.backoff.FixedIntervalBackoff
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import okhttp3.*
import org.apache.http.HttpException
import java.io.IOException
import java.io.InputStream

private val log = loggerOf("OkHttpx")

/**
 * 기본 [OkHttpClient] 인스턴스
 * [OkHttpClient.Builder]를 이용하는 것이 가장 좋다
 */
@JvmField val DefaultOkHttpClient: OkHttpClient = OkHttpClient.Builder().build()

/**
 * url에 요청을 하는 [Request] 를 생성한다
 */
fun okHttpRequestOf(url: String): Request {
  return Request.Builder().url(url).build()
}

/**
 * HTTP 통신을 수행합니다. 실패 시에는 [retryCount] 만큼 재시도 합니다.
 */
@JvmOverloads
fun OkHttpClient.execute(request: Request, retryCount: Int = 3): Response {
  val retryExecutor = AsyncRetryExecutor()
      .retryOn(HttpException::class.java)
      .withMaxRetry(retryCount)
      .withBackoff(FirstRetryNoDelayBackoff(FixedIntervalBackoff(100)))

  val call = newCall(request)
  val future = retryExecutor.getWithRetry {
    call.execute()
  }

  return future.result()
}

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
 * Http [Response] 의 body 를 [ByteArray] 으로 가져온다
 */
fun Response.bodyBytes(): ByteArray = this.body().bytes()

/**
 * Http [Response] 의 body 를 문자열로 가져온다
 */
fun Response.bodyString(): String = this.body().string()
