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

package debop4k.data.orm.mapping.compositeid.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.AbstractHibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * 가장 일반적인 Composite Id 정의 방식입니다. @EmbeddableCarIdentifier 를 identifier 로 가지도록 정의합니다.
 */
@Entity(name = "compositeId_Car")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Getter
@Setter
public class EmbeddableIdCar extends AbstractHibernateEntity<EmbeddableCarIdentifier> {

  public EmbeddableIdCar(@NonNull EmbeddableCarIdentifier identifier) {
    this.id = identifier;
  }

  @EmbeddedId
  @Column(name = "carId")
  EmbeddableCarIdentifier id;

  private String serialNo;

  @Override
  public int hashCode() {
    return Hashx.compute(serialNo);
  }

  private static final long serialVersionUID = -6382224421962338694L;
}
