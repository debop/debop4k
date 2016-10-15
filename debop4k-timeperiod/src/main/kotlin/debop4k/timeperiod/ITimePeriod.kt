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
  val readonly: Boolean

  val duration: Duration get() = Duration(start, end)
  val hasStart: Boolean get() = start != MinPeriodTime
  val hasEnd: Boolean get() = end != MaxPeriodTime
  val hasPeriod: Boolean get() = hasStart && hasEnd
  val isMoment: Boolean get() = start == end
  val isAnyTime: Boolean get() = !hasStart && !hasEnd

//  fun hasStart(): Boolean = start != MinPeriodTime
//  fun hasEnd(): Boolean = end != MaxPeriodTime
//  fun hasPeriod(): Boolean = hasStart() && hasEnd()
//  fun isMoment(): Boolean = start == end
//  fun isAnyTime(): Boolean = !hasStart() && !hasEnd()

//  fun isReadonly(): Boolean = readonly

  fun setup(newStart: DateTime? = MinPeriodTime, newEnd: DateTime? = MaxPeriodTime)

  fun copy(): ITimePeriod = copy(Duration.ZERO)
  fun copy(offset: Duration = Duration.ZERO): ITimePeriod
  fun move(): Unit = move(Duration.ZERO)
  fun move(offset: Duration = Duration.ZERO): Unit

  /** 같은 기간인가? */
  fun isSamePeriod(other: ITimePeriod?): Boolean

  /** 지정한 시각이 현 기간안에 포함되는가? */
  fun hasInside(moment: DateTime): Boolean

  /** 지정한 기간이 현 기간안에 포함되는가? */
  fun hasInside(other: ITimePeriod): Boolean

  /** 지정한 기간과 현 기간의 교집합이 있는가? */
  fun intersectsWith(other: ITimePeriod): Boolean

  /** 지정한 기간과 현 기간이 겹치는 부분이 있는가 */
  fun overlapsWith(other: ITimePeriod): Boolean

  fun reset(): Unit

  /** 지정한 기간과의 관계 */
  fun relation(other: ITimePeriod): PeriodRelation

  /** 지정한 기간과의 교집합 기간을 구합니다 */
  fun intersection(other: ITimePeriod): ITimePeriod?

  /** 지정한 기간과의 합집합 기간을 구합니다 */
  fun union(other: ITimePeriod): ITimePeriod?

}

