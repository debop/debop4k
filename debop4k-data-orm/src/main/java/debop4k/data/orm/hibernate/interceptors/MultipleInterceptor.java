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

package debop4k.data.orm.hibernate.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Hibernate에서는 기본적으로 하나의 {@link Interceptor} 만을 등록할 수 있습니다.
 * MultipleInterceptor는 여러 개의 Interceptor를 대신 처리해 줍니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class MultipleInterceptor extends EmptyInterceptor {

  final FastList<Interceptor> interceptors = FastList.newList();

  public void add(Interceptor interceptor) {
    if (interceptor != null)
      interceptors.add(interceptor);
  }

  public void remove(Interceptor interceptor) {
    if (interceptor != null)
      interceptors.remove(interceptor);
  }

  public boolean isEmpty() {
    return interceptors.isEmpty();
  }

  public boolean nonEmpty() {
    return !isEmpty();
  }

  @Override
  public void onDelete(Object entity,
                       Serializable id,
                       Object[] state,
                       final String[] propertyNames,
                       Type[] types) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.onDelete(entity, id, state, propertyNames, types);
      }
    }
  }

  @Override
  public boolean onFlushDirty(Object entity,
                              Serializable id,
                              Object[] currentState,
                              Object[] previousState,
                              String[] propertyNames,
                              Type[] types) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
      }
    }
    return false;
  }

  @Override
  public boolean onLoad(Object entity,
                        Serializable id,
                        Object[] state,
                        String[] propertyNames,
                        Type[] types) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.onLoad(entity, id, state, propertyNames, types);
      }
    }
    return false;
  }

  @Override
  public boolean onSave(Object entity,
                        Serializable id,
                        Object[] state,
                        String[] propertyNames,
                        Type[] types) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.onSave(entity, id, state, propertyNames, types);
      }
    }
    return false;
  }

  @Override
  public void postFlush(Iterator entities) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.postFlush(entities);
      }
    }
  }

  @Override
  public void preFlush(Iterator entities) {
    if (nonEmpty()) {
      for (Interceptor interceptor : interceptors) {
        interceptor.preFlush(entities);
      }
    }
  }

  private static final long serialVersionUID = -8461427225818266204L;
}
