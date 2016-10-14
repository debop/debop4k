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

@file:JvmName("Criteriax")

package debop4k.data.orm.hibernate

import debop4k.core.collections.fastListOf
import debop4k.core.loggerOf
import org.hibernate.Criteria
import org.hibernate.criterion.*
import org.hibernate.criterion.MatchMode.ANYWHERE
import org.hibernate.criterion.Restrictions.*
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.ASC
import java.util.*

private val log = loggerOf("Criteriax")

fun Sort.toOrders(): List<Order> {
  val orders = fastListOf<Order>()
  this.forEach {
    when (it) {
      ASC -> orders.add(Order.asc(it.property))
      else -> orders.add(Order.desc(it.property))
    }
  }
  return orders
}

fun String.eq(value: Any): Criterion = eq(this, value)
fun String.eqOrIsNull(value: Any): Criterion = eqOrIsNull(this, value)
fun String.ne(value: Any): Criterion = ne(this, value)
fun String.neOrIsNotNull(value: Any): Criterion = neOrIsNotNull(this, value)

fun Criterion.not(): Criterion = not(this)

fun String.gt(value: Any): Criterion = gt(this, value)
fun String.ge(value: Any): Criterion = ge(this, value)
fun String.geProperty(otherPropertyName: String): Criterion = geProperty(this, otherPropertyName)

fun String.gtProperty(otherPropertyName: String): Criterion = gtProperty(this, otherPropertyName)

@JvmOverloads
fun String.greater(value: Any, includeValue: Boolean = true): Criterion
    = if (includeValue) this.ge(value) else this.gt(value)

@JvmOverloads
fun String.greaterProperty(otherPropertyName: String, includeValue: Boolean = true): Criterion
    = if (includeValue) this.geProperty(otherPropertyName) else this.gtProperty(otherPropertyName)


fun String.lt(value: Any): Criterion = lt(this, value)
fun String.le(value: Any): Criterion = le(this, value)
fun String.leProperty(otherPropertyName: String): Criterion = leProperty(this, otherPropertyName)
fun String.ltProperty(otherPropertyName: String): Criterion = ltProperty(this, otherPropertyName)

@JvmOverloads
fun String.less(value: Any, includeValue: Boolean = true): Criterion
    = if (includeValue) this.le(value) else this.lt(value)

@JvmOverloads
fun String.lessProperty(otherPropertyName: String, includeValue: Boolean = true): Criterion
    = if (includeValue) this.leProperty(otherPropertyName) else this.ltProperty(otherPropertyName)


fun String.like(value: String, matchMode: MatchMode = ANYWHERE): Criterion = like(this, value, matchMode)
fun String.ilike(value: String, matchMode: MatchMode = ANYWHERE): Criterion = ilike(this, value, matchMode)
fun String.`in`(values: Collection<*>): Criterion = Restrictions.`in`(this, values)
fun String.`in`(vararg values: Any?): Criterion = Restrictions.`in`(this, *values)

/**
 * 속성 값이 lo, hi 사이의 값인지를 검사하는 질의어
 *
 * @param lo        하한 값
 * @param hi        상한 값
 * @param loInclude  include lo value ?
 * @param hiInclude  include hi value ?
 * @return the is between criterion
 */
@JvmOverloads
fun String.between(lo: Any?, hi: Any?, loInclude: Boolean = true, hiInclude: Boolean = true): Criterion {
  if (lo == null && hi == null)
    throw IllegalArgumentException("lo and hi is null both")

  if (lo != null && hi != null && loInclude && hiInclude)
    return between(this, lo, hi)

  val conj = conjunction()
  lo?.let { conj.add(this.greater(it, loInclude)) }
  hi?.let { conj.add(this.less(it, hiInclude)) }
  return conj
}

/**
 * 지정한 값이 두 속성 값 사이에 존재하는지 여부
 *
 * @param loPropertyName the lo property name
 * @param hiPropertyName the hi property name
 * @param loInclude  include lo value ?
 * @param hiInclude  include hi value ?
 * @return the is in range criterion
 */
@JvmOverloads
fun Any.inRange(loPropertyName: String,
                hiPropertyName: String,
                loInclude: Boolean = true,
                hiInclude: Boolean = true): Criterion {

  val loCriterion = loPropertyName.greater(this, loInclude)
  val hiCriterion = hiPropertyName.less(this, hiInclude)

  return conjunction()
      .add(disjunction()
               .add(isNull(loPropertyName))
               .add(loCriterion))
      .add(disjunction().add(isNull(hiPropertyName))
               .add(hiCriterion))
}

