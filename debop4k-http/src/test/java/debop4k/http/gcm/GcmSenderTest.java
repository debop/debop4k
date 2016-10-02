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


import debop4k.gcm.GcmMessage;
import debop4k.gcm.GcmSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class GcmSenderTest extends AbstractGcmTest {

  @Test
  public void sendGcmMessage() {
    GcmSender sender = GcmSender.of();
    GcmMessage msg = newSampleGcmMessage();
    msg.getRegistrationIds().clear();
    msg.getRegistrationIds().add(DEVICE_ID_PANTEC);
    msg.setTimeToLive(60);

    int result = sender.send(SERVER_API_KEY, msg);
    assertThat(result).isEqualTo(HttpStatus.SC_OK);
  }

  @Test
  public void sendGcmMessageToMultipleDevice() {
    GcmSender sender = GcmSender.of();
    GcmMessage msg = newSampleGcmMessage();
    msg.setTimeToLive(60);

    int result = sender.send(SERVER_API_KEY, msg);
    assertThat(result).isEqualTo(HttpStatus.SC_OK);
  }
}
