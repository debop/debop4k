/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.spring.core

import org.springframework.core.style.ToStringCreator

/**
 * [ToStringCreator] 를 Wrapping 한 클래스입니다.
 * @author debop sunghyouk.bae@gmail.com
 */
class ToStringCreatorAppendTokens(private val toStringCreator: ToStringCreator) {

  operator fun get(fieldName: String, value: Byte) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Char) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Short) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Int) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Long) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Float) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Double) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Boolean) = toStringCreator.append(fieldName, value)

  operator fun get(fieldName: String, value: Any?) = toStringCreator.append(fieldName, value)

}