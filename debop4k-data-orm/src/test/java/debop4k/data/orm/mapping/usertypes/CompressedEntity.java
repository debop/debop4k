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

package debop4k.data.orm.mapping.usertypes;

import debop4k.data.orm.hibernate.usertypes.UserTypes;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class CompressedEntity extends IntEntity {

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "CompressedString", length = 8000)
  @Type(type = UserTypes.SNAPPY_STRING_USER_TYPE)
  private String stringData;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "CompressedBytes", columnDefinition = "BLOB")
  @Type(type = UserTypes.SNAPPY_BYTE_ARRAY_USER_TYPE)
  private byte[] binaryData;

  private static final long serialVersionUID = -5598671817683892124L;
}
