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

package debop4k.redisson.spring.cache

import debop4k.core.utils.asLong
import debop4k.redisson.DEFAULT_DELIMETER
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.redisson.api.RMapCache
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager


/**
 * Spring Cache 를 관리하는 {@link CacheManager} 에 대해 Redis 를 캐시 저장소로 사용하고,
 * client driver 를 redisson 을 사용하는 CacheManager 의 구현체입니다.
 * <p>
 * <p>
 * Example:
 * <pre>
 *   &#64;Configuration
 *   &#64;EnableCaching
 *   public class ApplicationConfiguration {
 *
 *     &#64;Bean
 *     CacheManager redissonCacheManager(RedissonClient redisson) {
 *       return new RedissonCacheManager(redisson);
 *     }
 *   }
 * </pre>
 * @author sunghyouk.bae@gmail.com
 */
open class RedissonCacheManager(val redisson: RedissonClient) : CacheManager, DisposableBean {

  private val log = LoggerFactory.getLogger(javaClass)

  private val caches = ConcurrentHashMap<String, RedissonCache>()
  private val expires = ConcurrentHashMap<String, Long>()

  var defaultExpiryInMillis: Long = 60 * 60 * 1000L   // 60 분


//  override fun loadCaches(): MutableCollection<out Cache>? {
//    return caches.values
//  }

  override fun getCacheNames(): MutableCollection<String>? {
    return caches.keys.toMutableList()
  }

  override fun getCache(name: String?): Cache {
    require(name != null) { "cache name should not be null." }

    // cache name 에 "name:9000" 이런 식이라면 이름과 Expiration 으로 구분
    if (name!!.contains(DEFAULT_DELIMETER)) {
      val names = name.split(DEFAULT_DELIMETER)
      val expiration = names[1].asLong(defaultExpiryInMillis)
      return caches.getIfAbsentPut(names[0], createCache(names[0], expiration))
    } else {
      return caches.getIfAbsentPut(name, createCache(name))
    }
  }

  @Synchronized
  private fun createCache(name: String, expiration: Long = defaultExpiryInMillis): RedissonCache {
    val map: RMapCache<Any?, Any?> = redisson.getMapCache<Any?, Any?>(name)
    return RedissonCache(map, expiration)
  }

  @Synchronized
  open override fun destroy() {
    log.trace("Redis Cache 를 제거합니다...")
    try {
      caches.values.forEach { it.clear() }
    } catch(ignored: Throwable) {
      log.debug("RedisCacheManager 를 제거하는 동안 예외가 발생했습니다. 무시합니다.", ignored)
    }
    log.debug("Redisson CacheManager Bean을 제거했습니다.")
  }
}