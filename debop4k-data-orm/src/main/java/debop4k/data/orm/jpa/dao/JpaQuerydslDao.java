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

package debop4k.data.orm.jpa.dao;

import debop4k.data.orm.jpa.JpaEx;
import kotlin.jvm.functions.Function1;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * QueryDSL for JPA 를 손쉽게 사용할 수 있도록 해줍니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@Repository
@Transactional
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JpaQuerydslDao {

  @Inject EntityManagerFactory emf;
  @PersistenceContext EntityManager em;

  public <T> T withNewEntityManager(Function1<EntityManager, T> block) {
    return JpaEx.withNewEntityManager(emf, block);
  }

  @Transactional(readOnly = true)
  public <T> T withReadOnly(@NonNull Function1<EntityManager, T> block) {
    return block.invoke(em);
  }
}
