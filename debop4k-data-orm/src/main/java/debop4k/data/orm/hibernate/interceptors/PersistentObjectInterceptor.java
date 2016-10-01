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

import debop4k.data.orm.model.PersistentObject;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class PersistentObjectInterceptor extends EmptyInterceptor {

  @Override
  public boolean onLoad(Object entity,
                        Serializable id,
                        Object[] state,
                        String[] propertyNames,
                        Type[] types) {
    if (isPersistentObject(entity)) {
      ((PersistentObject) entity).onLoad();
    }
    return false;
  }

  @Override
  public boolean onSave(Object entity,
                        Serializable id,
                        Object[] state,
                        String[] propertyNames,
                        Type[] types) {
    if (isPersistentObject(entity)) {
      ((PersistentObject) entity).onSave();
    }
    return false;
  }

  private boolean isPersistentObject(Object entity) {
    return entity != null && entity instanceof PersistentObject;
  }

  private static final long serialVersionUID = -4753096442515795155L;
}
