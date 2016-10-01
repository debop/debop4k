///*
// * Copyright (c) 2016. KESTI co, ltd
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package debop4k.mongodb.cache;
//
//import debop4k.core.io.serializers.Serializer;
//import debop4k.core.io.serializers.SnappyFstJava6Serializer;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.collections.api.block.function.Function;
//import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.data.mongodb.core.MongoTemplate;
//
//import java.util.Collection;
//
///**
// * MongoDB를 저장소로 사용하는 Spring @Cacheable용 Cache 관리자입니다.
// * Spring Application Context 에 MongoCacheManager를 Bean으로 등록하셔야 합니다.
// * <p>
// * <pre><code>
// * @Bean
// * public MongoCacheMaanger mongoCacheManager() {
// *    return new MongoCacheManager(mongo, 120);
// * }
// * </code></pre>
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Getter
//@Slf4j
//public class MongoCacheManager implements CacheManager, DisposableBean {
//
//  private final Serializer valueSerializer = SnappyFstJava6Serializer.of();
//  private final ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
//  private final ConcurrentHashMap<String, Long> expires = new ConcurrentHashMap<String, Long>();
//
//  private final MongoTemplate mongo;
//
//  @Getter
//  @Setter
//  private long expirationInSeconds;
//
//  private boolean usePrefix = false;
//  private MongoCachePrefix cachePrefix = new DefaultMongoCachePrefix();
//
//  public MongoCacheManager(MongoTemplate mongo) {
//    this(mongo, 60 * 60 * 1000);
//  }
//
//  public MongoCacheManager(MongoTemplate mongo, long expirationInSeconds) {
//    this.mongo = mongo;
//    this.expirationInSeconds = expirationInSeconds;
//  }
//
//  @Override
//  public Collection<String> getCacheNames() {
//    return caches.keySet();
//  }
//
//  @Override
//  public Cache getCache(final String name) {
//    return caches.getIfAbsentPut(name, new Function<String, Cache>() {
//      @Override
//      public Cache valueOf(String key) {
//        long expiration = computeExpiration(name);
//        String prefix = usePrefix ? cachePrefix.prefix(name) : "";
//
//        return MongoCache.of(name, prefix, mongo, expiration, valueSerializer);
//      }
//    });
//  }
//
//  @Override
//  public void destroy() throws Exception {
//    log.info("MongoDB를 저장소로 사용하는 CacheManager를 제거합니다...");
//    if (caches.size() > 0) {
//      try {
//        for (Cache cache : caches.values()) {
//          cache.clear();
//        }
//        caches.clear();
//        expires.clear();
//      } catch (Exception e) {
//        log.warn("MongoCacheManager를 제거하는데 실패했습니다.", e);
//      }
//    }
//  }
//
//  private long computeExpiration(String name) {
//    return expires.getIfAbsentPut(name, this.expirationInSeconds);
//  }
//}
