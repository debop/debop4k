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

import debop4k.core.utils.Stringx;
import org.apache.commons.lang3.LocaleUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

/**
 * {@link Locale} 정보를 DB에 문자열로 저장할 수 있게 하는 JPA 2.1 Converter 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Converter
public class LocaleAsString implements AttributeConverter<Locale, String> {
  /**
   * 엔티티의 {@link Locale} 속성을 Database String 수형으로 변환합니다.
   *
   * @param attribute 엔티티의 {@link Locale} 속성
   * @return Database String 값
   */
  @Override
  public String convertToDatabaseColumn(Locale attribute) {
    return (attribute != null) ? attribute.toString() : null;
  }

  /**
   * Database String 값을 {@link Locale} 값으로 변환합니다.
   *
   * @param dbData 문자열 값
   * @return {@link Locale} 인스턴스
   */
  @Override
  public Locale convertToEntityAttribute(String dbData) {
    return Stringx.isNotEmpty(dbData) ? LocaleUtils.toLocale(dbData) : null;
  }
}
