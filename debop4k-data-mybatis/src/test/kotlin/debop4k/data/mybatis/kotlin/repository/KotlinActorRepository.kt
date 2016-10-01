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

package debop4k.data.mybatis.kotlin.repository

import debop4k.core.uninitialized
import debop4k.data.mybatis.kotlin.mappers.KotlinActorMapper
import debop4k.data.mybatis.kotlin.models.KotlinActor
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
 * KotlinActorRepository
 * @author sunghyouk.bae@gmail.com
 */
@Repository
@Transactional
@CacheConfig(cacheNames = arrayOf("kotlin-actor"))
open class KotlinActorRepository {

  @Inject private val actorMapper: KotlinActorMapper = uninitialized()
  @Inject private val sqlTemplate: SqlSessionTemplate = uninitialized()

  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  open fun findById(id: Int): KotlinActor {
    return this.actorMapper.findById(id)
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  open fun findByFirstname(@NonNull firstname: String): KotlinActor {
    return this.actorMapper.findByFirstname(firstname)
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  open fun findByFirstnameWithSqlSessionTemplate(@NonNull firstname: String): KotlinActor {
    return sqlTemplate.selectOne<KotlinActor>("selectActorByFirstname", firstname)
  }

  @Transactional(readOnly = true)
  open fun findAll(): List<KotlinActor> {
    return actorMapper.findAll()
  }

  @CachePut(key = "#actor.id")
  open fun insertActor(actor: KotlinActor): KotlinActor {
    sqlTemplate.insert("insertActor", actor)
    return actor
  }

  @CacheEvict(key = "#id")
  open fun deleteById(id: Int?) {
    actorMapper.deleteById(id)
  }
}