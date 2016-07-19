/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package debop4k.core

import org.apache.commons.lang3.builder.HashCodeBuilder
import java.io.Serializable

/**
 * @author debop sunghyouk.bae@gmail.com
 */
interface ValueObject : Serializable

abstract class AbstractValueObject : ValueObject {

  override fun equals(other: Any?): Boolean {
    return when (other) {
      null -> false
      else -> javaClass == other.javaClass && hashCode() == other.hashCode()
    }
  }

  override fun hashCode(): Int {
    return HashCodeBuilder.reflectionHashCode(this)
  }

  override fun toString(): String {
    return buildStringHelper().toString()
  }

  open protected fun buildStringHelper(): ToStringHelper = ToStringHelper(this)
}
