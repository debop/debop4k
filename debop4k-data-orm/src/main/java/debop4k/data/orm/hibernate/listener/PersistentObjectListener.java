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

import debop4k.data.orm.model.PersistentObject;
import org.hibernate.event.spi.*;
import org.hibernate.persister.entity.EntityPersister;

/**
 * {@link PersistentObject}의 저장 시, 로드 시에 엔티티의 저장 상태 값을 갱신합니다.
 * <p>
 * Listener 는 {@link org.hibernate.Interceptor} 와는 달리 Listener 구조를 가져서 여러 개의 Listener 를 할용할 수 있습니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public class PersistentObjectListener
    implements PostLoadEventListener, PostInsertEventListener, PostUpdateEventListener {

  @Override
  public void onPostInsert(PostInsertEvent event) {
    if (isPersistentObject(event.getEntity())) {
      ((PersistentObject) event.getEntity()).onSave();
    }
  }

  @Override
  public void onPostUpdate(PostUpdateEvent event) {
    if (isPersistentObject(event.getEntity())) {
      ((PersistentObject) event.getEntity()).onSave();
    }
  }

  @Override
  public boolean requiresPostCommitHanding(EntityPersister persister) {
    return false;
  }

  @Override
  public void onPostLoad(PostLoadEvent event) {
    if (isPersistentObject(event.getEntity())) {
      ((PersistentObject) event.getEntity()).onLoad();
    }
  }

  private boolean isPersistentObject(Object entity) {
    return entity != null && entity instanceof PersistentObject;
  }

  private static final long serialVersionUID = 8875181171497334755L;
}
