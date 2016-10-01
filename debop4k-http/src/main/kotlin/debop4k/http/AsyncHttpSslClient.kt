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

import org.apache.http.HttpException
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient
import org.apache.http.impl.nio.client.HttpAsyncClients
import org.apache.http.nio.conn.ssl.SSLIOSessionStrategy
import org.apache.http.ssl.SSLContexts
import java.security.KeyStore

/**
 * 비동기 HTTPS Client
 *
 * @author debop sunghyouk.bae@gmail.com
 */
class AsyncHttpSslClient : AsyncHttpClient() {

  /**
   * SSL 통신을 위한 [CloseableHttpAsyncClient] 를 생성합니다.
   *
   * @param uri 접속할 URI
   * @return 생성된 { @link CloseableHttpAsyncClient}, 다 사용 후에는 close() 를 호출합니다.
   */
  override fun newAsyncHttpClient(): CloseableHttpAsyncClient {
    return HttpAsyncClients.custom()
        .setDefaultRequestConfig(requestConfig)
        .setSSLStrategy(newSSLIOSessionStrategy())
        .build()
  }

  /**
   * HTTPS 통신을 위해 [[SSLIOSessionStrategy]] 를 생성합니다.
   *
   * @return { @link SSLIOSessionStrategy} 인스턴스
   * @throws HttpException
   */
  @Throws(HttpException::class)
  fun newSSLIOSessionStrategy(): SSLIOSessionStrategy {
    try {
      val trustStore = KeyStore.getInstance(KeyStore.getDefaultType())
      trustStore.load(null, null)

      val sslContext = SSLContexts.custom()
          .loadTrustMaterial(trustStore, TrustSelfSignedStrategy())
          .build()

      return SSLIOSessionStrategy(sslContext, arrayOf("TLSv1"), null, NoopHostnameVerifier())
    } catch(e: Exception) {
      throw HttpException("SSLIOSessionStrategy를 빌드하는데 실패했습니다.", e)
    }
  }
}