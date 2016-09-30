/*
 * Copyright (c) 2016. KESTI co, ltd
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

package debop4k.core

import debop4k.core.utils.toUtf8Bytes
import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset

/**
 * AbstractCoreKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class AbstractCoreKotlinTest {

  protected val log = loggerOf(javaClass)

  protected val sampleText = "동해물과 백두산이 마르고 닳도록 Hello World! KESTI.co.kr"
  protected val sampleBytes = sampleText.toUtf8Bytes()

  protected val offset: Offset<Double> = Assertions.offset(1.0e-8)
}