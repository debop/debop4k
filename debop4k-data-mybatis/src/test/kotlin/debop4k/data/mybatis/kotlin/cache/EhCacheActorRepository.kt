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

package debop4k.data.mybatis.kotlin.cache

import debop4k.core.uninitialized
import debop4k.data.mybatis.kotlin.mappers.KotlinActorMapper
import debop4k.data.mybatis.kotlin.models.Actor
import lombok.NonNull
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.inject.Inject

/**
 * EhCacheActorRepository
 * @author sunghyouk.bae@gmail.com
 */
@Repository
@Transactional
@CacheConfig(cacheNames = arrayOf("kotlin-actors"))
open class EhCacheActorRepository {

  @Inject val actorMapper: KotlinActorMapper = uninitialized()
  @Inject val sqlTemplate: SqlSessionTemplate = uninitialized()

  /**
   * Actor id 값으로 Actor를 조회
   * `@Cacheable` 은 먼저 Cache 에서 해당 Actor가 있는지 조회하고, 없다면 DB에서 읽어와서 캐시에 저장한 후 반환한다.
   * 캐시에 데이터가 존재한다면 캐시의 Actor 를 반환하고, DB를 읽지 않는다

   * @param id Actor의 identifier
   * *
   * @return 조회된 Actor (없으면 NULL 반환)
   */
  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  open fun findById(id: Int): Actor {
    return this.actorMapper.findById(id)
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  open fun findByFirstname(@NonNull firstname: String): Actor {
    return this.actorMapper.findByFirstname(firstname)
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  open fun findByFirstnameWithSqlSessionTemplate(@NonNull firstname: String): Actor {
    return sqlTemplate.selectOne<Actor>("selectActorByFirstname", firstname)
  }

  @Transactional(readOnly = true)
  open fun findAll(): List<Actor> {
    return actorMapper.findAll()
  }

  /**
   * DB에 새로운 Actor 를 추가하고, Cache에 미리 저장해둔다

   * @param actor DB에 추가할 Actor 정보
   * *
   * @return 저장된 Actor 정보 (발급된 Id 값이 있다)
   */
  @CachePut(key = "#actor.id")
  open fun insertActor(actor: Actor): Actor {
    sqlTemplate.insert("insertActor", actor)
    return actor
  }

  /**
   * 캐시에서 해당 정보를 삭제하고, DB에서 데이터를 삭제한다.

   * @param id 삭제할 Actor 의 identifier
   */
  @CacheEvict(key = "#id")
  open fun deleteById(id: Int?) {
    actorMapper.deleteById(id)
  }
}