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

package debop4k.timeperiod

import debop4k.core.SortDirection
import debop4k.core.SortDirection.ASC
import org.joda.time.DateTime

/**
 * @author debop sunghyouk.bae@gmail.com
 */
interface ITimePeriodContainer : MutableList<ITimePeriod>, ITimePeriod {

  val periods: MutableList<ITimePeriod>

  override val size: Int

  override fun isEmpty(): Boolean

  override var start: DateTime

  override val readOnly: Boolean

  fun containsPeriod(target: ITimePeriod): Boolean

  fun addAll(vararg elements: ITimePeriod): Unit

  fun removeIf(filter: (ITimePeriod) -> Boolean): Boolean

  fun sortByStart(sortDir: SortDirection = ASC): Unit

  fun sortByEnd(sortDir: SortDirection = ASC): Unit

  fun sortByDuration(sortDir: SortDirection = ASC): Unit

  fun compare(x: ITimePeriod, y: ITimePeriod): Int

}