/**
 * 지정한 범위 값이 두 속성 값 구간과 겹치는지를 알아보기 위한 질의어
 *
 * @param loProperty the lo property name
 * @param hiProperty the hi property name
 * @param lo         the lo value
 * @param hi         the hi value
 * @param loInclude  include lo value ?
 * @param hiInclude  include hi value ?
 * @return the is overlap criterion
 */
@JvmOverloads
fun overlap(loProperty: String,
            hiProperty: String,
            lo: Any?,
            hi: Any?,
            loInclude: Boolean = true,
            hiInclude: Boolean = true): Criterion {
  if (lo == null && hi == null)
    throw IllegalArgumentException("상하한 값 모두 null 입니다.")

  if (lo != null && hi != null)
    return disjunction()
        .add(lo.inRange(loProperty, hiProperty, loInclude, hiInclude))
        .add(hi.inRange(loProperty, hiProperty, loInclude, hiInclude))
        .add(loProperty.between(lo, hi, loInclude, hiInclude))
        .add(hiProperty.between(lo, hi, loInclude, hiInclude))

  lo?.let {
    return disjunction()
        .add(it.inRange(loProperty, hiProperty, loInclude, hiInclude))
        .add(loProperty.less(it, loInclude))
        .add(hiProperty.greater(it, loInclude))
  }

  hi?.let {
    return disjunction()
        .add(it.inRange(loProperty, hiProperty, loInclude, hiInclude))
        .add(loProperty.less(it, hiInclude))
        .add(hiProperty.greater(it, hiInclude))
  }
  ?: throw IllegalArgumentException("상하한 값 모두 null 입니다.")
}

fun String.eqIncludeNull(value: Any?): Criterion
    = if (value == null) isNull(this) else eqOrIsNull(this, value)

@JvmOverloads
fun String.insensitiveLikeIncludeNull(value: String?,
                                      matchMode: MatchMode = MatchMode.ANYWHERE): Criterion {
  if (value.isNullOrBlank())
    return isEmpty(this)

  return disjunction()
      .add(ilike(this, value, matchMode))
      .add(isEmpty(this))
}


//
//
// Criteria
//
//


fun Criteria.addEq(propertyName: String, value: Any): Criteria = this.add(propertyName.eq(value))
fun Criteria.addEqOrIsNull(propertyName: String, value: Any): Criteria = this.add(propertyName.eqOrIsNull(value))
fun Criteria.addNe(propertyName: String, value: Any): Criteria = this.add(propertyName.ne(value))
fun Criteria.addNeOrIsNotNull(propertyName: String, value: Any): Criteria = this.add(propertyName.neOrIsNotNull(value))
fun Criteria.addGe(propertyName: String, value: Any): Criteria = this.add(propertyName.ge(value))
fun Criteria.addGt(propertyName: String, value: Any): Criteria = this.add(propertyName.gt(value))

@JvmOverloads
fun Criteria.addGreater(propertyName: String, value: Any, includeValue: Boolean = true): Criteria
    = this.add(propertyName.greater(value, includeValue))

fun Criteria.addGeProperty(propertyName: String, otherPropertyName: String): Criteria
    = this.add(propertyName.geProperty(otherPropertyName))

fun Criteria.addGtProperty(propertyName: String, otherPropertyName: String): Criteria
    = this.add(propertyName.gtProperty(otherPropertyName))

@JvmOverloads
fun Criteria.addGreaterProperty(propertyName: String, otherPropertyName: String, includeValue: Boolean = true): Criteria
    = add(propertyName.greaterProperty(otherPropertyName, includeValue))


fun Criteria.addLe(propertyName: String, value: Any): Criteria
    = this.add(propertyName.le(value))

fun Criteria.addLt(propertyName: String, value: Any): Criteria
    = this.add(propertyName.lt(value))

@JvmOverloads
fun Criteria.addLess(propertyName: String, value: Any, includeValue: Boolean = true): Criteria
    = add(propertyName.less(value, includeValue))

fun Criteria.addLeProperty(propertyName: String, otherPropertyName: String): Criteria
    = this.add(propertyName.leProperty(otherPropertyName))

fun Criteria.addLtProperty(propertyName: String, otherPropertyName: String): Criteria
    = this.add(propertyName.ltProperty(otherPropertyName))

@JvmOverloads
fun Criteria.addLessProperty(propertyName: String, otherPropertyName: String, includeValue: Boolean = true): Criteria
    = add(propertyName.lessProperty(otherPropertyName, includeValue))

