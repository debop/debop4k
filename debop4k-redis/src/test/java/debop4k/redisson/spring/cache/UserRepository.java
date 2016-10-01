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

package debop4k.redisson.spring.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Slf4j
@Repository
@CacheConfig(cacheNames = {"users:90*1000"})
public class UserRepository {

  @CachePut(key = "#key")
  public UserObject save(String key, UserObject obj) {
    return obj;
  }

  @CachePut(key = "#key")
  public UserObject saveNull(String key) {
    return null;
  }

  @CacheEvict(key = "#key")
  public void remove(String key) {
  }

  @Cacheable(key = "#key")
  public UserObject get(String key) {
    throw new IllegalStateException();
  }

  @Cacheable(key = "#key")
  public UserObject getNull(String key) {
    return null;
  }

  @Data
  @NoArgsConstructor(staticName = "of")
  @AllArgsConstructor(staticName = "of")
  public static class UserObject implements Serializable {
    private String name;
    private String value;
  }
}
