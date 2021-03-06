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

package debop4k.redisson.spring.cache

import debop4k.core.loggerOf
import org.redisson.api.RMapCache
import org.springframework.cache.Cache
import org.springframework.cache.Cache.ValueWrapper
import org.springframework.cache.support.SimpleValueWrapper
import java.util.concurrent.*

/**
 * Spring Cache Client for Redis
 *
 * @author sunghyouk.bae@gmail.com
 */
open class RedissonCache(val mapCache: RMapCache<Any?, Any?>,
                         val expiration: Long = DEFAULT_CACHE_TIMEOUT) : Cache {

  private val log = loggerOf(javaClass)

  override fun getName(): String = mapCache.name
  override fun getNativeCache(): Any = mapCache

  override fun get(key: Any?): ValueWrapper? {
    log.trace("retrieve cache item... key={}", key)
    val value = mapCache.get(key)
    return toValueWrapper(value)
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any?> get(key: Any?, type: Class<T>?): T {
    log.trace("retrieve cache item... key={}", key)
    val value = mapCache.get(key)

    if (isNullOrNullCacheValue(value)) {
      return null as T
    } else {
      if (type != null && !type.isInstance(value)) {
        error("Cache value is not of required type [${type.name}]: $value")
      }
    }
    return value as T
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any?> get(key: Any?, valueLoader: Callable<T>?): T {
    val wrapper = get(key)
    try {
      return (wrapper?.get() ?: valueLoader?.call()) as T
    } catch(e: Throwable) {
      return null as T
    }
  }

  override fun put(key: Any?, value: Any?) {
    log.trace("insert cache item... key={}, value={}", key, value)

    if (expiration > 0) {
      mapCache.fastPut(key, value, expiration, TimeUnit.MILLISECONDS)
    } else {
      mapCache.fastPut(key, value)
    }
  }

  override fun putIfAbsent(key: Any?, value: Any?): ValueWrapper? {
    log.trace("insert cache item if absent... key={}, value={}", key, value)

    val oldValue: Any? =
        if (expiration > 0) {
          mapCache.putIfAbsent(key, value, expiration, TimeUnit.MILLISECONDS)
        } else {
          mapCache.putIfAbsent(key, value)
        }
    return toValueWrapper(oldValue)
  }

  override fun evict(key: Any?) {
    mapCache.fastRemove(key)
  }

  override fun clear() {
    mapCache.clear()
  }

  private fun toValueWrapper(value: Any?): ValueWrapper? {
    return value?.let(::SimpleValueWrapper)
  }

  private fun isNullOrNullCacheValue(value: Any?): Boolean {
    return value == null || value.javaClass.name == NullCacheValue::class.java.name
  }
}