fun Criteria.addAllEq(propertyValues: Map<String, Any?>): Criteria = add(allEq(propertyValues))
fun Criteria.addIsEmpty(propertyName: String): Criteria = add(isEmpty(propertyName))
fun Criteria.addIsNull(propertyName: String): Criteria = add(isNull(propertyName))
fun Criteria.addIsNotEmpty(propertyName: String): Criteria = add(isNotEmpty(propertyName))
fun Criteria.addIsNotNull(propertyName: String): Criteria = add(isNotNull(propertyName))

@JvmOverloads
fun Criteria.addLike(propertyName: String, value: String, matchMode: MatchMode = ANYWHERE): Criteria
    = add(propertyName.like(value, matchMode))

@JvmOverloads
fun Criteria.addIlike(propertyName: String, value: String, matchMode: MatchMode = ANYWHERE): Criteria
    = add(propertyName.ilike(value, matchMode))

fun Criteria.addIn(propertyName: String, values: Collection<*>): Criteria = add(propertyName.`in`(values))
fun Criteria.addIn(propertyName: String, vararg values: Any?): Criteria = add(propertyName.`in`(*values))

@JvmOverloads
fun Criteria.addBetween(propertyName: String,
                        lo: Any?,
                        hi: Any?,
                        loInclude: Boolean = true,
                        hiInclude: Boolean = true): Criteria
    = add(propertyName.between(lo, hi, loInclude, hiInclude))

@JvmOverloads
fun Criteria.addInRange(loPropertyName: String,
                        hiPropertyName: String,
                        value: Any,
                        loInclude: Boolean = true,
                        hiInclude: Boolean = true): Criteria
    = add(value.inRange(loPropertyName, hiPropertyName, loInclude, hiInclude))

@JvmOverloads
fun Criteria.addOverlap(loPropertyName: String,
                        hiPropertyName: String,
                        lo: Any?,
                        hi: Any?,
                        loInclude: Boolean = true,
                        hiInclude: Boolean = true): Criteria
    = add(overlap(loPropertyName, hiPropertyName, lo, hi, loInclude, hiInclude))

fun Criteria.addElapsed(propertyName: String, moment: Date): Criteria = add(propertyName.lt(moment))
fun Criteria.addElapsedOrEquals(propertyName: String, moment: Date): Criteria = add(propertyName.le(moment))
fun Criteria.addFutures(propertyName: String, moment: Date): Criteria = add(propertyName.gt(moment))
fun Criteria.addFuturesOrEquals(propertyName: String, moment: Date): Criteria = add(propertyName.ge(moment))

/**
 * 속성 값이 null인 경우는 false로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
 * @param propertyName 속성명
 * @param value    지정한 값
 * @return Criteria instance.
 */
fun Criteria.addNullAsFalse(propertyName: String, value: Boolean?): Criteria {
  if (value == null || value == true)
    return addEq(propertyName, true)

  return addEqOrIsNull(propertyName, false)
}

/**
 * 속성 값이 null인 경우는 true로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
 * @param propertyName 속성명
 * @param value    지정한 값
 * @return Criteria instance.
 */
fun Criteria.addNullAsTrue(propertyName: String, value: Boolean?): Criteria {
  if (value == null || value == true)
    return addEq(propertyName, true)

  return addEqOrIsNull(propertyName, true)
}

fun Criteria.addNot(expr: Criterion): Criteria = add(expr.not())


//
//
// DetachedCriteria
//
//


fun DetachedCriteria.addEq(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.eq(value))
fun DetachedCriteria.addEqOrIsNull(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.eqOrIsNull(value))
fun DetachedCriteria.addNe(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.ne(value))
fun DetachedCriteria.addNeOrIsNotNull(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.neOrIsNotNull(value))
fun DetachedCriteria.addGe(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.ge(value))
fun DetachedCriteria.addGt(propertyName: String, value: Any): DetachedCriteria = this.add(propertyName.gt(value))

@JvmOverloads
fun DetachedCriteria.addGreater(propertyName: String, value: Any, includeValue: Boolean = true): DetachedCriteria
    = this.add(propertyName.greater(value, includeValue))

fun DetachedCriteria.addGeProperty(propertyName: String, otherPropertyName: String): DetachedCriteria
    = this.add(propertyName.geProperty(otherPropertyName))

fun DetachedCriteria.addGtProperty(propertyName: String, otherPropertyName: String): DetachedCriteria
    = this.add(propertyName.gtProperty(otherPropertyName))

@JvmOverloads
fun DetachedCriteria.addGreaterProperty(propertyName: String, otherPropertyName: String, includeValue: Boolean = true): DetachedCriteria
    = add(propertyName.greaterProperty(otherPropertyName, includeValue))


fun DetachedCriteria.addLe(propertyName: String, value: Any): DetachedCriteria
    = this.add(propertyName.le(value))

