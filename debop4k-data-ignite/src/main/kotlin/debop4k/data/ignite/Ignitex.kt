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

@file:JvmName("Ignitex")

package debop4k.data.ignite

import debop4k.core.use
import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.configuration.NearCacheConfiguration

/**
 * Apache Ignite 를 시작하고, 시작된 [Ignite] 인스턴스를 반환합니다.
 */
fun startIgnite(cfg: IgniteConfiguration = IgniteConfiguration()): Ignite {
  return Ignition.getOrStart(cfg)
}

/**
 * [Ignite] 를 시작하고, 작업 후 [Ignite] 인스턴스를 종료 시킵니다.
 */
fun usingIgnite(cfg: IgniteConfiguration = IgniteConfiguration(), action: (Ignite) -> Unit): Unit {
  Ignition.getOrStart(cfg).use { action(it) }
}

fun <K, V> Ignite.getCache(cacheName: String): IgniteCache<K, V> {
  return this.getOrCreateCache(cacheName)
}

fun <K, V> Ignite.getCache(cacheCfg: CacheConfiguration<K, V>, nearCfg: NearCacheConfiguration<K, V>): IgniteCache<K, V> {
  return this.getOrCreateCache(cacheCfg, nearCfg)
}

fun <K, V> Ignite.getNearCache(cacheName: String, nearCfg: NearCacheConfiguration<K, V>): IgniteCache<K, V> {
  return this.getOrCreateNearCache(cacheName, nearCfg)
}

fun <K, V> Ignite.getCacheAsync(cacheName: String): IgniteCache<K, V> {
  return this.getCache<K, V>(cacheName).withAsync()
}
