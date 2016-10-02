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

package debop4k.gcm

import com.fasterxml.jackson.annotation.JsonProperty
import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import java.util.*

/**
 * 구글 푸시 메시지 (GCM)
 * @author sunghyouk.bae@gmail.com
 */
class GcmMessage : AbstractValueObject() {

  /** 등록된 디바이스의 Id (스마트 폰의 고유 Device Id) */
  @JsonProperty("registration_ids")
  val registrationIds: Set<String> = HashSet<String>()

  @JsonProperty("collapse_key")
  var collapseKey: String? = null

  @JsonProperty("time_to_live")
  var timeToLive: Int = 0

  @JsonProperty("delay_while_idle")
  val delayWhileIdle: Boolean = false

  @JsonProperty("data")
  val data: Map<String, String?> = HashMap<String, String?>()

  override fun hashCode(): Int {
    return hashOf(collapseKey, registrationIds)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("registrationIds", registrationIds)
        .add("collapseKey", collapseKey)
        .add("timeToLive", timeToLive)
        .add("delayWhileIdle", delayWhileIdle)
        .add("data", data)
  }

  companion object {
    const val serialVersionUID = 4242831662352331816L

    @JvmStatic fun of(): GcmMessage = GcmMessage()
  }
}