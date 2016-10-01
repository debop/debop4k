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

import debop4k.data.orm.model.UpdatedTimestampEntity;
import org.hibernate.EmptyInterceptor;

import java.util.Iterator;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class UpdatedTimestampInterceptor extends EmptyInterceptor {

  @Override
  public void preFlush(Iterator entities) {
    if (entities != null) {
      while (entities.hasNext()) {
        Object entity = entities.next();
        if (isUpdatedTimestampEntity(entity)) {
          ((UpdatedTimestampEntity) entity).updateUpdatedTimestamp();
        }
      }
    }
  }

  private boolean isUpdatedTimestampEntity(Object entity) {
    return entity != null && entity instanceof UpdatedTimestampEntity;
  }

  private static final long serialVersionUID = -685581755338949113L;
}
