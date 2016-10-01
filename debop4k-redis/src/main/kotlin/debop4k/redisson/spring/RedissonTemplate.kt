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

package debop4k.redisson.spring

import org.redisson.api.*
import org.redisson.client.codec.Codec
import org.redisson.config.Config
import org.redisson.liveobject.provider.CodecProvider
import org.redisson.liveobject.provider.ResolverProvider
import java.util.concurrent.*

/**
 * {@link RedissonClient} 를 이용하여 여러가지 자료구조를 제공합니다.
 * <p>
 * 참고: https://github.com/mrniko/redisson/wiki/8.-Redis-commands-mapping
 *
 * @author sunghyouk.bae@gmail.com
 */
class RedissonTemplate(val redisson: RedissonClient) : RedissonClient {

  override fun <V> getGeo(name: String): RGeo<V> = redisson.getGeo<V>(name)
  override fun <V> getGeo(name: String, codec: Codec): RGeo<V> = redisson.getGeo<V>(name, codec)

  override fun <V> getSetCache(name: String): RSetCache<V> = redisson.getSetCache<V>(name)
  override fun <V> getSetCache(name: String, codec: Codec): RSetCache<V> = redisson.getSetCache<V>(name, codec)

  override fun <K, V> getMapCache(name: String, codec: Codec): RMapCache<K, V> = redisson.getMapCache<K, V>(name, codec)
  override fun <K, V> getMapCache(name: String): RMapCache<K, V> = redisson.getMapCache<K, V>(name)

  override fun <V> getBucket(name: String): RBucket<V> = redisson.getBucket<V>(name)
  override fun <V> getBucket(name: String, codec: Codec): RBucket<V> = redisson.getBucket<V>(name, codec)

  override fun getBuckets(): RBuckets = redisson.buckets
  override fun getBuckets(codec: Codec): RBuckets = redisson.getBuckets(codec)

  override fun <V> getHyperLogLog(name: String): RHyperLogLog<V> = redisson.getHyperLogLog<V>(name)
  override fun <V> getHyperLogLog(name: String, codec: Codec): RHyperLogLog<V> = redisson.getHyperLogLog<V>(name, codec)

  override fun <V> getList(name: String): RList<V> = redisson.getList<V>(name)
  override fun <V> getList(name: String, codec: Codec): RList<V> = redisson.getList<V>(name, codec)

  override fun <K, V> getListMultimap(name: String): RListMultimap<K, V> = redisson.getListMultimap<K, V>(name)
  override fun <K, V> getListMultimap(name: String, codec: Codec): RListMultimap<K, V>
      = redisson.getListMultimap<K, V>(name, codec)

  override fun <K, V> getListMultimapCache(name: String): RListMultimapCache<K, V> = redisson.getListMultimapCache<K, V>(name)
  override fun <K, V> getListMultimapCache(name: String, codec: Codec): RListMultimapCache<K, V>
      = redisson.getListMultimapCache<K, V>(name, codec)

  override fun <K, V> getMap(name: String): RMap<K, V> = redisson.getMap<K, V>(name)
  override fun <K, V> getMap(name: String, codec: Codec): RMap<K, V> = redisson.getMap<K, V>(name, codec)

  override fun <K, V> getSetMultimap(name: String): RSetMultimap<K, V> = redisson.getSetMultimap<K, V>(name)
  override fun <K, V> getSetMultimap(name: String, codec: Codec): RSetMultimap<K, V>
      = redisson.getSetMultimap<K, V>(name, codec)

  override fun <K, V> getSetMultimapCache(name: String): RSetMultimapCache<K, V> = redisson.getSetMultimapCache<K, V>(name)
  override fun <K, V> getSetMultimapCache(name: String, codec: Codec): RSetMultimapCache<K, V>
      = redisson.getSetMultimapCache<K, V>(name, codec)

  override fun getSemaphore(name: String): RSemaphore = redisson.getSemaphore(name)
  override fun getLock(name: String): RLock = redisson.getLock(name)
  override fun getFairLock(name: String): RLock = redisson.getFairLock(name)
  override fun getReadWriteLock(name: String): RReadWriteLock = redisson.getReadWriteLock(name)

  override fun <V> getSet(name: String): RSet<V> = redisson.getSet<V>(name)
  override fun <V> getSet(name: String, codec: Codec): RSet<V> = redisson.getSet<V>(name, codec)

  override fun <V> getSortedSet(name: String): RSortedSet<V> = redisson.getSortedSet<V>(name)
  override fun <V> getSortedSet(name: String, codec: Codec): RSortedSet<V> = redisson.getSortedSet<V>(name, codec)

