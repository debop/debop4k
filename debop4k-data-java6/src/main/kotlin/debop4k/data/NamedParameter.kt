@file:JvmName("parameters")

package debop4k.data

import debop4k.core.AbstractValueObject
import java.sql.JDBCType

interface INamedParameter {
  val name: String
  var value: Any?
}

open class NamedParameter(override val name: String, override var value: Any?) : AbstractValueObject(), INamedParameter {

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

open class JdbcNamedParameter(name: String, value: Any?, var type: JDBCType = JDBCType.INTEGER) : NamedParameter(name, value) {
  override fun toString(): String {
    return "NamedParameter(name=$name, value=$value, type=$type)"
  }
}