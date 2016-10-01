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

import java.util.Locale;
import java.util.Set;

/**
 * 지역화 정보를 가지는 엔티티를 나타내는 interface.
 *
 * @author sunghyouk.bae@gmail.com
 */
public interface LocalizedEntity<TValue extends LocalizedValue> extends PersistentObject {

  /**
   * 특정 지역에 해당하는 정보
   *
   * @param locale Locale 정보
   * @return 특정 지역에 해당하는 정보
   */
  TValue getLocalizedValue(Locale locale);

  /**
   * 엔티티가 보유한 지역 정보
   *
   * @return Locale Set
   */
  Set<Locale> getLocales();

  /**
   * 엔티티에 지역화 정보를 추가합니다.
   *
   * @param locale         지역 정보
   * @param localizedValue 해당 지역에 해당하는 정보
   */
  void addLocalizedValue(Locale locale, TValue localizedValue);

  /**
   * 특정 지역의 정보를 제거합니다.
   *
   * @param locale 지역 정보
   */
  void removeLocalizedValue(Locale locale);

  /**
   * 특정 지역의 정보를 가져옵니다. 만약 해당 지역의 정보가 없다면 엔티티의 정보를 이용한 정보를 제공합니다.
   *
   * @param locale 지역 정보
   * @return 지역화 정보
   */
  TValue getLocalizedValueOrDefault(Locale locale);

  /**
   * 현 Thread Context 에 해당하는 지역의 정보를 제공합니다.
   *
   * @return 지역화 정보
   */
  public TValue getCurrentLocalizedValue();
}
