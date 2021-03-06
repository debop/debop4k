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

package debop4k.core

import debop4k.core.utils.asString
import java.io.Serializable
import java.util.*

/**
 * 객체의 속성 정보를 문자열로 표현하기 위한 Helper class 입니다.
 * @author debop sunghyouk.bae@gmail.com
 */
class ToStringHelper(val className: String) : Serializable {

  constructor (obj: Any) : this(obj.javaClass.simpleName)

  private val map = HashMap<String, Any?>()

  fun add(name: String, value: Any?): ToStringHelper {
    map.put(name, value.asString())
    return this
  }

  override fun toString(): String {
    val properties = map.entries.joinToString(separator = ",") { entry ->
      "${entry.key}=${entry.value}"
    }
    return "$className{$properties}"
  }

  companion object {
    @JvmStatic fun of(className: String): ToStringHelper = ToStringHelper(className)
    @JvmStatic fun of(obj: Any): ToStringHelper = ToStringHelper(obj)
  }
}