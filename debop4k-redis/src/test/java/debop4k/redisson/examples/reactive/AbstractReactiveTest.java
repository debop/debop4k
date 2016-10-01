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

package debop4k.redisson.examples.reactive;//package debop4k.redisson.examples.reactive;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.BeforeClass;
//import org.reactivestreams.Publisher;
//import org.redisson.Config;
//import org.redisson.Redisson;
//import org.redisson.api.RCollectionReactive;
//import org.redisson.api.RScoredSortedSetReactive;
//import org.redisson.api.RedissonReactiveClient;
//import reactor.rx.Promise;
//import reactor.rx.Streams;
//
//import java.util.Iterator;
//
//public abstract class AbstractReactiveTest {
//
//  protected static RedissonReactiveClient redisson;
//
//  @BeforeClass
//  public static void beforeClass() {
//    redisson = createInstance();
//  }
//
//  @AfterClass
//  public static void afterClass() {
//    redisson.shutdown();
//  }
//
//  @After
//  public void after() {
//    sync(redisson.getKeys().flushdb());
//  }
//
//  public static Config createConfig() {
//    String redisAddress = System.getProperty("redisAddress", "127.0.0.1:6379");
//    Config config = new Config();
//    config.useSingleServer().setAddress(redisAddress);
//    return config;
//  }
//
//  public static RedissonReactiveClient createInstance() {
//    return Redisson.createReactive(createConfig());
//  }
//
//  public <V> Iterable<V> sync(RScoredSortedSetReactive<V> list) {
//    return Streams.create(list.iterator()).toList().poll();
//  }
//
//  public <V> Iterable<V> sync(RCollectionReactive<V> list) {
//    return Streams.create(list.iterator()).toList().poll();
//  }
//
//  public <V> Iterator<V> toIterator(Publisher<V> pub) {
//    return Streams.create(pub).toList().poll().iterator();
//  }
//
//  public <V> Iterable<V> toIterable(Publisher<V> pub) {
//    return Streams.create(pub).toList().poll();
//  }
//
//  public <V> V sync(Publisher<V> ob) {
//    Promise<V> promise;
//    if (Promise.class.isAssignableFrom(ob.getClass())) {
//      promise = (Promise<V>) ob;
//    } else {
//      promise = Streams.wrap(ob).next();
//    }
//
//    V val = promise.poll();
//    if (promise.isError()) {
//      throw new RuntimeException(promise.reason());
//    }
//    return val;
//  }
//}
