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

package debop4k.data.mybatis.kotlin.models

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.utils.hashOf

/**
 * KotlinActor
 * @author sunghyouk.bae@gmail.com
 */
class KotlinActor(var id: Int? = null,
                  var firstname: String = "",
                  var lastname: String = "") : AbstractValueObject() {

  override fun hashCode(): Int {
    return if (id != null) hashOf(id) else hashOf(firstname, lastname)
  }

  public override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper().add("id", id).add("firstname", firstname).add("lastname", lastname)
  }

  companion object {
    @JvmStatic
    fun of(id: Int?, firstname: String, lastname: String): KotlinActor {
      return KotlinActor(id, firstname, lastname)
    }
  }
}