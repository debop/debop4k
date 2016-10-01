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

package debop4k.data.orm.mapping.simple;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(indexes = {
    @Index(name = "ix_lifecycle_entity_name", columnList = "name")
})
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class LifecycleEntity extends IntEntity {

  public LifecycleEntity(@NonNull String name) {
    this.name = name;
  }

  @Column(nullable = false, length = 128)
  private String name;

  // HINT: Java 8 과 Spring Data JPA 에서는 LocalDateTime 과 @CreatedDate 를 사용하세요.
  // NOTE: Hibernate @Generated 는 JPA 에서는 작동하지 않는다. JPA에서는 @PrePersist @PreUpdate 를 사용해야 합니다.
  @Column(name = "createAt", updatable = false)
  @Temporal(TemporalType.DATE)
  private Date createAt;

  @Column(name = "updatedAt", insertable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedAt;

  @PrePersist
  public void onPrePersist() {
    if (createAt == null)
      createAt = new Date();
  }

  @PreUpdate
  public void onPreUpdate() {
    updatedAt = new Date();
  }

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 1094327803295288072L;
}
