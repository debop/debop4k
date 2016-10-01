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

package debop4k.data.orm.hibernate;

import kotlin.jvm.functions.Function1;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.internal.StatelessSessionImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.SQLException;

import static debop4k.data.orm.hibernate.HibernateEx.getSessionFactory;

/**
 * {@link org.hibernate.StatelessSession} 을 이용한 작업을 도와주는 메소드를 제공합니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class StatelessEx {

  private StatelessEx() {}

  public static StatelessSessionImpl of(@NonNull SessionFactory factory) {
    return (StatelessSessionImpl) factory.openStatelessSession();
  }

  public static StatelessSessionImpl of(@NonNull Session session) {
    return of(session.getSessionFactory());
  }

  public static StatelessSessionImpl of(@NonNull EntityManager em) {
    return of(getSessionFactory(em));
  }

  public static <T> T withTransaction(@NonNull SessionFactory factory,
                                      @NonNull final Function1<StatelessSessionImpl, T> func) {
    StatelessSessionImpl stateless = of(factory);
    try {
      Transaction tx = stateless.beginTransaction();
      try {
        T result = func.invoke(stateless);
        tx.commit();
        return result;
      } catch (HibernateException e) {
        log.error("Hibernate StatelessSession 작업 중에 예외가 발생했습니다.", e);
        tx.rollback();
        return null;
      }
    } finally {
      if (stateless != null && !stateless.isClosed())
        stateless.close();
    }
  }

  @Transactional
  public static <T> T withTransaction(@NonNull EntityManager em, @NonNull Function1<StatelessSessionImpl, T> func) {
    return withTransaction(getSessionFactory(em), func);
  }

  /**
   * DB에 대해 읽기 전용 작업을 수행합니다.
   *
   * @param factory hibernate session factory
   * @param func    일기 작업
   * @param <T>     읽을 엔티티의 수형
   * @return 읽은 엔티티
   * @deprecated {@literal @}Transactional(readOnly=true) 를 사용하세요.
   */
  @Deprecated
  @Transactional(readOnly = true)
  public static <T> T withReadOnly(@NonNull SessionFactory factory, @NonNull Function1<StatelessSessionImpl, T> func) {
    StatelessSessionImpl stateless = of(factory);
    try {
      Connection conn = stateless.connection();
      conn.setReadOnly(true);
      conn.setAutoCommit(false);
      Transaction tx = stateless.beginTransaction();
      try {
        T result = func.invoke(stateless);
        tx.commit();
        return result;
      } catch (Exception e) {
        log.error("Hibernate StatelessSession 작업 중에 예외가 발생했습니다.", e);
        tx.rollback();
        return null;
      }
    } catch (SQLException e) {
      log.error("Connection 속성 설정에서 예외가 발생했습니다.", e);
      throw new RuntimeException(e);
    } finally {
      if (stateless != null && !stateless.isClosed())
        stateless.close();
    }
  }

  /**
   * DB에 대해 읽기 전용 작업을 수행합니다.
   *
   * @param em   JPA entity manager.
   * @param func 일기 작업
   * @param <T>  읽을 엔티티의 수형
   * @return 읽은 엔티티
   * @deprecated {@literal @}Transactional(readOnly=true) 를 사용하세요.
   */
  @Deprecated
  @Transactional(readOnly = true)
  public static <T> T withReadOnly(@NonNull EntityManager em, @NonNull Function1<StatelessSessionImpl, T> func) {
    return withReadOnly(getSessionFactory(em), func);
  }
}
