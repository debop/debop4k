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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Identifier의 수형이 문자열인 엔티티의 추상 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 26.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class StringEntity extends AbstractHibernateEntity<String> {

  /**
   * 생성자
   *
   * @param id 엔티티의 identifier
   */
  public StringEntity(String id) {
    this.id = id;
  }

  /**
   * 엔티티의 identifier
   */
  @Id
  private String id;

  private static final long serialVersionUID = -4920552832816342844L;
}
