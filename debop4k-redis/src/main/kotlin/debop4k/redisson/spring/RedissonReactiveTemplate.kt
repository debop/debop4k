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

/**
 * RedissonReactiveTemplate
 * @author sunghyouk.bae@gmail.com
 */
class RedissonReactiveTemplate(val redisson: RedissonReactiveClient) : RedissonReactiveClient {

  override fun <V> findBuckets(pattern: String?): MutableList<RBucketReactive<V>>
      = redisson.findBuckets(pattern)

  override fun <V> getSetCache(name: String): RSetCacheReactive<V> = redisson.getSetCache<V>(name)
  override fun <V> getSetCache(name: String, codec: Codec): RSetCacheReactive<V> = redisson.getSetCache<V>(name, codec)

  override fun <K, V> getMapCache(name: String, codec: Codec): RMapCacheReactive<K, V> = redisson.getMapCache<K, V>(name, codec)
  override fun <K, V> getMapCache(name: String): RMapCacheReactive<K, V> = redisson.getMapCache<K, V>(name)

  override fun <V> getBucket(name: String): RBucketReactive<V> = redisson.getBucket<V>(name)
  override fun <V> getBucket(name: String, codec: Codec): RBucketReactive<V> = redisson.getBucket<V>(name, codec)

  override fun <V> getHyperLogLog(name: String): RHyperLogLogReactive<V> = redisson.getHyperLogLog<V>(name)
  override fun <V> getHyperLogLog(name: String, codec: Codec): RHyperLogLogReactive<V> = redisson.getHyperLogLog<V>(name, codec)

  override fun <V> getList(name: String): RListReactive<V> = redisson.getList<V>(name)
  override fun <V> getList(name: String, codec: Codec): RListReactive<V> = redisson.getList<V>(name, codec)

  override fun <K, V> getMap(name: String): RMapReactive<K, V> = redisson.getMap<K, V>(name)
  override fun <K, V> getMap(name: String, codec: Codec): RMapReactive<K, V> = redisson.getMap<K, V>(name, codec)

  override fun <V> getSet(name: String): RSetReactive<V> = redisson.getSet<V>(name)
  override fun <V> getSet(name: String, codec: Codec): RSetReactive<V> = redisson.getSet<V>(name, codec)

  override fun <V> getScoredSortedSet(name: String): RScoredSortedSetReactive<V> = redisson.getScoredSortedSet<V>(name)
  override fun <V> getScoredSortedSet(name: String, codec: Codec): RScoredSortedSetReactive<V>
      = redisson.getScoredSortedSet<V>(name, codec)

  override fun getLexSortedSet(name: String): RLexSortedSetReactive = redisson.getLexSortedSet(name)

  override fun <M> getTopic(name: String): RTopicReactive<M> = redisson.getTopic<M>(name)
  override fun <M> getTopic(name: String, codec: Codec): RTopicReactive<M> = redisson.getTopic<M>(name, codec)

  override fun <M> getPatternTopic(pattern: String): RPatternTopicReactive<M> = redisson.getPatternTopic<M>(pattern)
  override fun <M> getPatternTopic(pattern: String, codec: Codec): RPatternTopicReactive<M>
      = redisson.getPatternTopic<M>(pattern, codec)

  override fun <V> getQueue(name: String): RQueueReactive<V> = redisson.getQueue<V>(name)
  override fun <V> getQueue(name: String, codec: Codec): RQueueReactive<V> = redisson.getQueue<V>(name, codec)

  override fun <V> getBlockingQueue(name: String): RBlockingQueueReactive<V> = redisson.getBlockingQueue<V>(name)
  override fun <V> getBlockingQueue(name: String, codec: Codec): RBlockingQueueReactive<V>
      = redisson.getBlockingQueue<V>(name, codec)

  override fun <V> getDeque(name: String): RDequeReactive<V> = redisson.getDeque<V>(name)
  override fun <V> getDeque(name: String, codec: Codec): RDequeReactive<V> = redisson.getDeque<V>(name, codec)

  override fun getAtomicLong(name: String): RAtomicLongReactive = redisson.getAtomicLong(name)

  override fun getBitSet(name: String): RBitSetReactive = redisson.getBitSet(name)

  override fun getScript(): RScriptReactive = redisson.script


  override fun createBatch(): RBatchReactive = redisson.createBatch()

  override fun getKeys(): RKeysReactive = redisson.keys

  override fun shutdown() = redisson.shutdown()

  override fun getConfig(): Config = redisson.config

  override fun getNodesGroup(): NodesGroup<Node> = redisson.nodesGroup

  override fun getClusterNodesGroup(): NodesGroup<ClusterNode> = redisson.clusterNodesGroup

  override fun isShutdown(): Boolean = redisson.isShutdown

  override fun isShuttingDown(): Boolean = redisson.isShuttingDown
}