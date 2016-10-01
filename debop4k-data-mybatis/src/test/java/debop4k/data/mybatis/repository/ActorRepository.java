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

package debop4k.data.mybatis.repository;

import debop4k.data.mybatis.mappers.ActorMapper;
import debop4k.data.mybatis.models.Actor;
import lombok.NonNull;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Repository
@Transactional
@CacheConfig(cacheNames = {"actor"})
public class ActorRepository {

  @Inject private ActorMapper actorMapper;
  @Inject private SqlSessionTemplate sqlTemplate;

  @Transactional(readOnly = true)
  @Cacheable(key = "#id")
  public Actor findById(int id) {
    return this.actorMapper.findById(id);
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  public Actor findByFirstname(@NonNull String firstname) {
    return this.actorMapper.findByFirstname(firstname);
  }

  @Transactional(readOnly = true)
  @Cacheable(key = "'firstname=' + #firstname")
  public Actor findByFirstnameWithSqlSessionTemplate(@NonNull String firstname) {
    return sqlTemplate.selectOne("selectActorByFirstname", firstname);
  }

  @Transactional(readOnly = true)
  public List<Actor> findAll() {
    return actorMapper.findAll();
  }

  @CachePut(key = "#actor.id")
  public Actor insertActor(Actor actor) {
    sqlTemplate.insert("insertActor", actor);
    return actor;
  }

  @CacheEvict(key = "#id")
  public void deleteById(Integer id) {
    actorMapper.deleteById(id);
  }

}
