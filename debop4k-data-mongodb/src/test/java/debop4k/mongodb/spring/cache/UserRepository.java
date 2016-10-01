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

package debop4k.mongodb.spring.cache;

import debop4k.mongodb.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class UserRepository {

  @Cacheable(value = "user", key = "'user:' + #id")
  public User getUser(String id, int favoriteMovieSize) {
    log.debug("새로운 사용자를 생성합니다. id={}", id);
    User user = User.of(favoriteMovieSize);
    user.setId(id);

    return user;
  }

  @CacheEvict(value = "user", key = "'user:' + #user.id")
  public void updateUser(User user) {
    log.debug("사용자 정보를 갱신합니다. 캐시에서 삭제됩니다...");
  }
}
