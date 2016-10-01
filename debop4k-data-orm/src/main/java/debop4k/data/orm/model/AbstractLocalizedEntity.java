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

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * 지역화 정보를 가지는 엔티티의 추상 클래스
 *
 * @param <TId>    엔티티 Identifier 의 수형
 * @param <TValue> 지역화 정보의 수형
 * @author sunghyouk.bae@gmail.com
 */
@MappedSuperclass
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public abstract class AbstractLocalizedEntity<TId extends Serializable, TValue extends LocalizedValue>
    extends AbstractHibernateEntity<TId> implements LocalizedEntity<TValue> {

  /**
   * 기본 지역화 정보
   */
  private TValue defaultLocalizedValue = null;

  /**
   * {@link Locale} 별 지역화 정보를 가진 Map
   *
   * @return Locale 별 지역화 정보를 가진 Map
   */
  abstract public Map<Locale, TValue> getLocaleMap();

  /**
   * Java에서는 실행 시 Generic 수형을 없애버립니다.
   * scala나 c#은 generic으로 인스턴스를 생성할 수 있지만, Java는 불가능합니다.
   * 그래서 이 값을 꼭 구현해 주셔야 합니다.
   *
   * @return 기본 지역의 지역화 정보
   */
  abstract public TValue createDefaultLocalizedValue();

  /**
   * 지정한 Locale 에 해당하는 지역화 정보
   *
   * @param locale Locale 정보
   * @return 지역화 정보
   */
  @Override
  @Synchronized
  public TValue getLocalizedValue(Locale locale) {
    if (defaultLocalizedValue == null)
      defaultLocalizedValue = createDefaultLocalizedValue();

    return defaultLocalizedValue;
  }

  /**
   * 제공하는 Locale 의 컬렉션
   *
   * @return 제공하는 Locale 의 컬렉션
   */
  @Override
  public Set<Locale> getLocales() {
    return getLocaleMap().keySet();
  }

  /**
   * 지정한 지역의 지역화 정보를 추가합니다.
   *
   * @param locale         지역 정보
   * @param localizedValue 해당 지역에 해당하는 정보
   */
  @Override
  public void addLocalizedValue(Locale locale, TValue localizedValue) {
    getLocaleMap().put(locale, localizedValue);
  }

  /**
   * 해당 locale의 지역 정보를 삭제합니다.
   *
   * @param locale 지역 정보
   */
  @Override
  public void removeLocalizedValue(Locale locale) {
    getLocaleMap().remove(locale);
  }

  /**
   * 지정한 locale 의 지역정보를 가져옵니다. 만약 없다면 기본 지역 정보를 반환합니다.
   *
   * @param locale 지역 정보
   * @return locale에 해당하는 지역정보, 없다면 기본 지역정보를 반환
   */
  @Override
  public TValue getLocalizedValueOrDefault(Locale locale) {
    Map<Locale, TValue> map = getLocaleMap();
    boolean notExists = map == null || map.isEmpty() || locale == null || locale.getDisplayName() == null;

    if (!notExists && map.containsKey(locale)) {
      return map.get(locale);
    } else {
      return getLocalizedValue(locale);
    }
  }

  /**
   * 현 Thread Context 에 해당하는 지역의 정보를 제공합니다.
   *
   * @return 지역화 정보
   */
  public TValue getCurrentLocalizedValue() {
    return getLocalizedValueOrDefault(Locale.getDefault());
  }

  private static final long serialVersionUID = 5685662195288939531L;
}
