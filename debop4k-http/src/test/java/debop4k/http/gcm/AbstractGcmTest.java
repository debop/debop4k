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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
public abstract class AbstractGcmTest {

  public static final String SERVICE_ID = "healthon";

  public static final String PROJECT_ID = "1028834060650";

  public static final String SERVER_API_KEY = "AIzaSyCVQy7ncg8UXg7S7V2tviEel0mIaAN-dlc";

  public static final String DEVICE_ID_PANTEC = "APA91bEZtlVWrxUadbKuQLBMjndJPxiHiEq1P0kpaA70_3puVt_0DeD_V64pliM4eyPxsoAWqCv4lPTqpt6VSGXNc26-sQr9QSo3_SgMIZpDxkBydtL26FJ4PVcaISpXetyCbHlpnIQqxsT-hQ9KDp3mTI-1nZI4R15fJg_esysTs5QTZlnD87o";
  public static final String DEVICE_ID_LG = "APA91bHgGjn9z64XXL4jZPwd3EcodgQVzAVeVLFSJtDFiQyRBKYJra2Z5Ql_k8vDnPCDxDTV8SsYKD1m-vKMeoljCuTiTijj5p06dEEEt3cjSfH3zOLryL_qALsQK21OMamHv8APvi6P9B2e7Vt5ohBQrYZFxQ7kMv8YPVry74SGFwMf15gjFoY";
  public static final String DEVICE_ID_G3 = "APA91bHv81dkphwt6_oohetApSPWPx78iu5EPUmezWvIRtV7zktBSI_FhsSuRx6yUb-XTL1e2hKlDq4XmXHu9xFYljLS5G3OV9-DhP1pEvzZfefTHRcD2Edunn3qfTs6747j_pj9XqHzo4wuXsn_N52Iu_dh5K8Hlw";

  public static final List<String> DEVICE_ID_LIST = FastList.newListWith(DEVICE_ID_LG,
                                                                         DEVICE_ID_PANTEC,
                                                                         DEVICE_ID_LG,
                                                                         DEVICE_ID_PANTEC);

  protected GcmMessage newSampleGcmMessage() {
    HashSet<String> deviceIds = new HashSet<String>(DEVICE_ID_LIST);
    HashMap<String, String> data = new HashMap<String, String>();
    data.put("EventKey", "이벤트 키");
    data.put("Body", "동해물과 백두산이");

    GcmMessage msg = new GcmMessage();
    msg.getRegistrationIds().addAll(deviceIds);
    msg.getData().putAll(data);
    return msg;

//    return GcmMessage.builder()
//                     .registrationIds(deviceIds)
//                     .data(data)
//                     .build();
  }
}
