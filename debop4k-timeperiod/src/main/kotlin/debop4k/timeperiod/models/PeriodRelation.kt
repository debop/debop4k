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

package debop4k.timeperiod.models

import debop4k.timeperiod.ITimePeriod

/**
 * Period 간의 관계 종류를 나타냅니다
 * @author debop sunghyouk.bae@gmail.com
 */
enum class PeriodRelation {

  /** 알 수 없음 (두개의 기간([ITimePeriod])이 모두 Anytime 일 경우)  */
  NoRelation,

  /** 현 [ITimePeriod] 이후에 대상 [ITimePeriod]가 있을 때  */
  After,

  /** 현 [ITimePeriod]의 완료 시각이 대상 [ITimePeriod]의 시작 시각과 같습니다.  */
  StartTouching,

  /** 현 [ITimePeriod] 기간 안에 대상 [ITimePeriod]의 시작 시각만 포함될 때  */
  StartInside,

  /**
   * 현 [ITimePeriod]의 시작 시각과 대상 [ITimePeriod]의 시작 시각이 일치하고,
   * 대상 [ITimePeriod] 가 현 [ITimePeriod]에 포함될 때
   */
  InsideStartTouching,

  /**
   * 현 [ITimePeriod]의 시작 시각과 대상 [ITimePeriod]의 시작 시각이 일치하고,
   * 현 [ITimePeriod] 가 대상 [ITimePeriod]에 포함될 때
   */
  EnclosingStartTouching,

  /** 현 [ITimePeriod]가 대상 [ITimePeriod] 기간에 포함될 때  */
  Enclosing,

  /**
   * 현 [ITimePeriod]의 완료 시각과 대상 [ITimePeriod]의 완료 시각이 일치하고,
   * 현 [ITimePeriod] 가 대상 [ITimePeriod]에 포함될 때
   */
  EnclosingEndTouching,

  /**
   * 현 [ITimePeriod] 기간과 대상 [ITimePeriod]의 기간이 일치할 때,
   * 둘 다 Anytime이라도 ExactMath가 된다.
   */
  ExactMatch,

  /** 현 기간안에 대상 기간이 내부에 포함될 때  */
  Inside,

  /** 현 기간 안에 대상 기간이 포함되는데, 완료시각만 같을 때  */
  InsideEndTouching,

  /** 현 기간 안에 대상 기간의 완료 시각만 포함될 때  */
  EndInside,

  /** 현 기간의 시작 시각이 대상 기간의 완료 시각과 일치할 때  */
  EndTouching,

  /** 대상 기간의 완료 시각이 현 기간의 시작시간 전에 있을 때  */
  Before
}