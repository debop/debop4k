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

import debop4k.core.ValueObject;

import javax.persistence.Transient;

/**
 * Hibernate, JPA 용 데이터를 표현하는 객체의 기본 클래스 (Entity나 Component의 조상입니다)
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface PersistentObject extends ValueObject {

  /**
   * 엔티티가 영구 저장되었는지 여부
   *
   * @return true 면 영구 저장, false면 transient object 입니다.
   */
  @Transient
  boolean isPersisted();

  /**
   * 엔티티 저장 시 호출되는 메소드
   */
  void onSave();

  /**
   * 엔티티를 영구 저장소에서 로드 시에 호출되는 메소드
   */
  void onLoad();

}
