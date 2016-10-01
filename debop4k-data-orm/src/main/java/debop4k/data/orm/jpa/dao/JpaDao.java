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
import debop4k.data.orm.model.HibernateEntity;
import kotlin.jvm.functions.Function1;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static debop4k.core.utils.Convertx.asInt;

/**
 * JPA 용 Data Access Object
 */
@Slf4j
@Getter
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
public class JpaDao {

  @Inject private EntityManagerFactory emf;
  @PersistenceContext private EntityManager em;

  @Value("${hibernate.jdbc.batch_size}")
  private String batchSize;

  public PersistenceProvider provider() {
    return PersistenceProvider.fromEntityManager(em);
  }

  public <T> JpaEntityInformation<T, ?> getEntityInfo(Class<T> entityClass) {
    return JpaEntityInformationSupport.getEntityInformation(entityClass, em);
  }

  public String getDeleteAllQueryString(Class<?> entityClass) {
    return JpaEx.getQueryString(JpaEx.QUERY_DELETE_ALL, getEntityInfo(entityClass).getEntityName());
  }

  public String getCountQueryString(Class<?> entityClass) {
    return JpaEx.getQueryString(JpaEx.QUERY_COUNT, getEntityInfo(entityClass).getEntityName());
  }

  public <T> T withNewEntityManager(@NonNull Function1<EntityManager, T> block) {
    return JpaEx.withNewEntityManager(emf, block);
  }

  public <T> T withTransaction(@NonNull Function1<EntityManager, T> block) {
    return block.invoke(em);
  }

  @Transactional(readOnly = true)
  public <T> T withReadOnly(@NonNull Function1<EntityManager, T> block) {
    return block.invoke(em);
  }

  public void flush() {
    em.flush();
  }

  public void clear() {
    em.clear();
  }

  /**
   * 엔티티를 저장합니다 (Save or Update)
   *
   * @param entity 저장할 엔티티
   * @param <T>    엔티티 수형
   * @return 저장된 엔티티
   */
  public <T extends HibernateEntity> T save(@NonNull T entity) {
    return JpaEx.save(em, entity);
  }

  /**
   * 엔티티 컬렉션에 대해 벌크 저장을 수행합니다.
   *
   * @param entities 저장할 엔티티 컬렉션
   * @param <T>      엔티티의 수형
   * @return 저장된 엔티티 컬렉션
   */
  public <T extends HibernateEntity> MutableList<T> saveAll(Collection<T> entities) {
    MutableList<T> savedEntities = FastList.newList(entities.size());

    int i = 0;
    int batch = Math.max(asInt(batchSize, 100), 100);
    for (T entity : entities) {
      savedEntities.add(save(entity));
      i++;
      if (i % batch == 0) {
        em.flush();
        em.clear();
      }
    }
    if (i % batch > 0) {
      em.flush();
      em.clear();
    }
    return savedEntities;
  }

  /**
   * 엔티티 컬렉션에 대해 벌크 저장을 수행합니다.
   *
   * @param entities 저장할 엔티티 컬렉션
   * @param <T>      엔티티의 수형
   */
  public <T extends HibernateEntity> void insertAll(Collection<T> entities) {
    int i = 0;
    int batch = Math.max(asInt(batchSize, 100), 100);
    for (T entity : entities) {
      save(entity);
      i++;
      if (i % batch == 0) {
        em.flush();
        em.clear();
      }
    }
  }

  /**
   * 지정한 엔티티를 삭제합니다.
   *
   * @param entity 삭제할 엔티티
   * @param <T>    엔티티의 수형
   */
  public <T extends HibernateEntity> void delete(@NonNull T entity) {
    JpaEx.delete(em, entity);
  }

  /**
   * 지정한 엔티티 수형 중 지정한 identifier 를 가진 엔티티를 삭제합니다.
   *
   * @param entityClass 엔티티의 수형
   * @param id          엔티티의 Identifier 의 값
   * @param <T>         엔티티의 수형
   */
  public <T extends HibernateEntity> void delete(@NonNull Class<T> entityClass, @NonNull Object id) {

    T entity = em.find(entityClass, id);

    if (entity == null) {
      String msg = "No " + entityClass + " entity with id " + id + " exists.";
      throw new EmptyResultDataAccessException(msg, 1);
    }

    delete(entity);
  }

  public <T extends HibernateEntity> void deleteAll(@NonNull Class<T> entityClass, Iterable<? extends T> entities) {
    JpaEx.deleteAll(em, entityClass, entities);
  }

  @Transactional(readOnly = true)
  public boolean exists(Class<?> entityClass, Serializable id) {
    return JpaEx.exists(em, entityClass, id);
  }

  public <T> T findOne(Class<T> entityClass, Object id) {
    return em.find(entityClass, id);
  }

  public <T> List<T> findAll(Class<T> entityClass) {
    return JpaEx.findAll(em, entityClass);
  }

  public <T> List<T> findAll(Class<T> entityClass, Iterable<?> ids) {
    return JpaEx.findAll(em, entityClass, ids);
  }

  public void detach(Object entity) {
    em.detach(entity);
  }

  public <T> T merge(T entity) {
    return em.merge(entity);
  }
}
