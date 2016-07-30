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

package debop4k.core.java.collections

import java.io.Serializable

/**
 * Paging 처리된 데이터 셋 정보
 *
 * @author debop sunghyouk.bae@gmail.com
 */
interface PaginatedList<out T> {

  val contents: List<T>

  val pageNo: Int

  val pageSize: Int

  val totalItemCount: Long

  val totalPageCount: Long
}

/**
 * Paging 처리된 데이터 셋 정보
 *
 * @author debop sunghyouk.bae@gmail.com
 */
data class SimplePaginatedList<out T>(override val contents: List<T>,
                                      override val pageNo: Int = 0,
                                      override val pageSize: Int = 10,
                                      override val totalItemCount: Long) : PaginatedList<T>, Serializable {

  override val totalPageCount: Long

  init {
    totalPageCount = (totalItemCount / pageSize) + (if (totalItemCount % pageSize > 0) 1 else 0)
  }

}