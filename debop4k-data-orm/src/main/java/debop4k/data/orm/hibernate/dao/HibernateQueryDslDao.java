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

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateUpdateClause;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * Hibernate 와 QueryDSL 을 이용한 DAO (Data Access Object)
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
public class HibernateQueryDslDao {

  @Inject private SessionFactory sf;

  private Session session() {
    return sf.getCurrentSession();
  }

  public StatelessSession openStateless() {
    return sf.openStatelessSession();
  }

  public HibernateQuery query() {
    return new HibernateQuery(session());
  }

  @SuppressWarnings("unchecked")
  public HibernateQuery from(EntityPath<?> path) {
    return (HibernateQuery) query().from(path);
  }

  @SuppressWarnings("unchecked")
  public HibernateQuery from(EntityPath<?>... paths) {
    return (HibernateQuery) query().from(paths);
  }

  public HibernateDeleteClause deleteFrom(EntityPath<?> path) {
    return new HibernateDeleteClause(session(), path);
  }

  public HibernateUpdateClause updateFrom(EntityPath<?> path) {
    return new HibernateUpdateClause(session(), path);
  }

  public long deleteStateless(EntityPath<?> path, Function1<HibernateDeleteClause, Unit> action) {
    try {
      HibernateDeleteClause deleteClause = new HibernateDeleteClause(openStateless(), path);
      action.invoke(deleteClause);
      return deleteClause.execute();
    } catch (Exception e) {
      log.error("삭제 작업 시 예외가 발생했습니다.", e);
      return -1L;
    }
  }

  public long updateStateless(EntityPath<?> path, Function1<HibernateUpdateClause, Unit> action) {
    try {
      HibernateUpdateClause updateClause = new HibernateUpdateClause(openStateless(), path);
      action.invoke(updateClause);
      return updateClause.execute();
    } catch (Exception e) {
      log.error("Update 작업 시 예외가 발생했습니다.", e);
      return -1L;
    }
  }

  public <T> T load(Class<T> clazz, Serializable id) {
    return session().load(clazz, id);
  }

  public <T> T load(Class<T> clazz, Serializable id, LockOptions lockOptions) {
    return session().load(clazz, id, lockOptions);
  }

  public <T> T load(Class<T> clazz, Serializable id, LockMode lockMode) {
    return session().load(clazz, id, lockMode);
  }

  public <T> T get(Class<T> clazz, Serializable id) {
    return session().get(clazz, id);
  }

  public <T> T get(Class<T> clazz, Serializable id, LockOptions lockOptions) {
    return session().get(clazz, id, lockOptions);
  }

  public <T> T get(Class<T> clazz, Serializable id, LockMode lockMode) {
    return session().get(clazz, id, lockMode);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(EntityPath<T> path) {
    return query().from(path).fetchAll().fetch();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(HibernateQuery query, Expression<T> expr) {
    return (List<T>) query.select(expr).fetch();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(HibernateQuery query, Expression<T> expr, int offset, int limit) {
    return ((HibernateQuery<T>) query.select(expr).offset(offset).limit(limit)).fetch();
  }

  public <T> Page<T> getPage(HibernateQuery query, Expression<T> expr, Pageable page) {
    long total = query.fetchCount();
    int offset = page.getOffset();
    int limit = page.getPageSize();
    List<T> items = findAll(query, expr, offset, limit);

    return new PageImpl<T>(items, page, total);
  }
}
