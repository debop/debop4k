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
import javax.persistence.Convert;
import java.sql.Timestamp;

/**
 * Joda-Time의 {@link DateTime} 을 {@link Timestamp} 로 변환하여 DB에 저장해주는 Converter 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Convert
public class DateTimeAsTimestamp implements AttributeConverter<DateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(DateTime attribute) {
    return (attribute != null) ? new Timestamp(attribute.getMillis()) : null;
  }

  @Override
  public DateTime convertToEntityAttribute(Timestamp dbData) {
    return (dbData != null) ? KodaTimes.toDateTime(dbData) : null;
  }
}
