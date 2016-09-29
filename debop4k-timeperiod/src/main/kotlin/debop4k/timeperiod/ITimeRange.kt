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

/**
 * 기간을 나타내는 Interface
 *
 * @author debop sunghyouk.bae@gmail.com
 */
interface ITimeRange : ITimePeriod {

  override var start: DateTime

  override var end: DateTime


  /** 시작시각을 지정된 시각으로 설정합니다. 시작시각 이전이여야 합니다.  */
  fun expandStartTo(moment: DateTime)

  /** 완료시각을 지정된 시각으로 설정합니다. 완료시각 이후여야 합니다.  */
  fun expandEndTo(moment: DateTime)

  /** 시작시각, 완료시각을 지정된 시각으로 설정합니다.  */
  fun expandTo(moment: DateTime)

  /** 시작시각과 완료시각을 지정된 기간으로 설정합니다.  */
  fun expandTo(period: ITimePeriod)

  /** 시작시각을 지정된 시각으로 설정합니다. 시작시각 이후여야 합니다.  */
  fun shrinkStartTo(moment: DateTime)

  /** 완료시각을 지정된 시각으로 설정합니다. 완료시각 이전이어야 합니다.  */
  fun shrinkEndTo(moment: DateTime)

  /** 시작시각, 완료시각을 지정된 시각으로 설정합니다.  */
  fun shrinkTo(moment: DateTime)

  /** 시작시각과 완료시각을 지정된 기간으로 설정합니다.  */
  fun shrinkTo(period: ITimePeriod)


}