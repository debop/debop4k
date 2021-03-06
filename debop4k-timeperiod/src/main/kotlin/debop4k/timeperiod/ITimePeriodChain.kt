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

package debop4k.timeperiod

import org.joda.time.DateTime
import org.joda.time.Duration

/**
 * @author debop sunghyouk.bae@gmail.com
 */
interface ITimePeriodChain : ITimePeriodContainer {

  val head: ITimePeriod? get() = if (isEmpty()) null else periods.first()

  val last: ITimePeriod? get() = if (isEmpty()) null else periods.last()

  fun assertSpaceBefore(moment: DateTime, duration: Duration): Unit

  fun assertSpaceAfter(moment: DateTime, duration: Duration): Unit
}