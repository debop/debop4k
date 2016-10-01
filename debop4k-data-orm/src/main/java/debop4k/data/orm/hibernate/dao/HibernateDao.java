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

import debop4k.core.collections.Arrayx;
import debop4k.core.utils.Convertx;
import debop4k.data.orm.hibernate.CriteriaEx;
import debop4k.data.orm.hibernate.HibernateEx;
import debop4k.data.orm.hibernate.HibernateParameter;
import debop4k.data.orm.model.HibernateEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Hibernate 용 기본 DAO (Data Access Object)
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
public class HibernateDao {

  @Inject private SessionFactory sf;

  public Session session() { return sf.getCurrentSession(); }

  public void flush() {
    session().flush();
  }

  public <T> T load(@NonNull Class<T> clazz, Serializable id) {
    return session().load(clazz, id);
  }

  public <T> T load(@NonNull Class<T> clazz, Serializable id, LockOptions lockOptions) {
    return session().load(clazz, id, lockOptions);
  }

  public <T> T get(@NonNull Class<T> clazz, Serializable id) {
    return session().get(clazz, id);
  }

  public <T> T get(@NonNull Class<T> clazz, Serializable id, LockOptions lockOptions) {
    return session().get(clazz, id, lockOptions);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> getIn(@NonNull Class<T> clazz, Collection<?> ids) {
    DetachedCriteria dc = CriteriaEx.addIn(DetachedCriteria.forClass(clazz), "id", ids);
    return (List<T>) find(dc);
  }

  public ScrollableResults scroll(@NonNull Class<?> clazz) {
    return scroll(DetachedCriteria.forClass(clazz));
  }

  public ScrollableResults scroll(@NonNull DetachedCriteria dc) {
    return dc.getExecutableCriteria(session()).scroll();
  }

  public ScrollableResults scroll(@NonNull DetachedCriteria dc, ScrollMode scrollMode) {
    return dc.getExecutableCriteria(session()).scroll(scrollMode);
  }

  public ScrollableResults scroll(@NonNull Criteria criteria) {
    return criteria.scroll();
  }

  public ScrollableResults scroll(@NonNull Criteria criteria, ScrollMode scrollMode) {
    return criteria.scroll(scrollMode);
  }

  public ScrollableResults scroll(@NonNull Query query) {
    return query.scroll();
  }

  public ScrollableResults scroll(@NonNull Query query, ScrollMode scrollMode) {
    return query.scroll(scrollMode);
  }

  public ScrollableResults scroll(@NonNull Query query, HibernateParameter... parameters) {
    return CriteriaEx.setParameters(query, asList(parameters))
                     .scroll();
  }

  public ScrollableResults scroll(@NonNull Query query, ScrollMode scrollMode, HibernateParameter... parameters) {
    return CriteriaEx.setParameters(query, asList(parameters))
                     .scroll(scrollMode);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(@NonNull Class<T> clazz, Order... orders) {
    Criteria criteria = session().createCriteria(clazz);
    if (!Arrayx.isNullOrEmpty(orders)) {
      CriteriaEx.addOrders(criteria, asList(orders));
    }
    return criteria.list();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(@NonNull Class<T> clazz, Pageable pageable) {
    Criteria criteria = session().createCriteria(clazz);
    return CriteriaEx.setPageable(criteria, pageable).list();
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findAll(@NonNull Class<T> clazz, int firstResult, int maxResults, Order... orders) {
    Criteria criteria = session().createCriteria(clazz);
    if (!Arrayx.isNullOrEmpty(orders)) {
      CriteriaEx.addOrders(criteria, asList(orders));
    }
    CriteriaEx.setFirstResult(criteria, firstResult);
    CriteriaEx.setMaxResults(criteria, maxResults);
    return criteria.list();
  }


  public List find(@NonNull Criteria criteria, Order... orders) {
    return CriteriaEx.addOrders(criteria, asList(orders)).list();
  }

  public List find(@NonNull Criteria criteria, @NonNull Pageable pageable) {
    return CriteriaEx.setPageable(criteria, pageable).list();
  }

  public List find(@NonNull Criteria criteria, int firstResult, int maxResults, Order... orders) {
    CriteriaEx.setPaging(criteria, firstResult, maxResults);
    CriteriaEx.addOrders(criteria, asList(orders));
    return criteria.list();
  }

  public List find(@NonNull DetachedCriteria dc, Order... orders) {
    return find(dc.getExecutableCriteria(session()), orders);
  }

  public List find(@NonNull DetachedCriteria dc, Pageable pageable) {
    return find(dc.getExecutableCriteria(session()), pageable);
  }

  public List find(@NonNull DetachedCriteria dc, int firstResult, int maxResults, Order... orders) {
    return find(dc.getExecutableCriteria(session()), firstResult, maxResults, orders);
  }

  public List find(@NonNull Query query, HibernateParameter... parameters) {
    return CriteriaEx.setParameters(query, asList(parameters)).list();
  }

  public List find(@NonNull Query query, Pageable pageable, HibernateParameter... parameters) {
    CriteriaEx.setPageable(query, pageable);
    CriteriaEx.setParameters(query, asList(parameters));
    return query.list();
  }

  public List find(@NonNull Query query, int firstResult, int maxResults, HibernateParameter... parameters) {
    CriteriaEx.setPaging(query, firstResult, maxResults);
    CriteriaEx.setParameters(query, asList(parameters));
    return query.list();
  }

  public List findByHql(@NonNull String hql, HibernateParameter... parameters) {
    return find(session().createQuery(hql), parameters);
  }

  public List findByHql(@NonNull String hql, Pageable pageable, HibernateParameter... parameters) {
    return find(session().createQuery(hql), pageable, parameters);
  }

  public List findByHql(@NonNull String hql, int firstResult, int maxResults, HibernateParameter... parameters) {
    return find(session().createQuery(hql), firstResult, maxResults, parameters);
  }

  public List findByNamedQuery(@NonNull String queryName, HibernateParameter... parameters) {
    return find(session().getNamedQuery(queryName), parameters);
  }

  public List findByNamedQuery(@NonNull String queryName, Pageable pageable, HibernateParameter... parameters) {
    return find(session().getNamedQuery(queryName), pageable, parameters);
  }

  public List findByNamedQuery(@NonNull String queryName, int firstResult, int maxResults, HibernateParameter... parameters) {
    return find(session().getNamedQuery(queryName), firstResult, maxResults, parameters);
  }

  public List findBySQLString(@NonNull String queryString, HibernateParameter... parameters) {
    return find(session().createSQLQuery(queryString), parameters);
  }

  public List findBySQLString(@NonNull String queryString, Pageable pageable, HibernateParameter... parameters) {
    return find(session().createSQLQuery(queryString), pageable, parameters);
  }

  public List findBySQLString(@NonNull String queryString, int firstResult, int maxResults, HibernateParameter... parameters) {
    return find(session().createSQLQuery(queryString), firstResult, maxResults, parameters);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> findByExample(@NonNull Class<T> clazz, @NonNull Example example) {
    return session().createCriteria(clazz).add(example).list();
  }

  @SuppressWarnings("unchecked")
  public Page getPage(@NonNull Criteria criteria, @NonNull Pageable page) {
    // get total n
    Criteria countCriteria = CriteriaEx.copyCriteria(criteria);
    long totalCount = count(countCriteria);
    // get list
    List items = find(CriteriaEx.setPageable(criteria, page));

    return new PageImpl(items, page, totalCount);
  }

  public Page getPage(@NonNull DetachedCriteria dc, @NonNull Pageable page) {
    return getPage(dc.getExecutableCriteria(session()), page);
  }

  @SuppressWarnings("unchecked")
  public Page getPage(@NonNull Query query, @NonNull Pageable page, HibernateParameter... parameters) {
    long totalCount = count(query, parameters);
    List items = find(CriteriaEx.setPageable(query, page));
    return new PageImpl(items, page, totalCount);
  }

  public Page getPageByHql(@NonNull String hql, @NonNull Pageable page, HibernateParameter... parameters) {
    return getPage(session().createQuery(hql), page, parameters);
  }

  public Page getPageByNamedQuery(@NonNull String queryName, @NonNull Pageable page, HibernateParameter... parameters) {
    return getPage(session().getNamedQuery(queryName), page, parameters);
  }

  public Page getPageBySQLString(@NonNull String queryString, @NonNull Pageable page, HibernateParameter... parameters) {
    return getPage(session().createSQLQuery(queryString), page, parameters);
  }

  @SuppressWarnings("unchecked")
  public <T> T findUnique(@NonNull Criteria criteria) {
    return (T) criteria.uniqueResult();
  }

  public <T> T findUnique(@NonNull DetachedCriteria dc) {
    return findUnique(dc.getExecutableCriteria(session()));
  }

  @SuppressWarnings("unchecked")
  public <T> T findUnique(@NonNull Query query, HibernateParameter... parameters) {
    return (T) CriteriaEx.setParameters(query, parameters).uniqueResult();
  }

  public <T> T findUniqueByHql(@NonNull String hql, HibernateParameter... parameters) {
    return findUnique(session().createQuery(hql), parameters);
  }

  public <T> T findUniqueByNamedQuery(@NonNull String queryName, HibernateParameter... parameters) {
    return findUnique(session().getNamedQuery(queryName), parameters);
  }

  public <T> T findUniqueBySQLString(@NonNull String sqlString, HibernateParameter... parameters) {
    return findUnique(session().createSQLQuery(sqlString), parameters);
  }

  public <T> T findFirst(Criteria criteria, Order... orders) {
    @SuppressWarnings("unchecked")
    List<T> items = find(criteria, 0, 1, orders);
    return items.isEmpty() ? null : items.get(0);
  }

  public <T> T findFirst(DetachedCriteria dc, Order... orders) {
    return findFirst(dc.getExecutableCriteria(session()), orders);
  }

  public <T> T findFirst(Query query, HibernateParameter... parameters) {
    @SuppressWarnings("unchecked")
    List<T> items = find(query, 0, 1, parameters);
    return items.isEmpty() ? null : items.get(0);
  }

  public <T> T findFirstByHql(@NonNull String hql, HibernateParameter... parameters) {
    return findFirst(session().createQuery(hql), parameters);
  }

  public <T> T findFirstByNamedQuery(@NonNull String queryName, HibernateParameter... parameters) {
    return findFirst(session().getNamedQuery(queryName), parameters);
  }

  public <T> T findFirstBySQLString(@NonNull String sqlString, HibernateParameter... parameters) {
    return findFirst(session().createSQLQuery(sqlString), parameters);
  }

  public boolean exists(@NonNull Class<?> clazz) {
    return exists(session().createCriteria(clazz));
  }

  public boolean exists(@NonNull Criteria criteria) {
    return findFirst(criteria) != null;
  }

  public boolean exists(@NonNull DetachedCriteria dc) {
    return exists(dc.getExecutableCriteria(session()));
  }

  public boolean exists(@NonNull Query query, HibernateParameter... parameters) {
    return findFirst(query, parameters) != null;
  }

  public boolean existsByHql(@NonNull String hql, HibernateParameter... parameters) {
    Query query = session().createQuery(hql);
    return exists(query, parameters);
  }

  public boolean existsByNamedQuery(@NonNull String queryName, HibernateParameter... parameters) {
    Query query = session().getNamedQuery(queryName);
    return exists(query, parameters);
  }

  public boolean existsBySQLString(@NonNull String sqlString, HibernateParameter... parameters) {
    Query query = session().createSQLQuery(sqlString);
    return exists(query, parameters);
  }

  public long count(@NonNull Class<?> clazz) {
    return count(session().createCriteria(clazz));
  }

  public long count(@NonNull Criteria criteria) {
    return Convertx.asLong(criteria.setProjection(Projections.rowCount()).uniqueResult());
  }

  public long count(@NonNull DetachedCriteria dc) {
    return count(dc.getExecutableCriteria(session()));
  }

  public long count(@NonNull Query query, HibernateParameter... parameters) {
    Object result = CriteriaEx.setParameters(query, parameters)
                              .setResultTransformer(CriteriaSpecification.PROJECTION)
                              .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                              .uniqueResult();
    return Convertx.asLong(result);
  }

  public long countByHql(@NonNull String hql, HibernateParameter... parameters) {
    Query query = session().createQuery(hql);
    return count(query, parameters);
  }

  public long countByNamedQuery(@NonNull String queryName, HibernateParameter... parameters) {
    Query query = session().getNamedQuery(queryName);
    return count(query, parameters);
  }

  public long countBySQLString(@NonNull String sqlString, HibernateParameter... parameters) {
    Query query = session().createSQLQuery(sqlString);
    return count(query, parameters);
  }

  @SuppressWarnings("unchecked")
  public <T> T merge(@NonNull T entity) {
    return (T) session().merge(entity);
  }

  public void persist(@NonNull Object entity) {
    session().persist(entity);
  }

  @SuppressWarnings("unchecked")
  public <TId extends Serializable> TId save(@NonNull HibernateEntity<TId> entity) {
    return (TId) session().save(entity);
  }

  public void saveOrUpdate(@NonNull Object entity) {
    session().saveOrUpdate(entity);
  }

  public void update(@NonNull Object entity) {
    session().update(entity);
  }

  public void delete(@NonNull Object entity) {
    session().delete(entity);
  }

  public void deleteById(@NonNull Class<?> clazz, Serializable id) {
    session().delete(load(clazz, id));
  }

  public int deleteAll(Collection<?> entities) {
    Session session = session();
    for (Object entity : entities) {
      session.delete(entity);
    }
    return entities.size();
  }

  public int deleteAll(@NonNull Class<?> clazz) {
    log.debug("엔티티[{}]의 모든 데이터를 삭제합니다.", clazz);
    Session session = session();
    String hql = "delete from " + HibernateEx.getEntityName(sf, clazz);
    return executeUpdate(session.createQuery(hql));
  }

  public int deleteAll(@NonNull Class<?> clazz, @NonNull Criteria criteria) {
    List ids = criteria.setProjection(Projections.id()).list();
    for (Object id : ids) {
      deleteById(clazz, (Serializable) id);
    }
    return ids.size();
  }

  public int deleteAll(@NonNull Class<?> clazz, @NonNull DetachedCriteria dc) {
    return deleteAll(clazz, dc.getExecutableCriteria(session()));
  }

  public int executeUpdate(@NonNull Query query, HibernateParameter... parameters) {
    return CriteriaEx.setParameters(query, parameters).executeUpdate();
  }

  public int executeUpdateByHql(@NonNull String hql, HibernateParameter... parameter) {
    return executeUpdate(session().createQuery(hql), parameter);
  }

  public int executeUpdateByNamedQuery(@NonNull String queryName, HibernateParameter... parameter) {
    return executeUpdate(session().getNamedQuery(queryName), parameter);
  }

  public int executeUpdateBySQLString(@NonNull String sqlString, HibernateParameter... parameter) {
    return executeUpdate(session().createSQLQuery(sqlString), parameter);
  }

  private <P> Criteria buildProjectionCriteria(@NonNull Class<P> projectClass,
                                               @NonNull Criteria criteria,
                                               @NonNull Projection projections,
                                               boolean distinctResult) {
    if (distinctResult)
      criteria.setProjection(Projections.distinct(projections));
    else
      criteria.setProjection(projections);

    return criteria.setResultTransformer(Transformers.aliasToBean(projectClass));
  }

  @SuppressWarnings("unchecked")
  public <P> P reportOne(@NonNull Class<P> projectClass,
                         @NonNull ProjectionList projectionList,
                         @NonNull Criteria criteria) {

    return (P) this.buildProjectionCriteria(projectClass,
                                            criteria,
                                            projectionList,
                                            true)
                   .uniqueResult();
  }

  public <P> P reportOne(@NonNull Class<P> projectClass,
                         @NonNull ProjectionList projectionList,
                         @NonNull DetachedCriteria dc) {
    return reportOne(projectClass,
                     projectionList,
                     dc.getExecutableCriteria(session()));
  }

  @SuppressWarnings("unchecked")
  public <P> List<P> report(@NonNull Class<P> projectClass,
                            @NonNull ProjectionList projectionList,
                            @NonNull Criteria criteria) {
    return this.buildProjectionCriteria(projectClass,
                                        criteria,
                                        projectionList,
                                        false)
               .list();
  }


  public <P> List<P> report(@NonNull Class<P> projectClass,
                            @NonNull ProjectionList projectionList,
                            @NonNull DetachedCriteria dc) {
    return report(projectClass,
                  projectionList,
                  dc.getExecutableCriteria(session()));
  }

  @SuppressWarnings("unchecked")
  public <P> List<P> report(@NonNull Class<P> projectClass,
                            @NonNull ProjectionList projectionList,
                            @NonNull Criteria criteria,
                            int firstResult,
                            int maxResults) {
    Criteria crit = this.buildProjectionCriteria(projectClass,
                                                 criteria,
                                                 projectionList,
                                                 false);
    return CriteriaEx.setPaging(crit, firstResult, maxResults).list();
  }

  public <P> List<P> report(@NonNull Class<P> projectClass,
                            @NonNull ProjectionList projectionList,
                            @NonNull DetachedCriteria dc,
                            int firstResult,
                            int maxResults) {
    return report(projectClass,
                  projectionList,
                  dc.getExecutableCriteria(session()),
                  firstResult,
                  maxResults);
  }

  @SuppressWarnings("unchecked")
  public <P> Page<P> reportPage(@NonNull Class<P> projectClass,
                                @NonNull ProjectionList projectionList,
                                @NonNull Criteria criteria,
                                @NonNull Pageable page) {
    Criteria crit = buildProjectionCriteria(projectClass, criteria, projectionList, false);
    return getPage(crit, page);
  }

  public <P> Page<P> reportPage(@NonNull Class<P> projectClass,
                                @NonNull ProjectionList projectionList,
                                @NonNull DetachedCriteria dc,
                                @NonNull Pageable page) {
    return reportPage(projectClass,
                      projectionList,
                      dc.getExecutableCriteria(session()),
                      page);
  }
}
