/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core

import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.*

/**
 * Thread Context 별로 Local Storage를 제공하는 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Suppress("UNCHECKED_CAST")
object Local {

  private val log by lazy { LoggerFactory.getLogger(Local::class.java) }

  private val threadLocal by lazy {
    object : ThreadLocal<HashMap<Any, Any?>>() {
      override fun initialValue(): HashMap<Any, Any?> {
        return HashMap<Any, Any?>()
      }
    }
  }

  @JvmStatic
  internal val storage: HashMap<Any, Any?> by lazy {
    threadLocal.get()
  }

  @JvmStatic
  fun save(): HashMap<Any, Any?> = storage.clone() as HashMap<Any, Any?>

  @JvmStatic
  fun restore(saved: HashMap<Any, Any?>): Unit = threadLocal.set(saved)

  @Suppress("UNCHECKED_CAST")
  @JvmStatic
  operator fun <T> get(key: Any): T? {
    return storage[key] as T?
  }

  @JvmStatic
  operator fun <T> set(key: Any, value: T?): Unit {
    if (value != null)
      storage.put(key, value)
  }

  @JvmStatic
  fun clearAll(): Unit {
    log.debug("clear local storage")
    storage.clear()
  }

  @JvmStatic
  fun <T> getOrPut(key: Any, defaultValue: () -> T?): T? {
    return storage.getOrPut(key, defaultValue) as T?
  }

  @JvmStatic
  fun <T> getOrPut(key: Any, factory: Callable<T?>): T? {
    return storage.getOrPut(key, { factory.call() }) as T?
  }
}

class LocalStorage<T> {

  private val key: UUID by lazy { UUID.randomUUID() }

  fun get(): T? = Local[key]

  fun set(value: T?): Unit {
    Local[key] = value
  }

  fun update(value: T?): Unit = set(value)

  fun clear(): Any? = Local.storage.remove(key)

}