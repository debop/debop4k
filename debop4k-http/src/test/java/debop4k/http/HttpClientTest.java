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

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Slf4j
public class HttpClientTest extends AbstractHttpTest {

  @Test
  @SneakyThrows({URISyntaxException.class})
  public void testGetMethod() {

    HttpClient client = new HttpClient();
    try {
      String responseStr = client.get(new URI(URI_DUCKDUCKGO), Consts.UTF_8);
      log.trace("Get response html={}", responseStr);
      assertThat(responseStr).isNotEmpty().contains("html");
    } finally {
      client.close();
    }
  }

  @Test
  public void testGetWithParameters() {
    URI uri = makeURIWithSearchString("scala");

    HttpClient client = new HttpClient();
    try {
      String responseStr = client.get(uri, Consts.UTF_8);
      log.trace("Get response html={}", responseStr);
      assertThat(responseStr).isNotEmpty().contains("scala");
    } finally {
      client.close();
    }
  }

  @Test
  @SneakyThrows({URISyntaxException.class})
  public void testPostWithParameters() {
    URI uri = new URIBuilder(URI_DUCKDUCKGO).build();
    List<BasicNameValuePair> nvps = FastList.newListWith(new BasicNameValuePair("q", "scala"));

    HttpClient client = new HttpClient();
    try {
      String responseStr = client.post(uri, nvps);
      log.trace("Get response html={}", responseStr);
      assertThat(responseStr).isNotEmpty().contains("scala");
    } finally {
      client.close();
    }
  }

  @Test
  @SneakyThrows({IOException.class})
  public void testResponseHandler() {
    URI uri = makeURIWithSearchString("scala");
    HttpGet httpget = new HttpGet(uri);
    ResponseHandler<String> responseHandler = new BasicResponseHandler();
    String responseBody = HttpClients.createDefault().execute(httpget, responseHandler);

    assertThat(responseBody).isNotEmpty().contains("scala");
    log.trace(responseBody);
  }

  @Test
  public void executeHttpGetByAsynchronous() throws Exception {
    CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
    try {
      client.start();

      URI uri = makeURIWithSearchString("scala");
      HttpGet httpget = new HttpGet(uri);
      Future<HttpResponse> futureRepsonse = client.execute(httpget, null);

      while (!futureRepsonse.isDone()) {
        log.trace("....");
        Thread.sleep(5L);
      }
      HttpResponse response = futureRepsonse.get();
      String responseBody = EntityUtils.toString(response.getEntity());

      assertThat(responseBody).isNotEmpty().contains("scala");
      log.trace(responseBody);
    } finally {
      client.close();
    }
  }

  @Test
  public void mutipleHttpGetByAsynchronous() throws Exception {
    final PoolingNHttpClientConnectionManager connectionManager =
        new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor());

    CloseableHttpAsyncClient client =
        HttpAsyncClients.custom().setConnectionManager(connectionManager).build();
    try {
      client.start();

      final URI uri = makeURIWithSearchString("scala");
      final HttpGet httpget = new HttpGet(uri);

      for (int i = 0; i < 10; i++) {
        try {
          Future<HttpResponse> futureRepsonse = client.execute(httpget, null);
          HttpResponse response = futureRepsonse.get();
          String responseBody = EntityUtils.toString(response.getEntity());

          assertThat(responseBody).isNotEmpty().contains("scala");
          log.trace(responseBody);
        } catch (Exception e) {
          log.error("예외가 발생했습니다.", e);
          fail("예외 발생. message=" + e.getMessage());
        }
      }
    } finally {
      client.close();
    }
  }
}
