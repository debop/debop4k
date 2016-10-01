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

package debop4k.data.orm.jpa.converters;


import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.joda.time.DateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import java.util.Locale;

/**
 * JPA 2.1 의 @Converter 에 대한 테스트 코드입니다.
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@NoArgsConstructor
public class ConverterEntity extends IntEntity {

  public ConverterEntity(String name) {
    this.name = name;
  }

  private String name;

  @Convert(converter = LocaleAsString.class)
  private Locale locale;

  @Convert(converter = DateTimeAsIsoFormatString.class)
  private DateTime dateStr;

  @Convert(converter = DateTimeAsTimestamp.class)
  private DateTime timestamp;

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = -8466903693952968967L;
}
