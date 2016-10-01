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

package debop4k.data.orm.springdata.examples.caching;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


@CacheConfig(cacheNames = {"users"})
public interface UserRepository extends JpaRepository<User, Integer> {

  // Spring EL 과 관련해서 주의해야 할 사항.
  // Spring Data JPA 에서는 @Param("xxx") 를 지정하면 #xxx 를 @Query 등에서 사용가능하나 (이 것도 Java 8에서는 필요없다고 하는데, 그렇지 않은 듯)
  // @Cacheable 관련된 곳에서는 제대로 동작하지 않는다.
  // 그래서 #root.args 를 사용한다.

  @Override
  @CacheEvict(key = "'username:' + #root.args[0].username")
  <S extends User> S save(@Param("user") S user);

  @CachePut(key = "'username:' + #root.args[0]")
  User findByUsername(@Param("username") String username);

  @Cacheable(key = "'userId:' + #root.args[0]")
  User findById(@Param("id") Integer id);
}
