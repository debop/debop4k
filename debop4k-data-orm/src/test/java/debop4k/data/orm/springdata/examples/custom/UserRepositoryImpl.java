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

package debop4k.data.orm.springdata.examples.custom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * {@link UserRepository} 의 구현체이다. 꼭 Repository + Impl 으로 명명해야 한다.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {

  @PersistenceContext EntityManager em;

  public void setEntityManager(EntityManager em) {
    this.em = em;
  }

  @Override
  public List<User> customBatchOperation() {
    CriteriaQuery<User> criteriaQuery = em.getCriteriaBuilder().createQuery(User.class);
    criteriaQuery.select(criteriaQuery.from(User.class));

    return em.createQuery(criteriaQuery).getResultList();
  }
}
