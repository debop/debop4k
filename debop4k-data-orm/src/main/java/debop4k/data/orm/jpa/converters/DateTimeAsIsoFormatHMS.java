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

import debop4k.core.kodatimes.KodaTimes;
import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;

/**
 * {@link DateTime} 정보를 ISO 8601 형태의 Second 까지만 변환하여 DB에 저장하는 Converter 입니다.
 * JPA 2.1 이상에서 지원하는 @Converter 를 위한 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
public class DateTimeAsIsoFormatHMS implements AttributeConverter<DateTime, String> {

  @Override
  public String convertToDatabaseColumn(DateTime attribute) {
    return KodaTimes.asIsoFormatDateHMSString(attribute);
  }

  @Override
  public DateTime convertToEntityAttribute(String dbData) {
    return KodaTimes.asIsoFormatDateHMS(dbData);
  }
}
