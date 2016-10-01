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

package debop4k.mongodb

import debop4k.core.AbstractValueObject
import debop4k.core.ToStringHelper
import debop4k.core.uninitialized
import debop4k.core.utils.Objects
import debop4k.core.utils.hashOf
import org.bson.types.ObjectId

/**
 * MongoDB 용 Document의 기본 클래스
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractMongoDocument : AbstractValueObject() {

  @org.springframework.data.annotation.Id
  var id: ObjectId? = uninitialized()

  fun resetIdentifier() {
    this.id = null
  }

  override fun equals(other: Any?): Boolean {
    if (other != null && other is AbstractMongoDocument) {
      return if (id != null && other.id != null)
        Objects.equals(id, other.id)
      else
        id == null && other.id == null && super.equals(other)
    }
    return false
  }

  override fun hashCode(): Int {
    return if (id != null) hashOf(id) else toString().hashCode()
  }

  public override fun buildStringHelper(): ToStringHelper {
    return super.buildStringHelper()
        .add("id", id)
  }

  companion object {
    private const val serialVersionUID: Long = 8472797851539024491L
  }
}