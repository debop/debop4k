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

package debop4k.http;

import debop4k.core.asyncs.Asyncx;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

import static debop4k.http.AsyncHttpx.executeAsync;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class HttpAsyncExTest extends AbstractHttpTest {

  private static final String SEARCH_WORD = "scala";

  @Test
  public void executeByAsynchronous() {
    URI uri = makeURIWithSearchString(SEARCH_WORD);
    HttpUriRequest request = HttpRequestBuilder.of().setMethod("GET").setUri(uri).setCharset(Consts.UTF_8).build();
    Promise<HttpResponse, Exception> promise = executeAsync(request);
    assertHttpResponse(promise, HttpStatus.SC_OK, SEARCH_WORD);
  }

  @Test
  public void executeByHttpGetWithAsynchronous() {
    HttpGet httpget = new HttpGet(makeURIWithSearchString("scala"));

    Promise<HttpResponse, Exception> promise = executeAsync(httpget);
    assertHttpResponse(promise, HttpStatus.SC_OK, SEARCH_WORD);
  }

  @Test
  public void executeByHttpPostWithAsynchronous() {
    HttpPost httppost = new HttpPost(makeURIWithSearchString("scala"));

    Promise<HttpResponse, Exception> promise = executeAsync(httppost);
    assertHttpResponse(promise, HttpStatus.SC_METHOD_NOT_ALLOWED, SEARCH_WORD);
  }

  @Test
  @Ignore("JDK 1.7 이상부터 SSL 을 사용할 수 있습니다.")
  public void executeSSLGetWithAsynchronous() {
    URI uri = makeSSLURI();
    HttpGet httpget = new HttpGet(uri);

    Promise<HttpResponse, Exception> promise = executeAsync(httpget);
    assertHttpResponse(promise, HttpStatus.SC_OK, "Apache");
  }

  @SneakyThrows({IOException.class, ParseException.class})
  private void assertHttpResponse(Promise<HttpResponse, Exception> promise,
                                  final int expectedStatusCode,
                                  final String expectedSearchWord) {
    HttpResponse response = Asyncx.result(promise);
    assertThat(response).isNotNull();
    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(expectedStatusCode);

    if (expectedStatusCode == HttpStatus.SC_OK) {
      String responseBody = EntityUtils.toString(response.getEntity());
      assertThat(responseBody).isNotEmpty().contains(expectedSearchWord);
    }
  }

}
