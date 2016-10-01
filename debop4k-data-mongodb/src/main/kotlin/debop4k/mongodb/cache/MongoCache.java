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
//import com.mongodb.WriteResult;
//import debop4k.core.asyncs.AsyncEx;
//import debop4k.core.io.serializers.FstJava6Serializer;
//import debop4k.core.io.serializers.Serializer;
//import lombok.Getter;
//import lombok.extern.slf4j.Slf4j;
//import nl.komponents.kovenant.Promise;
//import org.springframework.cache.Cache;
//import org.springframework.cache.support.SimpleValueWrapper;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//
//import java.util.concurrent.Callable;
//
///**
// * Spring @Cacheable 을 지원하고, MongoDB에 캐시 항목을 관리하는 클래스입니다.
// *
// * @author sunghyouk.bae@gmail.com
// */
//@Slf4j
//@Getter
//@SuppressWarnings("unchecked")
//public class MongoCache implements Cache {
//
//  public static MongoCache of(String name, String prefix, MongoTemplate mongo) {
//    return of(name, prefix, mongo, 0, FstJava6Serializer.of());
//  }
//
//  public static MongoCache of(String name, String prefix, MongoTemplate mongo, long expiration) {
//    return of(name, prefix, mongo, expiration, FstJava6Serializer.of());
//  }
//
//  public static MongoCache of(String name, String prefix, MongoTemplate mongo, long expiration, Serializer serializer) {
//    return new MongoCache(name, prefix, mongo, expiration, serializer);
//  }
//
//  private final String name;
//  private final String prefix;
//  private final MongoTemplate mongo;
//  private final long expirationInSeconds;
//  private final Serializer serializer;
//
//  public MongoCache(String name, String prefix, MongoTemplate mongo, long expirationInSeconds, Serializer serializer) {
//    this.name = name;
//    this.prefix = prefix;
//    this.mongo = mongo;
//    this.expirationInSeconds = expirationInSeconds;
//    this.serializer = serializer;
//  }
//
//  @Override
//  public Object getNativeCache() {
//    return mongo;
//  }
//
//  @Override
//  public ValueWrapper get(Object key) {
//    Object item = this.get(key, Object.class);
//    return (item != null) ? new SimpleValueWrapper(item) : null;
//  }
//
//  @Override
//  public <T> T get(final Object key, Class<T> type) {
//    log.trace("캐시를 로드합니다. key={}", key);
//    T value = null;
//
//    try {
//      MongoCacheItem item = mongo.findOne(Query.query(Criteria.where("key").is(key)), MongoCacheItem.class, name);
//      if (item != null) {
//        log.trace("캐시 값 조회. item={}", item);
//
//        boolean isNotExpired = item.getExpireAt() <= 0 || item.getExpireAt() > System.currentTimeMillis();
//        if (isNotExpired)
//          value = serializer.deserialize(item.getValue());
//        else
//          AsyncEx.future(new Runnable() {
//            @Override
//            public void run() {
//              evict(key);
//            }
//          });
//      }
//    } catch (Exception e) {
//      log.warn("캐시 조회에 실패했습니다. key=" + key, e);
//    }
//
//    return value;
//  }
//
//  @Override
//  public <T> T get(Object key, Callable<T> valueLoader) {
//    try {
//      ValueWrapper wrapper = get(key);
//      return (wrapper == null) ? valueLoader.call() : (T) wrapper.get();
//    } catch (Exception e) {
//      return (T) null;
//    }
//  }
//
//  public Promise<WriteResult, Exception> putAsync(final Object key, final Object value) {
//    log.trace("캐시를 저장합니다. key={}", key);
//
//    return AsyncEx.future(new Callable<WriteResult>() {
//      @Override
//      public WriteResult call() throws Exception {
//        Query query = Query.query(Criteria.where("key").is(key));
//        Update update = Update.update("value", serializer.serialize(value));
//        if (expirationInSeconds > 0)
//          update.set("expireAt", System.currentTimeMillis() + expirationInSeconds * 1000L);
//
//        return mongo.upsert(query, update, name);
//      }
//    });
//  }
//
//  @Override
//  public void put(Object key, Object value) {
//    log.trace("캐시를 저장합니다. key={}", key);
//
//    Query query = Query.query(Criteria.where("key").is(key));
//    Update update = Update.update("value", serializer.serialize(value));
//    if (expirationInSeconds > 0)
//      update.set("expireAt", System.currentTimeMillis() + expirationInSeconds * 1000L);
//
//    mongo.upsert(query, update, name);
//  }
//
//  @Override
//  public ValueWrapper putIfAbsent(Object key, Object value) {
//    ValueWrapper oldValue = get(key);
//    if (oldValue == null || oldValue.get() == null) {
//      put(key, value);
//    }
//    return oldValue;
//  }
//
//  @Override
//  public void evict(Object key) {
//    log.trace("캐시를 삭제합니다. key={}", key);
//
//    try {
//      Query query = Query.query(Criteria.where("key").is(key));
//      mongo.remove(query, name);
//    } catch (Exception e) {
//      log.warn("캐시를 삭제하는데 실패했습니다. key=" + key, e);
//    }
//  }
//
//  @Override
//  public void clear() {
//    log.debug("캐시 컬렉션을 삭제합니다. name={}", name);
//    try {
//      mongo.dropCollection(name);
//    } catch (Exception e) {
//      log.warn("캐시 컬렉션을 삭제하는데 실패했습니다. name=" + name, e);
//    }
//  }
//}