fun DetachedCriteria.addLt(propertyName: String, value: Any): DetachedCriteria
    = this.add(propertyName.lt(value))

@JvmOverloads
fun DetachedCriteria.addLess(propertyName: String, value: Any, includeValue: Boolean = true): DetachedCriteria
    = add(propertyName.less(value, includeValue))

fun DetachedCriteria.addLeProperty(propertyName: String, otherPropertyName: String): DetachedCriteria
    = this.add(propertyName.leProperty(otherPropertyName))

fun DetachedCriteria.addLtProperty(propertyName: String, otherPropertyName: String): DetachedCriteria
    = this.add(propertyName.ltProperty(otherPropertyName))

@JvmOverloads
fun DetachedCriteria.addLessProperty(propertyName: String, otherPropertyName: String, includeValue: Boolean = true): DetachedCriteria
    = add(propertyName.lessProperty(otherPropertyName, includeValue))

fun DetachedCriteria.addAllEq(propertyValues: Map<String, Any?>): DetachedCriteria = add(allEq(propertyValues))
fun DetachedCriteria.addIsEmpty(propertyName: String): DetachedCriteria = add(isEmpty(propertyName))
fun DetachedCriteria.addIsNull(propertyName: String): DetachedCriteria = add(isNull(propertyName))
fun DetachedCriteria.addIsNotEmpty(propertyName: String): DetachedCriteria = add(isNotEmpty(propertyName))
fun DetachedCriteria.addIsNotNull(propertyName: String): DetachedCriteria = add(isNotNull(propertyName))

@JvmOverloads
fun DetachedCriteria.addLike(propertyName: String, value: String, matchMode: MatchMode = ANYWHERE): DetachedCriteria
    = add(propertyName.like(value, matchMode))

@JvmOverloads
fun DetachedCriteria.addIlike(propertyName: String, value: String, matchMode: MatchMode = ANYWHERE): DetachedCriteria
    = add(propertyName.ilike(value, matchMode))

fun DetachedCriteria.addIn(propertyName: String, values: Collection<*>): DetachedCriteria = add(propertyName.`in`(values))
fun DetachedCriteria.addIn(propertyName: String, vararg values: Any?): DetachedCriteria = add(propertyName.`in`(*values))

@JvmOverloads
fun DetachedCriteria.addBetween(propertyName: String,
                                lo: Any?,
                                hi: Any?,
                                loInclude: Boolean = true,
                                hiInclude: Boolean = true): DetachedCriteria
    = add(propertyName.between(lo, hi, loInclude, hiInclude))

@JvmOverloads
fun DetachedCriteria.addInRange(loPropertyName: String,
                                hiPropertyName: String,
                                value: Any,
                                loInclude: Boolean = true,
                                hiInclude: Boolean = true): DetachedCriteria
    = add(value.inRange(loPropertyName, hiPropertyName, loInclude, hiInclude))

@JvmOverloads
fun DetachedCriteria.addOverlap(loPropertyName: String,
                                hiPropertyName: String,
                                lo: Any?,
                                hi: Any?,
                                loInclude: Boolean = true,
                                hiInclude: Boolean = true): DetachedCriteria
    = add(overlap(loPropertyName, hiPropertyName, lo, hi, loInclude, hiInclude))

fun DetachedCriteria.addElapsed(propertyName: String, moment: Date) = add(propertyName.lt(moment))
fun DetachedCriteria.addElapsedOrEquals(propertyName: String, moment: Date) = add(propertyName.le(moment))
fun DetachedCriteria.addFutures(propertyName: String, moment: Date) = add(propertyName.gt(moment))
fun DetachedCriteria.addFuturesOrEquals(propertyName: String, moment: Date) = add(propertyName.ge(moment))

/**
 * 속성 값이 null인 경우는 false로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
 * @param propertyName 속성명
 * @param value    지정한 값
 * @return DetachedCriteria instance.
 */
fun DetachedCriteria.addNullAsFalse(propertyName: String, value: Boolean?): DetachedCriteria {
  if (value == null || value == true)
    return addEq(propertyName, true)

  return addEqOrIsNull(propertyName, false)
}

/**
 * 속성 값이 null인 경우는 true로 간주하고, value와 같은 값을 가지는 질의어를 추가합니다.
 * @param propertyName 속성명
 * @param value    지정한 값
 * @return DetachedCriteria instance.
 */
fun DetachedCriteria.addNullAsTrue(propertyName: String, value: Boolean?): DetachedCriteria {
  if (value == null || value == true)
    return addEq(propertyName, true)

  return addEqOrIsNull(propertyName, true)
}

fun DetachedCriteria.addNot(expr: Criterion): DetachedCriteria = add(expr.not())

