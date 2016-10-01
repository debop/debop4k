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

package debop4k.data


import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf
import java.io.Serializable
import java.sql.JDBCType

interface INamedParameter : Serializable {
  val name: String
  var value: Any?
}

abstract class AbstractNamedParameter(override val name: String,
                                      override var value: Any?) :
    AbstractValueObject(), INamedParameter {

  override fun equals(other: Any?): Boolean = when (other) {
    is INamedParameter -> hashCode() == other.hashCode()
    else -> false
  }

  override fun hashCode(): Int {
    return hashOf(name)
  }

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("name", name)
        .add("value", value)
  }
}

open class JdbcNamedParameter(name: String,
                              value: Any?,
                              var type: JDBCType = JDBCType.INTEGER) :
    AbstractNamedParameter(name, value) {

  override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("type", type)
  }
}