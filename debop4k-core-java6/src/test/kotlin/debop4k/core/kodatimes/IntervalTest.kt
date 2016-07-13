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

package debop4k.core.kodatimes

import org.assertj.core.api.Assertions.assertThat
import org.joda.time.Interval
import org.junit.Test

class IntervalTest : AbstractKodaTimesTest() {

  @Test fun rangeTest() {
    val start = now()
    val end = start + 1.days()

    val range: Interval = start..end

    val inRange = range.days().all { day -> start <= day && day <= end }
    assertThat(inRange).isTrue()

    assertThat(range.days().last()).isEqualTo(end)
  }

  @Test fun rangeStepTest() {
    val start = now()
    val end = start + 1.days()

    (start..end step 1.hours().toPeriod()).forEach { hour ->
      assertThat(start <= hour && hour <= end).isTrue()
    }
  }
}