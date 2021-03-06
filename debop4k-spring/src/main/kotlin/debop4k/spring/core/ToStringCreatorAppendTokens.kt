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
 */

package debop4k.spring.core

import org.springframework.core.style.ToStringCreator

/**
 * [toStringCreatorOf] 를 Wrapping 한 클래스입니다.
 * @author debop sunghyouk.bae@gmail.com
 */
open class ToStringCreatorAppendTokens(private val toStringCreator: ToStringCreator) {

  operator fun get(fieldName: String, value: Byte): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Char): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Short): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Int): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Long): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Float): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Double): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Boolean): ToStringCreator = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Any?): ToStringCreator = toStringCreator.append(fieldName, value)

}