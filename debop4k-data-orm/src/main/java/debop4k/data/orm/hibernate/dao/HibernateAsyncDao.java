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

package debop4k.data.orm.hibernate.dao;

import debop4k.core.asyncs.Asyncs;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nl.komponents.kovenant.Promise;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.Callable;

/**
 * Hibernate 는 기본적으로 Thread-safe 하지 않지만, 성능을 위해 새로운 Session을 만들어 부가 정보를 얻을 수 있도록 합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
@Getter
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
@SuppressWarnings("unchecked")
public class HibernateAsyncDao extends HibernateDao {

  /**
   * 비동기 방식으로 {@link DetachedCriteria} 질의에 해당하는 ROW COUNT 를 구합니다.
   */
  public Promise<Long, Exception> countAsync(@NonNull DetachedCriteria dc) {
    return uniqueResultAsync(dc.setProjection(Projections.rowCount()));
  }

  /**
   * 비동기 방식으로 Criteria#uniqueResult 를 수행합니다.
   */
  public <T> Promise<T, Exception> uniqueResultAsync(@NonNull final DetachedCriteria dc) {

    return Asyncs.future(new Callable<T>() {
      @Override
      public T call() throws Exception {
        Session sess = getSf().openSession();
        try {
          return (T) dc.getExecutableCriteria(sess).uniqueResult();
        } finally {
          sess.close();
        }
      }
    });
  }

}
