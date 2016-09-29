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

package debop4k.timeperiod.timelines

import debop4k.timeperiod.ITimePeriod
import debop4k.timeperiod.ITimePeriodCollection
import debop4k.timeperiod.ITimePeriodContainer
import debop4k.timeperiod.ITimePeriodMapper
import java.io.Serializable

/**
 * @author sunghyouk.bae@gmail.com
 */
interface ITimeLine : Serializable {

  val periods: ITimePeriodContainer

  val limits: ITimePeriod

  val mapper: ITimePeriodMapper?

  fun combinePeriods(): ITimePeriodCollection

  fun intersectPeriods(): ITimePeriodCollection

  fun calculateCaps(): ITimePeriodCollection
}