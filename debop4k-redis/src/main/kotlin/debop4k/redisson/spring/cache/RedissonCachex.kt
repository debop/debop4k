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
 */

@file:JvmName("RedissonCachex")

package debop4k.redisson.spring.cache

import debop4k.redisson.DEFAULT_DELIMETER
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.KeyGenerator
import java.lang.reflect.Method

const val DEFAULT_CACHE_TIMEOUT = 60 * 60 * 1000L // 60 Minutes

/**
 * 캐시 값이 NULL 인 것을 표현하는 클래스
 */
class NullCacheValue : Cache.ValueWrapper {
  override fun get(): Any? {
    return null
  }

  companion object {
    @JvmField val INSTANCE = NullCacheValue()
  }
}

/**
 * Redis의 key convension 을 적용한 key generator 입니다. (구분자를 ':' 를 사용합니다.)
 */
class RedissonCacheKeyGenerator : KeyGenerator {

  override fun generate(target: Any?, method: Method?, vararg params: Any?): Any {
    val builder = StringBuilder()

    method?.let {
      builder.append(it.name).append(DEFAULT_DELIMETER)
    }

    params.forEach {
      builder.append(it).append(DEFAULT_DELIMETER)
    }

    builder.deleteCharAt(builder.lastIndex)
    return builder.toString()
  }

}

