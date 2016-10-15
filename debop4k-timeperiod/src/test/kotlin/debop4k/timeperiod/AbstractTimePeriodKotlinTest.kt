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

import debop4k.core.kodatimes.now
import debop4k.core.loggerOf
import org.joda.time.DateTime

abstract class AbstractTimePeriodKotlinTest {

  protected val log = loggerOf(javaClass)

  val testDate = DateTime(2000, 10, 2, 13, 45, 53, 673)
  val testDiffDate = DateTime(2002, 9, 3, 7, 14, 22, 234)
  val testNow = now()

}