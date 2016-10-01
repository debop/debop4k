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

package debop4k.data.orm.model;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import lombok.Getter;

import javax.persistence.Transient;

/**
 * Hibernate, JPA 등 ORM에서 영구 저장된 객체를 구분하기 위한 속성을 가진 추상 클래스
 *
 * @author sunghyouk.bae@gmail.com
 */
@Getter
public abstract class AbstractPersistentObject extends AbstractValueObject implements PersistentObject {

  /**
   * 영구 저장 여부
   */
  @Transient
  protected boolean persisted = false;

  /**
   * 객체를 영구저장소에 저장할 때 호출되는 메소드
   */
  @Override
  public void onSave() {
    this.persisted = true;
  }

  /**
   * 객체를 영구저장소에서 읽어올 때 호출되는 메소드
   */
  @Override
  public void onLoad() {
    this.persisted = true;
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("persisted", persisted);
  }

  private static final long serialVersionUID = 6720301225815063949L;
}
