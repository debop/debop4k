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

package debop4k.http.gcm

import debop4k.core.asyncs.result
import debop4k.core.json.JacksonSerializer
import debop4k.core.loggerOf
import debop4k.core.retry.AsyncRetryExecutor
import debop4k.core.utils.Systems
import debop4k.http.executeAsync
import org.apache.http.*
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.message.BasicHeader
import java.net.URI
import java.util.concurrent.*

/**
 * 구글 푸시 메시지를 전송 요청을 수행하는 클래스입니다
 * @author sunghyouk.bae@gmail.com
 */
class GcmSender {

  private val log = loggerOf(javaClass)

  companion object {
    @JvmField val GCM_SERVER_URL = "https://android.googleapis.com/gcm/send"
    @JvmField val GCM_SERVER_URI = URI(GCM_SERVER_URL)
    @JvmStatic val jsonSerializer = JacksonSerializer()
    @JvmStatic fun of(): GcmSender = GcmSender()
  }

  /**
   * 구글 푸시 메시지를 사용자의 핸드폰에 전송하도록 GCM 서버에 요청힙니다.
   *
   * @param serverApiKey 서비스의 등록 정보
   * @param msg          푸시 메시지 정보
   * @param retry        최대 시도 횟수
   * @return HTTP Status Code
   */
  @JvmOverloads
  fun send(serverApiKey: String, msg: GcmMessage, retry: Int = 3): Int {
    require(!msg.registrationIds.isEmpty()) {
      "전송할 대상 핸드폰의 Device Id가 없습니다."
    }
    val post = buildHttpPost(serverApiKey, msg)

    val executor = AsyncRetryExecutor(Executors.newScheduledThreadPool(Systems.ProcessCount))
    val future = executor.withMaxRetry(retry)
        .getWithRetry { ctx ->

          log.trace("GCM 푸시 메시지 발송을 시작합니다... retryCount={}", ctx.retryCount)
          val future = executeAsync(post)
          val response = future.result()

          val statusCode = response?.statusLine?.statusCode ?: HttpStatus.SC_SERVICE_UNAVAILABLE

          if (statusCode != HttpStatus.SC_OK)
            throw HttpException("StatusCode is not OK.")

          statusCode
        }

    return future.result()
  }

  private fun buildHttpPost(serverApiKey: String, msg: GcmMessage): HttpPost {
    val post = HttpPost(GCM_SERVER_URI)
    buildHttpHeaders(serverApiKey).forEach {
      post.addHeader(it)
    }

    val jsonText = toJsonText(msg)
    assert(!jsonText.isNullOrBlank())

    post.entity = StringEntity(jsonText, Charsets.UTF_8)
    return post
  }

  private fun buildHttpHeaders(serverApiKey: String): List<Header> {
    return listOf(BasicHeader("Authorization", "key=$serverApiKey"),
                  BasicHeader("Content-Type", "application/json"))
  }

  private fun toJsonText(msg: GcmMessage): String {
    return jsonSerializer.toString(msg)
  }
}