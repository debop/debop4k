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

import debop4k.data.orm.hibernate.listener.PersistentObjectListener;
import debop4k.data.orm.hibernate.listener.UpdatedTimestampListener;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Properties;

import static org.hibernate.cfg.AvailableSettings.*;

/**
 * Hibernate 사용을 위한 Helper class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class HibernateEx {

  private HibernateEx() {}

  /**
   * Hibernate {@link Configuration} 을 이용하여 {@link SessionFactory}를 빌드합니다.
   *
   * @param cfg Hibernate용 환경설정
   * @return {@link SessionFactory} 인스턴스
   */
  public static SessionFactory buildSessionFactory(@NonNull Configuration cfg) {
    log.info("SessionFactory 를 빌드합니다.");

    ServiceRegistry registry = new StandardServiceRegistryBuilder()
        .applySettings(cfg.getProperties())
        .build();

    SessionFactory factory = cfg.buildSessionFactory(registry);

    log.info("SessionFactory 를 빌드했습니다.");
    return factory;
  }

  /**
   * Hibernate SessionFactory 에 event listener 를 등록합니다.
   *
   * @param factory    SessionFactory instance
   * @param listener   Listener instance
   * @param eventTypes 리스닝할 이벤트 종류
   */
  @SuppressWarnings("unchecked")
  public static <T> void registerEventListener(@NonNull SessionFactory factory,
                                               T listener,
                                               Collection<EventType<?>> eventTypes) {
    EventListenerRegistry registry = ((SessionFactoryImpl) factory)
        .getServiceRegistry()
        .getService(EventListenerRegistry.class);

    for (EventType eventType : eventTypes) {
      registry.getEventListenerGroup(eventType).appendListener(listener);
    }
  }

  /**
   * SessionFactory의 event listener 에 {@link UpdatedTimestampListener} 를 추가합니다.
   *
   * @param factory SessionFactory instance
   */
  public static void registUpdateTimestampEventListener(@NonNull SessionFactory factory) {
    UpdatedTimestampListener listener = new UpdatedTimestampListener();

    registerEventListener(factory,
                          listener,
                          FastList.<EventType<?>>newListWith(EventType.PRE_INSERT,
                                                             EventType.PRE_UPDATE));
  }

  /**
   * SessionFactory의 event listener 에 {@link UpdatedTimestampListener} 를 추가합니다.
   *
   * @param factory SessionFactory instance
   */
  public static void registPersistentObjectEventListener(@NonNull SessionFactory factory) {
    PersistentObjectListener listener = new PersistentObjectListener();

    registerEventListener(factory,
                          listener,
                          FastList.<EventType<?>>newListWith(EventType.POST_LOAD,
                                                             EventType.POST_INSERT));
  }

  /**
   * 지정된 entity manager 가 사용하는 hibernate session factory 를 구합니다.
   *
   * @param em entity manager
   * @return {@link SessionFactory} 인스턴스
   */
  public static SessionFactory getSessionFactory(@NonNull EntityManager em) {
    return em.unwrap(Session.class).getSessionFactory();
  }

  /**
   * 지정된 entity manager 가 사용하는 hibernate session을 반환합니다.
   *
   * @param em entity manager
   * @return {@link Session} 인스턴스
   */
  public static Session getCurrentSession(@NonNull EntityManager em) {
    return getSessionFactory(em).getCurrentSession();
  }

  /**
   * 지정한 엔티티 수형의 엔티티명을 구합니다. (엔티티명과 클래스명은 다를 수 있습니다.)
   *
   * @param factory     hibernate session factory
   * @param entityClass entity class
   * @return 엔티티 명
   */
  public static String getEntityName(@NonNull SessionFactory factory, @NonNull Class<?> entityClass) {
    ClassMetadata meta = factory.getClassMetadata(entityClass);
    return meta.getEntityName();
  }

  public static Properties newHibernateProperties() {
    Properties props = new Properties();

    // create | create-drop | spawn | spawn-drop | update | validate | none
    props.setProperty(HBM2DDL_AUTO, "none");

    props.setProperty(POOL_SIZE, "30");
    props.setProperty(SHOW_SQL, "true");
    props.setProperty(FORMAT_SQL, "true");

    props.setProperty(AUTOCOMMIT, "true");

    // 참고 : http://stackoverflow.com/questions/15573370/my-spring-application-leaks-database-connections-whereas-i-use-the-default-roo-c
    // 기본값을 사용하면 connection이 release 되지 않을 수 있다.
    props.setProperty(RELEASE_CONNECTIONS, "after_transaction");

    return props;
  }
}
