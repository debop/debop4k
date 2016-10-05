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

package debop4k.http.gcm;

import debop4k.core.asyncs.Asyncx;
import debop4k.core.json.JacksonSerializer;
import debop4k.core.json.JsonSerializer;
import debop4k.http.AsyncHttpx;
import kotlin.text.Charsets;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GcmHttpPostTest extends AbstractGcmTest {

  private static final JsonSerializer jackson = new JacksonSerializer();

  @Test
  public void convertGcmPushMessageToJsonText() {

    GcmMessage msg = newSampleGcmMessage();

    String text = jackson.toString(msg);
    log.debug("json text={}", text);

    GcmMessage msg2 = jackson.fromString(text, GcmMessage.class);
    assertThat(msg2).isEqualTo(msg);
  }

  @Test
  public void sendMulticastPushMessageToGCMServer() throws Exception {
    HttpPost post = new HttpPost(GcmSender.GCM_SERVER_URI);
    post.addHeader("Authorization", "key=" + SERVER_API_KEY);
    post.addHeader("Content-Type", "application/json");

    GcmMessage msg = newSampleGcmMessage();

    String text = jackson.toString(msg);
    log.debug("json text={}", text);

    post.setEntity(new StringEntity(text, Charsets.UTF_8));

    Promise<HttpResponse, Exception> future = AsyncHttpx.executeAsync(post);
    HttpResponse response = Asyncx.result(future);

    log.trace("Response={}", response.getStatusLine());
    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
  }

  @Test(expected = AssertionError.class)
  public void sendPushMessageWithoutHeader() {

    HttpPost post = new HttpPost(GcmSender.GCM_SERVER_URI);
    GcmMessage msg = newSampleGcmMessage();
    String text = jackson.toString(msg);
    post.setEntity(new StringEntity(text, Charsets.UTF_8));

    Promise<HttpResponse, Exception> future = AsyncHttpx.executeAsync(post);
    HttpResponse response = Asyncx.result(future);

    log.trace("Response={}", response.getStatusLine());
    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
  }
}
