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

package debop4k.data.orm.jpa.stateless;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.internal.SessionImpl;
import org.hibernate.internal.StatelessSessionImpl;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.ReflectionUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;

/**
 * Hibernate 의 {@link StatelessSession}을 JPA에서 사용할 수 있도록, {@link StatelessSession}을 생성해주는 Factory Bean입니다.
 * 참고 : https://gist.github.com/jelies/5181262
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class StatelessSessionFactoryBean implements FactoryBean<StatelessSession> {

  private final EntityManagerFactory emf;
  private final SessionFactory sf;

  @Inject
  public StatelessSessionFactoryBean(SessionFactory sf) {
    this.emf = sf;
    this.sf = sf;
  }

  @Override
  public StatelessSession getObject() throws Exception {
    StatelessSessionInterceptor interceptor = new StatelessSessionInterceptor(emf, sf);
    return ProxyFactory.getProxy(StatelessSession.class, interceptor);
  }

  @Override
  public Class<?> getObjectType() {
    return StatelessSession.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * StatelessSession interceptor
   */
  class StatelessSessionInterceptor implements org.aopalliance.intercept.MethodInterceptor {

    private final EntityManagerFactory emf;
    private final SessionFactory sf;

    public StatelessSessionInterceptor(EntityManagerFactory emf, SessionFactory sf) {
      this.emf = emf;
      this.sf = sf;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
      StatelessSession stateless = getCurrentStateless();
      return ReflectionUtils.invokeMethod(invocation.getMethod(), stateless, invocation.getArguments());
    }

    private StatelessSession getCurrentStateless() {
      if (!TransactionSynchronizationManager.isActualTransactionActive()) {
        throw new IllegalStateException("현 스레드에서 활성화된 트랜잭션이 없습니다. StatelessSession 은 Transaction 하에서만 작동됩니다.");
      }

      StatelessSession stateless = (StatelessSession) TransactionSynchronizationManager.getResource(sf);
      if (stateless == null) {
        log.debug("현 스레드에 새로운 Stateless Session 을 생성합니다.");
        stateless = newStatelessSession();
        bindWithTransaction(stateless);
      }
      return stateless;
    }

    private StatelessSession newStatelessSession() {
      Connection conn = obtainPhysicalConnection();
      return sf.openStatelessSession(conn);
    }

    public Connection obtainPhysicalConnection() {
      log.debug("Proxy 가 아닌 Physical Connection을 얻습니다...");
      EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);

      SessionImpl session = (SessionImpl) em.unwrap(Session.class);

      return session.getJdbcCoordinator()
                    .getLogicalConnection()
                    .getPhysicalConnection();
    }

    public void bindWithTransaction(StatelessSession stateless) {
      log.trace("bind with transaction");
      TransactionSynchronizationManager.registerSynchronization(new StatelessSessionSynchronization(sf, stateless));

      TransactionSynchronizationManager.bindResource(sf, stateless);
    }
  }

  @Getter
  static class StatelessSessionSynchronization extends TransactionSynchronizationAdapter {

    private final SessionFactory sf;
    private final StatelessSession stateless;

    public StatelessSessionSynchronization(SessionFactory sf, StatelessSession stateless) {
      this.sf = sf;
      this.stateless = stateless;
    }

    @Override
    public int getOrder() {
      return EntityManagerFactoryUtils.ENTITY_MANAGER_SYNCHRONIZATION_ORDER - 100;
    }

    @Override
    public void beforeCommit(boolean readOnly) {
      if (!readOnly) {
        ((StatelessSessionImpl) stateless).flushBeforeTransactionCompletion();
      }
    }

    @Override
    public void beforeCompletion() {
      TransactionSynchronizationManager.unbindResource(sf);
      stateless.close();
    }
  }
}
