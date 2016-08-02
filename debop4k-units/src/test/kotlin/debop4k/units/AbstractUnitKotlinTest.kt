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

package debop4k.units

import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author sunghyouk.bae@gmail.com
 */
abstract class AbstractUnitKotlinTest {

  protected val log: Logger = LoggerFactory.getLogger(javaClass)

  val TOLERANCE: Offset<Double> = Assertions.offset(1.0e-8)

}
