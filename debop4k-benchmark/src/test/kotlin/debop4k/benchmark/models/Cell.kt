/*
 *  Copyright (c) 2016. KESTI co, ltd
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

package debop4k.benchmark.models

import debop4k.core.utils.hashOf
import java.io.Serializable

data class Cell @JvmOverloads constructor(val row: Int = 0,
                                          val col: Int = 0,
                                          var text: String? = null,
                                          var bytes: ByteArray? = null) : Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    return other != null && other is Cell && hashCode() == other.hashCode()
  }

  override fun hashCode(): Int {
    return hashOf(row, col)
  }

  companion object {
    @JvmStatic
    @JvmOverloads
    fun of(r: Int = 0, c: Int = 0): Cell = Cell(r, c)
  }
}