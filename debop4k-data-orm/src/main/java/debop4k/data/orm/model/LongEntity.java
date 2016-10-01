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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 엔티티의 Identifier 수형이 Long 인 엔티티의 추상 클래스.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class LongEntity extends AbstractHibernateEntity<Long> {

  /**
   * 엔티티의 Identifier
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.PROTECTED)
  private Long id;

  /**
   * {@inheritDoc}
   */
  @Override
  public void resetIdentifier() {
    this.id = null;
    this.persisted = false;
  }

  private static final long serialVersionUID = 3646343595626884133L;
}
