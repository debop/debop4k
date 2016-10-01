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

import debop4k.core.io.serializers.Serializers
import debop4k.core.loggerOf
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap
import org.springframework.beans.factory.DisposableBean
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.data.mongodb.core.MongoTemplate

/**
 * MongoDB를 저장소로 사용하는 Spring @Cacheable용 Cache 관리자입니다.
 * Spring Application Context 에 MongoCacheManager를 Bean으로 등록하셔야 합니다.
 * <p>
 * <pre><code>
 * @Bean
 * public MongoCacheMaanger mongoCacheManager() {
 *    return new MongoCacheManager(mongo, 120);
 * }
 * </code></pre>
 *
 * @author sunghyouk.bae@gmail.com
 */
class MongodbCacheManager
@JvmOverloads constructor(val mongo: MongoTemplate,
                          val expirationInSeconds: Long = 60 * 60 * 1000L) : CacheManager, DisposableBean {

  private val log = loggerOf(javaClass)

  val valueSerializer = Serializers.FST
  val caches = ConcurrentHashMap<String, Cache>()
  val expires = ConcurrentHashMap<String, Long>()

  override fun getCacheNames(): MutableCollection<String>? = caches.keys

  override fun getCache(name: String?): Cache {
    return caches.getIfAbsentPut(name!!) { key ->
      val expiration = computeExpiration(key)
      MongodbCache(key, mongo, expiration, valueSerializer)
    }
  }

  override fun destroy() {
    log.debug("MongoDB를 저장소로 사용하는 CacheManager를 제거합니다...")
    if (caches.notEmpty()) {
      try {
        caches.values.forEach { it.clear() }
        caches.clear()
        expires.clear()
      } catch(ignored: Exception) {
        log.warn("MongodbCacheManager를 제거하는데 실패했습니다.", ignored)
      }
    }
  }

  private fun computeExpiration(name: String): Long
      = expires.getIfAbsentPut(name, expirationInSeconds)
}