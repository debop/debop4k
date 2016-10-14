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

package debop4k.data.orm.hibernate;

import debop4k.core.Guardx;
import debop4k.core.io.serializers.Serializers;
import debop4k.core.utils.Stringx;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static debop4k.core.Guardx.shouldNotBeEmpty;
import static org.hibernate.criterion.Restrictions.*;

/**
 * Hibernate 질의 (Criteria) 빌드를 위한 Helper class 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public final class CriteriaEx {

  private CriteriaEx() {}

  /**
   * spring-data 의 {@link Sort} 정보를 Hibernate 의 {@link Order} Criterion 으로 변환합니다.
   *
   * @param sort Spring Data 정렬 정보
   * @return Hibernate 정렬 정보
   */
  public static List<Order> toOrders(Sort sort) {
    List<Order> orders = FastList.newList();
    for (Sort.Order x : sort) {
      if (x.getDirection() == Sort.Direction.ASC)
        orders.add(Order.asc(x.getProperty()));
      else
        orders.add(Order.desc(x.getProperty()));
    }
    return orders;
  }

  /**
   * 속성 값이 지정한 값보다 큰가? 를 질의어로 빌드합니다.
   *
   * @param property     property name
   * @param value        비교 값
   * @param includeValue 비교 값 포함 여부 (greater than or equal)
   * @return 질의어
   */
  public static Criterion great(String property, Object value, boolean includeValue) {
    return (includeValue)
           ? ge(property, value)
           : gt(property, value);
  }

  /**
   * 속성 값이 지정한 값보다 작은가? 를 질의어로 빌드합니다.
   *
   * @param property     property name
   * @param value        비교 값
   * @param includeValue 비교 값 포함 여부 (greater than or equal)
   * @return 질의어
   */
  public static Criterion less(String property, Object value, boolean includeValue) {
    return (includeValue)
           ? le(property, value)
           : lt(property, value);
  }


  /**
   * 속성 값이 lo, hi 사이의 값인지를 검사하는 질의어
   *
   * @param property 속성 명
   * @param lo       하한 값
   * @param hi       상한 값
   * @return the is between criterion
   */
  public static Criterion between(String property, Object lo, Object hi) {
    return between(property, lo, hi, true, true);
  }

  /**
   * 속성 값이 lo, hi 사이의 값인지를 검사하는 질의어
   *
   * @param property  속성 명
   * @param lo        하한 값
   * @param hi        상한 값
   * @param includeLo 하한 값 포함 여부
   * @param includeHi 상한 값 포함 여부
   * @return the is between criterion
   */
  public static Criterion between(String property, Object lo, Object hi, boolean includeLo, boolean includeHi) {
    shouldNotBeEmpty(property, "property");
    if (lo == null && hi == null)
      throw new IllegalArgumentException("상하한 값 모두 null 입니다.");

    if (lo != null && hi != null)
      return Restrictions.between(property, lo, hi);

    Conjunction result = conjunction();
    if (lo != null)
      result.add(great(property, lo, includeLo));

    if (hi != null)
      result.add(less(property, hi, includeHi));

    return result;
  }

  /**
   * 지정한 값이 두 속성 값 사이에 존재하는지 여부
   *
   * @param loProperty the lo property name
   * @param hiProperty the hi property name
   * @param value      the value
   * @return the is in range criterion
   */
  public static Criterion inRange(String loProperty,
                                  String hiProperty,
                                  Object value) {
    return inRange(loProperty, hiProperty, value, true, true);
  }

  /**
   * 지정한 값이 두 속성 값 사이에 존재하는지 여부
   *
   * @param loProperty the lo property name
   * @param hiProperty the hi property name
   * @param value      the value
   * @param includeLo  the include lo
   * @param includeHi  the include hi
   * @return the is in range criterion
   */
  public static Criterion inRange(String loProperty,
                                  String hiProperty,
                                  Object value,
                                  boolean includeLo,
                                  boolean includeHi) {
    shouldNotBeEmpty(loProperty, "loProperty");
    shouldNotBeEmpty(hiProperty, "hiProperty");

    Criterion loCriterion = great(loProperty, value, includeLo);
    Criterion hiCriterion = less(hiProperty, value, includeHi);

    return conjunction()
        .add(disjunction()
                 .add(isNull(loProperty))
                 .add(loCriterion))
        .add(disjunction()
                 .add(isNull(hiProperty))
                 .add(hiCriterion));
  }


  /**
   * 지정한 범위 값이 두 속성 값 구간과 겹치는지를 알아보기 위한 질의어
   *
   * @param loProperty the lo property name
   * @param hiProperty the hi property name
   * @param lo         the lo value
   * @param hi         the hi value
   * @return the is overlap criterion
   */
  public static Criterion overlap(String loProperty,
                                  String hiProperty,
                                  Object lo,
                                  Object hi) {
    return overlap(loProperty, hiProperty, lo, hi, true, true);
  }

  /**
   * 지정한 범위 값이 두 속성 값 구간과 겹치는지를 알아보기 위한 질의어
   *
   * @param loProperty the lo property name
   * @param hiProperty the hi property name
   * @param lo         the lo value
   * @param hi         the hi value
   * @param includeLo  include lo value ?
   * @param includeHi  include hi value ?
   * @return the is overlap criterion
   */
  public static Criterion overlap(String loProperty,
                                  String hiProperty,
                                  Object lo,
                                  Object hi,
                                  boolean includeLo,
                                  boolean includeHi) {
    shouldNotBeEmpty(loProperty, "loProperty");
    shouldNotBeEmpty(hiProperty, "hiProperty");
    if (lo == null && hi == null)
      throw new IllegalArgumentException("상하한 값 모두 null 입니다.");

    if (lo != null && hi != null)
      return disjunction()
          .add(inRange(loProperty, hiProperty, lo, includeLo, includeHi))
          .add(inRange(loProperty, hiProperty, hi, includeLo, includeHi))
          .add(between(loProperty, lo, hi, includeLo, includeHi))
          .add(between(hiProperty, lo, hi, includeLo, includeHi));

    if (lo != null)
      return disjunction()
          .add(inRange(loProperty, hiProperty, lo, includeLo, includeHi))
          .add(less(loProperty, lo, includeLo))
          .add(great(hiProperty, lo, includeLo));


    return disjunction()
        .add(inRange(loProperty, hiProperty, hi, includeLo, includeHi))
        .add(less(loProperty, hi, includeHi))
        .add(great(hiProperty, hi, includeHi));
  }

  /**
   * value가 null 이 아니면, 속성값과 eq 이거나 null 인 경우 모두 구한다. value가 null 인 경우는 isNull 로 만든다.
   *
   * @param property the property name
   * @param value    the value
   * @return the eq include null
   * @deprecated Restriction.eqOrIsNull 을 사용하세요. 같은 기능입니다.
   */
  @Deprecated
  public static Criterion eqIncludeNull(String property, Object value) {
    Guardx.shouldNotBeEmpty(property, "property");
    return (value == null)
           ? isNull(property)
           : eqOrIsNull(property, value);
  }

  /**
   * 대소문자 구분 없이 LIKE 를 수행합니다.
   *
   * @param property 속성명
   * @param value    LIKE 검색할 값
   * @return Criterion
   */
  public static Criterion insensitiveLikeIncludeNull(String property, String value) {
    return insensitiveLikeIncludeNull(property, value, MatchMode.START);
  }

  /**
   * 대소문자 구분 없이 LIKE 를 수행합니다.
   *
   * @param property  속성명
   * @param value     LIKE 검색할 값
   * @param matchMode LIKE 검색 방법 ({ @link org.hibernate.criterion.MatchMode})
   * @return Criterion
   */
  public static Criterion insensitiveLikeIncludeNull(String property, String value, MatchMode matchMode) {
    return Stringx.isWhitespace(value)
           ? isEmpty(property)
           : disjunction().add(ilike(property, value, matchMode))
                          .add(isEmpty(property));
  }


  public static Criteria addEq(Criteria criteria, String property, Object value) {
    return criteria.add(eq(property, value));
  }

  public static Criteria addEqOrIsNull(Criteria criteria, String property, Object value) {
    return criteria.add(eqOrIsNull(property, value));
  }

  public static Criteria addNotEq(Criteria criteria, String property, Object value) {
    return criteria.add(not(eq(property, value)));
  }

  public static Criteria addNotEqOrIsNull(Criteria criteria, String property, Object value) {
    return criteria.add(not(eqOrIsNull(property, value)));
  }

  public static Criteria addLe(Criteria criteria, String property, Object value) {
    return criteria.add(le(property, value));
  }

  public static Criteria addLeProperty(Criteria criteria, String property, String otherProperty) {
    return criteria.add(leProperty(property, otherProperty));
  }

  public static Criteria addLt(Criteria criteria, String property, Object value) {
    return criteria.add(lt(property, value));
  }

  public static Criteria addLtProperty(Criteria criteria, String property, String otherProperty) {
    return criteria.add(ltProperty(property, otherProperty));
  }

  public static Criteria addGe(Criteria criteria, String property, Object value) {
    return criteria.add(ge(property, value));
  }

  public static Criteria addGeProperty(Criteria criteria, String property, String otherProperty) {
    return criteria.add(geProperty(property, otherProperty));
  }

  public static Criteria addGt(Criteria criteria, String property, Object value) {
    return criteria.add(gt(property, value));
  }

  public static Criteria addGtProperty(Criteria criteria, String property, String otherProperty) {
    return criteria.add(gtProperty(property, otherProperty));
  }

  public static Criteria addAllEq(Criteria criteria, Map<String, ?> propertyValues) {
    return criteria.add(allEq(propertyValues));
  }

  public static Criteria addIsEmpty(Criteria criteria, String property) {
    return criteria.add(isEmpty(property));
  }

  public static Criteria addIsNotEmpty(Criteria criteria, String property) {
    return criteria.add(isNotEmpty(property));
  }

  public static Criteria addIsNull(Criteria criteria, String property) {
    return criteria.add(isNull(property));
  }

  public static Criteria addIsNotNull(Criteria criteria, String property) {
    return criteria.add(isNotNull(property));
  }

  public static Criteria addLike(Criteria criteria, String property, String value) {
    return addLike(criteria, property, value, MatchMode.START);
  }

  public static Criteria addLike(Criteria criteria, String property, String value, MatchMode matchMode) {
    return criteria.add(like(property, value, matchMode));
  }

  public static Criteria addILike(Criteria criteria, String property, String value) {
    return addILike(criteria, property, value, MatchMode.START);
  }

  public static Criteria addILike(Criteria criteria, String property, String value, MatchMode matchMode) {
    return criteria.add(ilike(property, value, matchMode));
  }

  public static Criteria addIdEq(Criteria criteria, Object idValue) {
    return criteria.add(idEq(idValue));
  }

  public static Criteria addIn(Criteria criteria, String property, Collection<?> values) {
    return criteria.add(in(property, values));
  }

  public static Criteria addIn(Criteria criteria, String property, Object... values) {
    return criteria.add(in(property, values));
  }

  public static Criteria addBetween(Criteria criteria,
                                    String property,
                                    Object lo,
                                    Object hi) {
    return criteria.add(between(property, lo, hi));
  }

  public static Criteria addBetween(Criteria criteria,
                                    String property,
                                    Object lo,
                                    Object hi,
                                    boolean includeLo,
                                    boolean includeHi) {
    return criteria.add(between(property, lo, hi, includeLo, includeHi));
  }


  public static Criteria addInRange(Criteria criteria,
                                    String loProperty,
                                    String hiProperty,
                                    Object value) {
    return criteria.add(inRange(loProperty, hiProperty, value));
  }

  public static Criteria addInRange(Criteria criteria,
                                    String loProperty,
                                    String hiProperty,
                                    Object value,
                                    boolean includeLo,
                                    boolean includeHi) {
    return criteria.add(inRange(loProperty, hiProperty, value, includeLo, includeHi));
  }

  public static Criteria addOverlap(Criteria criteria,
                                    String loProperty,
                                    String hiProperty,
                                    Object lo,
                                    Object hi) {
    return criteria.add(overlap(loProperty, hiProperty, lo, hi));
  }

  public static Criteria addOverlap(Criteria criteria,
                                    String loProperty,
                                    String hiProperty,
                                    Object lo,
                                    Object hi,
                                    boolean includeLo,
                                    boolean includeHi) {
    return criteria.add(overlap(loProperty, hiProperty, lo, hi, includeLo, includeHi));
  }

  /**
   * 지정된 시각 이전인 경우
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param moment   기준 시각
   * @return Criteria instance.
   */
  public static Criteria addElapsed(Criteria criteria, String property, Date moment) {
    return criteria.add(lt(property, moment));
  }

  /**
   * 지정된 시각 이전이거나 같은 경우
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param moment   기준 시각
   * @return Criteria instance.
   */
  public static Criteria addElapsedOrEqual(Criteria criteria, String property, Date moment) {
    return criteria.add(le(property, moment));
  }

  /**
   * 지정된 시각 이후인 경우
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param moment   기준 시각
   * @return Criteria instance.
   */
  public static Criteria addFutures(Criteria criteria, String property, Date moment) {
    return criteria.add(gt(property, moment));
  }

  /**
   * 지정된 시각 이후이거나 같은 경우
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param moment   기준 시각
   * @return Criteria instance.
   */
  public static Criteria addFuturesOrEqual(Criteria criteria, String property, Date moment) {
    return criteria.add(ge(property, moment));
  }

  /**
   * 속성 값이 null인 경우는 false로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param value    지정한 값
   * @return Criteria instance.
   */
  public static Criteria addNullAsFalse(Criteria criteria, String property, Boolean value) {
    if (value == null || value)
      return addEq(criteria, property, true);

    return criteria.add(eqOrIsNull(property, false));
  }

  /**
   * 속성 값이 null인 경우는 true로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
   *
   * @param criteria hibernate criteria instance.
   * @param property 속성명
   * @param value    지정한 값
   * @return Criteria instance.
   */
  public static Criteria addNullAsTrue(Criteria criteria, String property, Boolean value) {
    if (value == null || value)
      return addEq(criteria, property, true);

    return criteria.add(eqOrIsNull(property, true));
  }

  public static Criteria addNot(Criteria criteria, Criterion expr) {
    return criteria.add(not(expr));
  }


  //
  // DetachedCriteria 관련
  //

  public static DetachedCriteria addEq(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(eq(property, value));
  }

  public static DetachedCriteria addEqOrIsNull(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(eqOrIsNull(property, value));
  }

  public static DetachedCriteria addNotEq(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(not(eq(property, value)));
  }

  public static DetachedCriteria addNotEqOrIsNull(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(not(eqOrIsNull(property, value)));
  }

  public static DetachedCriteria addLe(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(le(property, value));
  }

  public static DetachedCriteria addLeProperty(DetachedCriteria criteria, String property, String otherProperty) {
    return criteria.add(leProperty(property, otherProperty));
  }

  public static DetachedCriteria addLt(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(lt(property, value));
  }

  public static DetachedCriteria addLtProperty(DetachedCriteria criteria, String property, String otherProperty) {
    return criteria.add(ltProperty(property, otherProperty));
  }

  public static DetachedCriteria addGe(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(ge(property, value));
  }

  public static DetachedCriteria addGeProperty(DetachedCriteria criteria, String property, String otherProperty) {
    return criteria.add(geProperty(property, otherProperty));
  }

  public static DetachedCriteria addGt(DetachedCriteria criteria, String property, Object value) {
    return criteria.add(gt(property, value));
  }

  public static DetachedCriteria addGtProperty(DetachedCriteria criteria, String property, String otherProperty) {
    return criteria.add(gtProperty(property, otherProperty));
  }

  public static DetachedCriteria addAllEq(DetachedCriteria criteria, Map<String, ?> propertyValues) {
    return criteria.add(allEq(propertyValues));
  }

  public static DetachedCriteria addIsEmpty(DetachedCriteria criteria, String property) {
    return criteria.add(isEmpty(property));
  }

  public static DetachedCriteria addIsNotEmpty(DetachedCriteria criteria, String property) {
    return criteria.add(isNotEmpty(property));
  }

  public static DetachedCriteria addIsNull(DetachedCriteria criteria, String property) {
    return criteria.add(isNull(property));
  }

  public static DetachedCriteria addIsNotNull(DetachedCriteria criteria, String property) {
    return criteria.add(isNotNull(property));
  }

  public static DetachedCriteria addLike(DetachedCriteria criteria, String property, String value) {
    return addLike(criteria, property, value, MatchMode.START);
  }

  public static DetachedCriteria addLike(DetachedCriteria criteria, String property, String value, MatchMode matchMode) {
    return criteria.add(like(property, value, matchMode));
  }

  public static DetachedCriteria addILike(DetachedCriteria criteria, String property, String value) {
    return addILike(criteria, property, value, MatchMode.START);
  }

  public static DetachedCriteria addILike(DetachedCriteria criteria, String property, String value, MatchMode matchMode) {
    return criteria.add(ilike(property, value, matchMode));
  }

  public static DetachedCriteria addIdEq(DetachedCriteria criteria, Object idValue) {
    return criteria.add(idEq(idValue));
  }

  public static DetachedCriteria addIn(DetachedCriteria criteria, String property, Collection<?> values) {
    return criteria.add(in(property, values));
  }

  public static DetachedCriteria addIn(DetachedCriteria criteria, String property, Object... values) {
    return criteria.add(in(property, values));
  }

  public static DetachedCriteria addBetween(DetachedCriteria criteria,
                                            String property,
                                            Object lo,
                                            Object hi) {
    return criteria.add(between(property, lo, hi));
  }

  public static DetachedCriteria addBetween(DetachedCriteria criteria,
                                            String property,
                                            Object lo,
                                            Object hi,
                                            boolean includeLo,
                                            boolean includeHi) {
    return criteria.add(between(property, lo, hi, includeLo, includeHi));
  }


  public static DetachedCriteria addInRange(DetachedCriteria criteria,
                                            String loProperty,
                                            String hiProperty,
                                            Object value) {
    return criteria.add(inRange(loProperty, hiProperty, value));
  }

  public static DetachedCriteria addInRange(DetachedCriteria criteria,
                                            String loProperty,
                                            String hiProperty,
                                            Object value,
                                            boolean includeLo,
                                            boolean includeHi) {
    return criteria.add(inRange(loProperty, hiProperty, value, includeLo, includeHi));
  }

  public static DetachedCriteria addOverlap(DetachedCriteria criteria,
                                            String loProperty,
                                            String hiProperty,
                                            Object lo,
                                            Object hi) {
    return criteria.add(overlap(loProperty, hiProperty, lo, hi));
  }

  public static DetachedCriteria addOverlap(DetachedCriteria criteria,
                                            String loProperty,
                                            String hiProperty,
                                            Object lo,
                                            Object hi,
                                            boolean includeLo,
                                            boolean includeHi) {
    return criteria.add(overlap(loProperty, hiProperty, lo, hi, includeLo, includeHi));
  }

  /**
   * 지정된 시각 이전인 경우
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param moment   date
   * @return detached criteria
   */
  public static DetachedCriteria addElapsed(DetachedCriteria criteria, String property, Date moment) {
    return criteria.add(lt(property, moment));
  }

  /**
   * 지정된 시각 이전이거나 같은 경우
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param moment   date
   * @return detached criteria
   */
  public static DetachedCriteria addElapsedOrEqual(DetachedCriteria criteria, String property, Date moment) {
    return criteria.add(le(property, moment));
  }

  /**
   * 지정된 시각 이후인 경우
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param moment   date
   * @return detached criteria
   */
  public static DetachedCriteria addFutures(DetachedCriteria criteria, String property, Date moment) {
    return criteria.add(gt(property, moment));
  }

  /**
   * 지정된 시각 이후이거나 같은 경우
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param moment   date
   * @return detached criteria
   */
  public static DetachedCriteria addFuturesOrEqual(DetachedCriteria criteria, String property, Date moment) {
    return criteria.add(ge(property, moment));
  }

  /**
   * 속성 값이 null인 경우는 false로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param value    null 값을 대체할 값, empty 면 true로 간주
   * @return a detached criteria instance
   */
  public static DetachedCriteria addNullAsFalse(DetachedCriteria criteria, String property, Boolean value) {
    if (value == null || value)
      return addEq(criteria, property, true);

    return criteria.add(eqOrIsNull(property, false));
  }

  /**
   * 속성 값이 null인 경우는 true로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
   *
   * @param criteria a detached criteria
   * @param property property name
   * @param value    null 값을 대체할 값, empty 면 true로 간주
   * @return a detached criteria instance
   */
  public static DetachedCriteria addNullAsTrue(DetachedCriteria criteria, String property, Boolean value) {
    if (value == null || value)
      return addEq(criteria, property, true);

    return criteria.add(eqOrIsNull(property, true));
  }

  public static DetachedCriteria addNot(DetachedCriteria criteria, Criterion expr) {
    return criteria.add(not(expr));
  }


  public static Map<String, Object> toMap(HibernateParameter... parameters) {
    Map<String, Object> map = new HashMap<String, Object>();
    for (HibernateParameter p : parameters) {
      map.put(p.getName(), p.getValue());
    }
    return map;
  }

  @NotNull
  public static DetachedCriteria newDetachedCriteria(Class<?> clazz) {
    return DetachedCriteria.forClass(clazz);
  }

  public static DetachedCriteria copyDetachedCriteria(DetachedCriteria dc) {
    return Serializers.FST.copy(dc);
  }

  public static Criteria newCriteria(Class<?> clazz,
                                     Session session,
                                     Iterable<Order> orders,
                                     Iterable<Criterion> criterions) {
    Criteria criteria = session.createCriteria(clazz);
    addOrders(criteria, orders);
    addCriterions(criteria, criterions);
    return criteria;
  }

  public static Criteria copyCriteria(Criteria criteria) {
    return Serializers.FST.copy(criteria);
  }

  public static Criteria getExecutableCriteria(Session session, DetachedCriteria dc) {
    return dc.getExecutableCriteria(session);
  }

  public static Criteria getExecutableCriteria(Session session, DetachedCriteria dc, Iterable<Order> orders) {
    return getExecutableCriteria(session, addOrders(dc, orders));
  }

  public static DetachedCriteria addOrders(DetachedCriteria dc, Iterable<Order> orders) {
    for (Order order : orders) {
      dc.addOrder(order);
    }
    return dc;
  }

  public static Criteria addOrders(Criteria criteria, Iterable<Order> orders) {
    for (Order order : orders) {
      criteria.addOrder(order);
    }
    return criteria;
  }

  public static Criteria addCriterions(Criteria criteria, Iterable<Criterion> criterions) {
    for (Criterion criterion : criterions) {
      criteria.add(criterion);
    }
    return criteria;
  }

  public static Query setParameters(Query query, Iterable<HibernateParameter> parameters) {
    for (HibernateParameter hp : parameters) {
      query.setParameter(hp.getName(), hp.getValue());
    }
    return query;
  }

  public static Query setParameters(Query query, HibernateParameter... parameters) {
    return setParameters(query, Arrays.asList(parameters));
  }

  public static Criteria setFirstResult(Criteria criteria, int firstResult) {
    if (firstResult >= 0)
      criteria.setFirstResult(firstResult);
    return criteria;
  }

  public static Query setFirstResult(Query query, int firstResult) {
    if (firstResult >= 0)
      query.setFirstResult(firstResult);
    return query;
  }

  public static Criteria setMaxResults(Criteria criteria, int maxResults) {
    if (maxResults > 0)
      criteria.setMaxResults(maxResults);
    return criteria;
  }

  public static Query setMaxResults(Query query, int maxResults) {
    if (maxResults > 0)
      query.setMaxResults(maxResults);
    return query;
  }

  public static Criteria setPaging(Criteria criteria, Pageable pageage) {
    return setPaging(criteria, pageage.getPageNumber() * pageage.getPageSize(), pageage.getPageSize());
  }

  public static Criteria setPaging(Criteria criteria, int firstResult, int maxResults) {
    setFirstResult(criteria, firstResult);
    return setMaxResults(criteria, maxResults);
  }

  public static Query setPaging(Query query, Pageable pageage) {
    return setPaging(query, pageage.getPageNumber() * pageage.getPageSize(), pageage.getPageSize());
  }

  public static Query setPaging(Query query, int firstResult, int maxResults) {
    setFirstResult(query, firstResult);
    return setMaxResults(query, maxResults);
  }


  public static Criteria setPageable(Criteria criteria, Pageable pageable) {
    setPaging(criteria, pageable);
    return addOrders(criteria, toOrders(pageable.getSort()));
  }

  public static Query setPageable(Query query, Pageable pageable) {
    return setPaging(query, pageable);
  }
}
