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
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

/**
 * 엔티티의 Identifier 수형이 {@link UUID} 인 엔티티의 추상 클래스.
 *
 * @author sunghyouk.bae@gmail.com
 */
@MappedSuperclass
@Access(AccessType.FIELD)
@DynamicInsert
@DynamicUpdate
@Getter
public abstract class UuidEntity extends AbstractHibernateEntity<UUID> {

  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Setter(AccessLevel.PROTECTED)
  private UUID id;

  private static final long serialVersionUID = 6433934850365748198L;
}
