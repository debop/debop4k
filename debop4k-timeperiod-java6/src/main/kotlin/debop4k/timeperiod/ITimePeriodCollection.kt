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

/**
 * @author debop sunghyouk.bae@gmail.com
 */
interface ITimePeriodCollection : ITimePeriodContainer {

  /**
   * 대상 ITimePeriod 기간에 속하는 기간이 있다면 true를 반환합니다.
   */
  fun hasInsidePeriods(that: ITimePeriod): Boolean

  /**
   * 대상 ITimePeriod 기간과 교집합이 존재하면 true를 반환합니다.
   */
  fun hasOverlapPeriods(that: ITimePeriod): Boolean

  /**
   * 대상 시각과 교집합이 존재하면 true를 반환합니다.
   */
  fun hasIntersectionPeriods(moment: DateTime): Boolean

  /**
   * 대상 ITimePeriod 기간과 교집합이 존재하면 true를 반환합니다.
   */
  fun hasIntersectionPeriods(target: ITimePeriod): Boolean

  /**
   * 대상 ITimePeriod 기간을 포함하는 ITimePeriod 들을 열거합니다.
   */
  fun insidePeriods(target: ITimePeriod): List<ITimePeriod>

  fun overlapPeriods(target: ITimePeriod): List<ITimePeriod>

  /**
   * 지정한 moment 시각과 교집합이 존재하는 TimePeriod를 열거합니다.
   */
  fun intersectionPeriods(moment: DateTime): List<ITimePeriod>

  /**
   * 지정한 moment 기간과 교집합이 존재하는 TimePeriod를 열거합니다.
   */
  fun intersectionPeriods(target: ITimePeriod): List<ITimePeriod>

  /**
   * 대상 ITimePeriod 와 특정 관계를 가지는 ITimePeriod 요소들을 열거합니다.
   */
  fun relationPeriods(target: ITimePeriod, vararg relations: PeriodRelation): List<ITimePeriod>


}