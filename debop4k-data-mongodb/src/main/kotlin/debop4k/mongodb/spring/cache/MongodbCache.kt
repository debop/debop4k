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

import com.mongodb.WriteResult
import debop4k.core.asyncs.future
import debop4k.core.io.serializers.Serializer
import debop4k.core.io.serializers.Serializers
import debop4k.core.loggerOf
import nl.komponents.kovenant.Promise
import org.springframework.cache.Cache
import org.springframework.cache.Cache.ValueWrapper
import org.springframework.cache.support.SimpleValueWrapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.util.concurrent.*

/**
 * Mongodb 를 캐시 저장소로 사용하는 Cache 입니다
 * @author sunghyouk.bae@gmail.com
 */
class MongodbCache(@JvmField val name: String,
                   val mongo: MongoTemplate,
                   val expirationInSeconds: Long,
                   val serializer: Serializer = Serializers.FST) : Cache {

  private val log = loggerOf(javaClass)

  init {
    log.debug("create MongodbCache. name={}, expiration={} sec", name, expirationInSeconds)
  }

  override fun getName(): String = name
  override fun getNativeCache(): Any = mongo

  override fun get(key: Any?): ValueWrapper? {
    val item = get(key, Any::class.java)
//    return if (item != null) SimpleValueWrapper(item) else null
    return item?.let { return SimpleValueWrapper(item) } ?: null
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any?> get(key: Any?, type: Class<T>?): T? {
    log.trace("캐시를 로드합니다... collection name={}, key={}", name, key)

    var value: T? = null

    try {
      val query = Query.query(Criteria.where("key").`is`(key))
      val item = mongo.findOne(query, MongodbCacheItem::class.java, name)

      if (item != null) {
        val isNotExpired = item.expireAt <= 0 || item.expireAt > System.currentTimeMillis()
        if (isNotExpired) {
          value = serializer.deserialize(item.value)
        } else {
          future { evict(key) }
        }
      }
    } catch(ignored: Exception) {
      log.warn("캐시 조회에 실패했습니다. collection name={}, key={}", name, key, ignored)
    }
    if (value == null)
      log.debug("기존 캐시 정보가 없습니다. collection name={}, key={}", name, key)

    return value
  }

  @Suppress("UNCHECKED_CAST")
  override fun <T : Any?> get(key: Any?, valueLoader: Callable<T>?): T? {
    try {
      val wrapper = get(key)
      return (wrapper?.get() ?: valueLoader?.call() ?: null) as T
    } catch(ignored: Exception) {
      return null as T
    }
  }

  fun putAsync(key: Any?, value: Any?): Promise<WriteResult, Exception> {
    return future {
      putInternal(key, value)
    }
  }

  override fun put(key: Any?, value: Any?) {
    putInternal(key, value)
  }

  private fun putInternal(key: Any?, value: Any?): WriteResult {
    log.trace("캐시를 저장합니다... collection name={}, key={}", name, key)

    val query = Query.query(Criteria.where("key").`is`(key))
    val update = Update.update("value", serializer.serialize(value))
    if (expirationInSeconds > 0)
      update.set("expireAt", System.currentTimeMillis() + expirationInSeconds * 1000)

    return mongo.upsert(query, update, name)
  }

  override fun putIfAbsent(key: Any?, value: Any?): ValueWrapper? {
    val oldValue = get(key)
    if (oldValue == null || oldValue.get() == null) {
      put(key, value)
    }
    return oldValue
  }

  override fun evict(key: Any?) {
    try {
      log.trace("캐시를 삭제합니다. collection name={}, key={}", name, key)
      val query = Query.query(Criteria.where("key").`is`(key))
      mongo.remove(query, name)
    } catch(ignored: Exception) {
      log.warn("캐시를 삭제하는데 실패했습니다. collection name={}, key={}", name, key, ignored)
    }
  }

  override fun clear() {
    try {
      log.trace("캐시 컬렉션을 삭제합니다... collection name={}", name)
      mongo.dropCollection(name)
    } catch(ignored: Exception) {
      log.warn("캐시 컬렉션을 삭제하는데 실패했습니다. collection name={}", name, ignored)
    }
  }
}