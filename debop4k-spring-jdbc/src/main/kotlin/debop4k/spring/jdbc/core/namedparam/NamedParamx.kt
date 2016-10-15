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
@file:JvmName("NamedParamx")

package debop4k.spring.jdbc.core.namedparam

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource

fun Any.toSqlParameterSource(): BeanPropertySqlParameterSource
    = BeanPropertySqlParameterSource(this)

fun Map<String, *>.toSqlParameterSource(): MapSqlParameterSource
    = MapSqlParameterSource(this)

operator fun MapSqlParameterSource.set(paramName: String,
                                       value: Any?): MapSqlParameterSource
    = this.addValue(paramName, value)

operator fun MapSqlParameterSource.set(paramName: String,
                                       value: Any?,
                                       sqlType: Int): MapSqlParameterSource
    = this.addValue(paramName, value, sqlType)

operator fun MapSqlParameterSource.set(paramName: String,
                                       value: Any,
                                       sqlType: Int,
                                       typeName: String): MapSqlParameterSource
    = this.addValue(paramName, value, sqlType, typeName)

fun MapSqlParameterSource.addValues(vararg params: Pair<String, Any?>): MapSqlParameterSource
    = this.addValues(mapOf(*params))

operator fun MapSqlParameterSource.get(paramName: String): Any
    = this.getValue(paramName)