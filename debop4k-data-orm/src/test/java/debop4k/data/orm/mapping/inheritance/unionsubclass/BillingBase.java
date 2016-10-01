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

package debop4k.data.orm.mapping.inheritance.unionsubclass;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.UuidEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * 상속관계의 엔테티들을 독립적인 테이블로 만든다.
 * 주의할 점은 Identifier 는 상속된 모든 class에 대해 고유한 값을 가져야 한다. (테이블 범위의 identity는 사용하면 안된다)
 * <p>
 * 여러 테이블에 걸쳐 Identity를 유지하기 위해 고유의 값을 가지도록 UUID를 사용한다.
 */
@Entity(name = "UnionSubclass_BuillingBase")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public abstract class BillingBase extends UuidEntity {

  public BillingBase(@NonNull String owner) {
    this.owner = owner;
  }

  @Column(nullable = false)
  private String owner;

  @Override
  public int hashCode() {
    return Hashx.compute(owner);
  }

  private static final long serialVersionUID = 1479059788589598327L;
}
