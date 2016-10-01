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

package debop4k.data.orm.hibernate.listener;

import debop4k.data.orm.model.UpdatedTimestampEntity;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;

/**
 * {@link UpdatedTimestampEntity} 를 저장하기 전에 UpdatedTimestamp 값을 갱신해 주는 Listener 입니다.
 * <p>
 * Listener 는 {@link org.hibernate.Interceptor} 와는 달리 Listener 구조를 가져서 여러 개의 Listener 를 할용할 수 있습니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public class UpdatedTimestampListener
    implements PreInsertEventListener, PreUpdateEventListener {

  @Override
  public boolean onPreInsert(PreInsertEvent event) {
    if (isUpdatedTimestampEntity(event.getEntity())) {
      ((UpdatedTimestampEntity) event.getEntity()).updateUpdatedTimestamp();
    }
    return false;
  }

  @Override
  public boolean onPreUpdate(PreUpdateEvent event) {
    if (isUpdatedTimestampEntity(event.getEntity())) {
      ((UpdatedTimestampEntity) event.getEntity()).updateUpdatedTimestamp();
    }
    return false;
  }

  private boolean isUpdatedTimestampEntity(Object entity) {
    return entity != null && entity instanceof UpdatedTimestampEntity;
  }

  private static final long serialVersionUID = -4233989513124048079L;
}
