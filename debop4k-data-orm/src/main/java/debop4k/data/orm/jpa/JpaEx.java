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

package debop4k.data.orm.jpa;

import debop4k.core.io.serializers.Serializers;
import debop4k.data.orm.hibernate.StatelessEx;
import debop4k.data.orm.model.HibernateEntity;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;
import org.hibernate.internal.StatelessSessionImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.sql.Connection;
import java.util.List;

/**
 * JPA (Java Persistence API) 를 위한 Helper method 를 제공합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class JpaEx {

  private JpaEx() {}

  /**
   * 특정 엔티티를 삭제하기 위한 SQL 구문. x 는 where 절을 나타낸다.
   */
  public static final String QUERY_DELETE_ALL = "delete from %s x";

  /**
   * 특정 엔티티에 대한 ROW COUNT 를 구한다. x 는 where 절을 나타낸다.
   */
  public static final String QUERY_COUNT = "select n(*) from %s x";

  /**
   * 레코드 수를 계산할 때의 방식 ( n(*) )
   */
  public static final String QUERY_COUNT_HOLDER = "*";

  /**
   * 지정한 엔티티명을 이용하여 쿼리 문을 생성합니다.
   *
   * @param template    쿼리 템플릿
   * @param entityClass 엔티티 수형
   * @param em          entity manager
   * @return 쿼리문
   */
  public static String getQueryString(String template, Class<?> entityClass, EntityManager em) {
    return getQueryString(template, getEntityInfo(em, entityClass).getEntityName());
  }

  /**
   * 지정한 엔티티명을 이용하여 쿼리 문을 생성합니다.
   *
   * @param template   쿼리 템플릿
   * @param entityName 엔티티 명
   * @return 쿼리문
   */
  public static String getQueryString(String template, String entityName) {
    return String.format(template, entityName);
  }

  /**
   * 엔티티 정보를 구합니다
   *
   * @param em          entity manager
   * @param entityClass 엔티티 수형
   * @param <T>         엔티티 수형
   * @return 엔티티 정보를 나타내는 {@link JpaEntityInformation} 인스턴스
   */
  public static <T> JpaEntityInformation<T, ?> getEntityInfo(EntityManager em, Class<T> entityClass) {
    return JpaEntityInformationSupport.getEntityInformation(entityClass, em);
  }

  public static <T> TypedQuery<T> getQuery(EntityManager em, Class<T> resultClass, Specification<T> spec, Pageable page) {
    Sort sort = (page != null) ? page.getSort() : null;
    TypedQuery<T> query = getQuery(em, resultClass, spec, sort);
    if (page != null)
      setPaging(query, page);

    return query;
  }

  public static <T> TypedQuery<T> getQuery(EntityManager em, Class<T> resultClass, Specification<T> spec, Sort sort) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<T> query = cb.createQuery(resultClass);

    Root<T> root = applySpecificationToCriteria(em, resultClass, spec, query);
    query.select(root);

    if (sort != null) {
      query.orderBy(QueryUtils.toOrders(sort, root, cb));
    }
    return em.createQuery(query);
  }

  public static <T> Root<T> applySpecificationToCriteria(@NonNull EntityManager em,
                                                         Class<T> resultClass,
                                                         Specification<T> spec,
                                                         @NonNull CriteriaQuery<?> query) {
    Root<T> root = query.from(resultClass);
    if (spec != null) {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      Predicate predicate = spec.toPredicate(root, query, cb);

      if (predicate != null) {
        query.where(predicate);
      }
    }
    return root;
  }

  /**
   * 결과 셋의 offset 위치를 지정합니다.
   *
   * @param query       쿼리
   * @param firstResult 결과 셋의 첫번째 레코드의 인덱스 (0부터 시작)
   * @return 쿼리
   */
  public static TypedQuery<?> setFirstResult(TypedQuery<?> query, int firstResult) {
    if (firstResult > 0)
      query.setFirstResult(firstResult);
    return query;
  }

  /**
   * 결과 셋의 최대 레코드 수를 지정합니다.
   *
   * @param query      쿼리
   * @param maxResults 레코드의 최대 크기
   * @return 쿼리
   */
  public static TypedQuery<?> setMaxResults(TypedQuery<?> query, int maxResults) {
    if (maxResults > 0)
      query.setMaxResults(maxResults);
    return query;
  }

  /**
   * 쿼리에 페이징 처리를 지정합니다.
   *
   * @param query       대상 쿼리
   * @param firstResult 첫번째 레코드의 인덱스 (0부터 시작)
   * @param maxResults  레코드의 최대 크기
   * @return 쿼리
   */
  public static TypedQuery<?> setPaging(TypedQuery<?> query, int firstResult, int maxResults) {
    setFirstResult(query, firstResult);
    setMaxResults(query, maxResults);
    return query;
  }

  /**
   * 쿼리에 페이징 처리를 지정합니다. (단 정렬은 수행하지 않습니다.)
   *
   * @param query    대상 쿼리
   * @param pageable 페이징 처리 정보
   * @return 쿼리
   */
  public static TypedQuery<?> setPaging(TypedQuery<?> query, Pageable pageable) {
    if (pageable == null)
      return query;

    return setPaging(query, pageable.getOffset(), pageable.getPageSize());
  }

  /**
   * 엔티티가 entity manager 에 로드된 엔티티인지 여부
   *
   * @param em     entity manager
   * @param entity 검사할 엔티티
   * @return 엔티티가 entity manager 에 로드된 엔티티인지 여부
   */
  public static boolean isLoaded(EntityManager em, Object entity) {
    return (entity != null) &&
           em.getEntityManagerFactory()
             .getPersistenceUnitUtil()
             .isLoaded(entity);
  }

  /**
   * transient object 이면 save 하고, detached 된 entity 면 merge 해서 update 한다.
   *
   * @param em     entity manager
   * @param entity 저장할 엔티티
   */
  public static <T extends HibernateEntity> T save(EntityManager em, T entity) {
    if (entity.isPersisted() && !em.contains(entity)) {
      return em.merge(entity);
    } else {
      em.persist(entity);
      return entity;
    }
  }

  /**
   * persistent object 이면 삭제합니다.
   *
   * @param em     entity manager
   * @param entity 삭제할 엔티티
   */
  public static <T extends HibernateEntity> void delete(@NonNull EntityManager em, @NonNull T entity) {
    if (entity.isPersisted()) {
      if (!em.contains(entity)) {
        em.remove(em.merge(entity));
      } else {
        em.remove(entity);
      }
    }
  }

  public static <T extends HibernateEntity> int deleteAll(@NonNull EntityManager em, @NonNull Class<T> clazz) {
    log.debug("delete all entities. entity={}", clazz.getName());
    String deleteQuery = getQueryString(QUERY_DELETE_ALL, clazz, em);
    Query query = em.createQuery(deleteQuery);
    return query.executeUpdate();
  }

  /**
   * 지정된 엔티티 수형의 모든 엔티티들을 삭제합니다.
   *
   * @param em       entity manager
   * @param clazz    엔티티 수형
   * @param entities 삭제할 엔티티들
   * @param <T>      엔티티 수형
   */
  public static <T extends HibernateEntity> int deleteAll(@NonNull EntityManager em,
                                                          @NonNull Class<T> clazz,
                                                          @NonNull Iterable<? extends T> entities) {
    String deleteQuery = getQueryString(QUERY_DELETE_ALL, clazz, em);
    Query query = QueryUtils.applyAndBind(deleteQuery, entities, em);
    return query.executeUpdate();
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T> boolean exists(EntityManager em, Class<T> entityClass, Serializable id) {
    log.debug("엔티티 존재 여부 확인 중... entityClass={}, id={}", entityClass, id);

    JpaEntityInformation entityInfo = getEntityInfo(em, entityClass);

    String entityName = entityInfo.getEntityName();
    Iterable<String> idNames = entityInfo.getIdAttributeNames();
    String existsQuery = QueryUtils.getExistsQueryString(entityName, JpaEx.QUERY_COUNT_HOLDER, idNames);

    TypedQuery<Long> query = em.createQuery(existsQuery, Long.class);
    if (entityInfo.hasCompositeId()) {
      for (String name : idNames) {
        query.setParameter(name, entityInfo.getCompositeIdAttributeValue(id, name));
      }
    } else {
      query.setParameter(idNames.iterator().next(), id);
    }
    return query.getSingleResult() == 1L;
  }

  public static <T> T findOne(EntityManager em, Class<T> entityClass, Object id) {
    return em.find(entityClass, id);
  }


  public static <T> List<T> findAll(EntityManager em, Class<T> entityClass) {
    return JpaEx.getQuery(em, entityClass, null, (Sort) null).getResultList();
  }

  public static <T> List<T> findAll(final EntityManager em, final Class<T> entityClass, Iterable<?> ids) {
    if (ids == null || !ids.iterator().hasNext())
      return FastList.newList();

    Specification<T> spec = new Specification<T>() {
      @Override
      public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Path path = root.get(getEntityInfo(em, entityClass).getIdAttribute());
        return path.in(cb.parameter(Iterable.class), "ids");
      }
    };

    return getQuery(em, entityClass, spec, (Sort) null)
        .setParameter("ids", ids)
        .getResultList();
  }

  /**
   * entity manager 가 사용하는 실제 물리적 {@link Connection}을 구합니다.
   *
   * @param em entity manager
   * @return {@link Connection} 인스턴스
   */
  public static Connection currentConnection(@NonNull EntityManager em) {
    SessionImpl session = (SessionImpl) em.unwrap(Session.class);

    return session.getJdbcCoordinator()
                  .getLogicalConnection()
                  .getPhysicalConnection();
  }

  @Transactional
  public static <T> T withTransaction(@NonNull EntityManager em, Function0<T> block) {
    return block.invoke();
  }

  /**
   * 새로운 `EntityManager`를 생성하여, DB 작업을 수행하고, `EntityManager`는 소멸시킵니다.
   *
   * @param emf  `EntityManagerFactory` 인스턴스
   * @param func 실행할 코드 블럭
   * @return 실행 결과
   */
  public static <T> T withNewEntityManager(@NonNull EntityManagerFactory emf,
                                           Function1<EntityManager, T> func) {
    log.debug("새로운 EntityManager를 생성하여, DB 작업을 수행합니다...");
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        T result = func.invoke(em);
        em.getTransaction().commit();
        log.debug("새로운 entitymanager 를 생성하여, DB 작업을 완료했습니다.");
        return result;
      } catch (Exception e) {
        em.getTransaction().rollback();
        log.error("새로운 EntityManager 로 DB 작업을 하는 중에 예외가 발생했습니다.", e);
        throw new RuntimeException(e);
      }
    } finally {
      em.close();
    }
  }

  /**
   * Hibernate Stateless Session 을 이용하여 읽기 전용 작업을 수행하도록 합니다.
   *
   * @param em    entity manager
   * @param block stateless session 을 이용하여 읽기 전용 작업할 코드 블럭
   * @param <T>   반환할 변수의 수형
   * @return 작업 결과
   */
  public static <T> T withStatelessReadOnly(@NonNull EntityManager em, Function1<StatelessSessionImpl, T> block) {
    return StatelessEx.withReadOnly(em, block);
  }

  /**
   * Hibernate Stateless Session 을 이용하여 작업을 수행하도록 합니다.
   *
   * @param em    entity manager
   * @param block stateless session 을 이용하여 작업할 코드 블럭
   * @param <T>   반환할 변수의 수형
   * @return 작업 결과
   */
  public static <T> T withStateless(@NonNull EntityManager em, Function1<StatelessSessionImpl, T> block) {
    return StatelessEx.withTransaction(em, block);
  }

  /**
   * 엔티티를 복사합니다.
   *
   * @param src 원본 엔티티
   * @param <T> 엔티티 수형
   * @return 복사한 엔티티
   */
  public static <T extends HibernateEntity<?>> T copy(T src) {
    return Serializers.FST.copy(src);
  }

  /**
   * 엔티티를 복사한 후, 복사한 엔티티를 transient object 로 만듭니다. (identifier 를 null 로 설정합니다.)
   *
   * @param src 원본 엔티티
   * @param <T> 엔티티 수형
   * @return 복사한 엔티티
   */
  public static <T extends HibernateEntity<?>> T copyAndResetId(T src) {
    T dest = Serializers.FST.copy(src);
    dest.resetIdentifier();
    return dest;
  }
}
