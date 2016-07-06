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

package debop4k.data

import debop4k.core.AbstractValueObject
import java.sql.JDBCType

interface INamedParameter {
  val name: String
  var value: Any?
}

open class NamedParameter(override val name: String,
                          override var value: Any?) : AbstractValueObject(), INamedParameter {

  override fun equals(other: Any?): Boolean {
    return super.equals(other)
  }

  override fun hashCode(): Int {
    return super<AbstractValueObject>.hashCode()
  }

  override fun toString(): String {
    return "NamedParameter(name=$name, value=$value)"
  }
}

open class JdbcNamedParameter(name: String,
                              value: Any?,
                              var type: JDBCType = JDBCType.INTEGER) : NamedParameter(name, value) {
  override fun toString(): String {
    return "NamedParameter(name=$name, value=$value, type=$type)"
  }
}