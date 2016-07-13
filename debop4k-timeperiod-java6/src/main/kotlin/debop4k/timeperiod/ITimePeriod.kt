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

import debop4k.timeperiod.models.PeriodRelation
import org.joda.time.DateTime
import org.joda.time.Duration
import java.io.Serializable

/**
 * 기간을 나타냅니다.
 * @author Sunghyouk Bae <sunghyouk.bae@gmail.com>
 */
interface ITimePeriod : Comparable<ITimePeriod>, Serializable {

  val start: DateTime
  val end: DateTime

  val duration: Duration
    get() = Duration(start, end)

  val readOnly: Boolean

  fun hasStart(): Boolean
  fun hasEnd(): Boolean
  fun hasPeriod(): Boolean
  fun isMoment(): Boolean
  fun isAnyTime(): Boolean

  fun setup(newStart: DateTime, newEnd: DateTime)

  fun copy(): ITimePeriod
  fun copy(offset: Duration = Duration.ZERO): ITimePeriod
  fun move(): Unit
  fun move(offset: Duration = Duration.ZERO): Unit

  fun isSamePeriod(other: ITimePeriod): Boolean
  fun hasInside(moment: DateTime): Boolean
  fun hasInside(other: ITimePeriod): Boolean
  fun intersectsWith(other: ITimePeriod): Boolean
  fun overlapsWith(other: ITimePeriod): Boolean

  fun reset(): Unit

  fun relation(other: ITimePeriod): PeriodRelation
  fun intersection(other: ITimePeriod): ITimePeriod
  fun union(other: ITimePeriod): ITimePeriod
}

