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

import java.io.Serializable;

/**
 * Hibernate, JPA 용 엔티티의 기본 인터페이스
 *
 * @param <TId> 엔티티의 고유성을 나타내는 Identifier의 수형
 * @author sunghyouk.bae@gmail.com
 */
public interface HibernateEntity<TId extends Serializable> extends PersistentObject {

  /**
   * Entity 의 identifier 값
   */
  TId getId();

  /**
   * 엔티티의 Identifier 를 초기화하고, 엔티티를 transient object 로 만든다.
   * 엔티티를 복사하여 사용할 시에 유용하다.
   */
  void resetIdentifier();

}
