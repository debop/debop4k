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

package debop4k.mongodb.spring.cache

import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import debop4k.mongodb.AbstractMongoDocument

/**
 * MongoDB에 저장할 캐시 값을 표현합니다.
 * @author sunghyouk.bae@gmail.com
 */
open class MongodbCacheItem
@JvmOverloads constructor(var key: Any? = null,
                          var value: ByteArray? = null,
                          var expireAt: Long = 0) : AbstractMongoDocument() {

  override fun hashCode(): Int {
    return hashOf(key)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("key", key)
        .add("value", value)
        .add("expireAt", expireAt)
  }

  companion object {
    const val serialVersionUID = 5081372628460588627L
  }
}