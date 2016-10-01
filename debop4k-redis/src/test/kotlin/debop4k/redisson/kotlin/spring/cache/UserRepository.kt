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

package debop4k.redisson.kotlin.spring.cache

import debop4k.core.AbstractValueObject
import debop4k.core.utils.hashOf
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import java.io.Serializable

/**
 * NOTE: Kotlin 으로 Spring Compoent 를 만들려면 모두 open 을 지정해주어야 합니다.
 * @author sunghyouk.bae@gmail.com
 */
@Component
@CacheConfig(cacheNames = arrayOf("kotlinUsers"))
open class UserRepository {

  @CachePut(key = "#key")
  open fun save(key: String, obj: UserObject?): UserObject? {
    return obj
  }

  @CachePut(key = "#key")
  open fun saveNull(key: String): UserObject? {
    return null
  }

  @CacheEvict(key = "#key")
  open fun remove(key: String) {
  }

  @Cacheable(key = "#key")
  open fun get(key: String): UserObject? {
    throw IllegalStateException()
  }

  @Cacheable(key = "#key")
  open fun getNull(key: String): UserObject? {
    return null
  }

  open class UserObject(var name: String? = null,
                        var value: String? = null) : AbstractValueObject(), Serializable {

    override fun hashCode(): Int {
      return hashOf(name, value)
    }
  }
}