  override fun <V> getScoredSortedSet(name: String): RScoredSortedSet<V> = redisson.getScoredSortedSet<V>(name)
  override fun <V> getScoredSortedSet(name: String, codec: Codec): RScoredSortedSet<V>
      = redisson.getScoredSortedSet<V>(name, codec)

  override fun getLexSortedSet(name: String): RLexSortedSet = redisson.getLexSortedSet(name)

  override fun <M> getTopic(name: String): RTopic<M> = redisson.getTopic<M>(name)
  override fun <M> getTopic(name: String, codec: Codec): RTopic<M> = redisson.getTopic<M>(name, codec)

  override fun <M> getPatternTopic(pattern: String): RPatternTopic<M> = redisson.getPatternTopic<M>(pattern)
  override fun <M> getPatternTopic(pattern: String, codec: Codec): RPatternTopic<M>
      = redisson.getPatternTopic<M>(pattern, codec)

  override fun <V> getQueue(name: String): RQueue<V> = redisson.getQueue<V>(name)
  override fun <V> getQueue(name: String, codec: Codec): RQueue<V> = redisson.getQueue<V>(name, codec)

  override fun <V> getBlockingQueue(name: String): RBlockingQueue<V> = redisson.getBlockingQueue<V>(name)
  override fun <V> getBlockingQueue(name: String, codec: Codec): RBlockingQueue<V>
      = redisson.getBlockingQueue<V>(name, codec)

  override fun <V> getBoundedBlockingQueue(s: String): RBoundedBlockingQueue<V> = redisson.getBoundedBlockingQueue<V>(s)
  override fun <V> getBoundedBlockingQueue(s: String, codec: Codec): RBoundedBlockingQueue<V>
      = redisson.getBoundedBlockingQueue<V>(s, codec)

  override fun <V> getDeque(name: String): RDeque<V> = redisson.getDeque<V>(name)
  override fun <V> getDeque(name: String, codec: Codec): RDeque<V> = redisson.getDeque<V>(name, codec)

  override fun <V> getBlockingDeque(name: String): RBlockingDeque<V> = redisson.getBlockingDeque<V>(name)
  override fun <V> getBlockingDeque(name: String, codec: Codec): RBlockingDeque<V> = redisson.getBlockingDeque<V>(name, codec)

  override fun getAtomicLong(name: String): RAtomicLong = redisson.getAtomicLong(name)
  override fun getAtomicDouble(name: String): RAtomicDouble = redisson.getAtomicDouble(name)

  override fun getCountDownLatch(name: String): RCountDownLatch = redisson.getCountDownLatch(name)

  override fun getBitSet(name: String): RBitSet = redisson.getBitSet(name)

  override fun <V> getBloomFilter(name: String): RBloomFilter<V> = redisson.getBloomFilter<V>(name)
  override fun <V> getBloomFilter(name: String, codec: Codec): RBloomFilter<V>
      = redisson.getBloomFilter<V>(name, codec)

  override fun getScript(): RScript = redisson.script

  override fun getExecutorService(s: String): RScheduledExecutorService = redisson.getExecutorService(s)
  override fun getExecutorService(codec: Codec, s: String): RScheduledExecutorService
      = redisson.getExecutorService(codec, s)

  override fun getRemoteSerivce(): RRemoteService = redisson.remoteSerivce
  override fun getRemoteSerivce(codec: Codec): RRemoteService = redisson.getRemoteSerivce(codec)
  override fun getRemoteSerivce(name: String): RRemoteService = redisson.getRemoteSerivce(name)
  override fun getRemoteSerivce(name: String, codec: Codec): RRemoteService = redisson.getRemoteSerivce(name, codec)

  override fun createBatch(): RBatch = redisson.createBatch()

  override fun getKeys(): RKeys = redisson.keys

  override fun getLiveObjectService(): RLiveObjectService = redisson.liveObjectService
  override fun getLiveObjectService(codecProvider: CodecProvider, resolverProvider: ResolverProvider): RLiveObjectService
      = redisson.getLiveObjectService(codecProvider, resolverProvider)

  override fun shutdown() = redisson.shutdown()

  override fun shutdown(quietPeriod: Long, timeout: Long, unit: TimeUnit) = redisson.shutdown(quietPeriod, timeout, unit)

  override fun getConfig(): Config = redisson.config

  override fun getNodesGroup(): NodesGroup<Node> = redisson.nodesGroup

  override fun getClusterNodesGroup(): ClusterNodesGroup = redisson.clusterNodesGroup

  override fun isShutdown(): Boolean = redisson.isShutdown

  override fun isShuttingDown(): Boolean = redisson.isShuttingDown
}