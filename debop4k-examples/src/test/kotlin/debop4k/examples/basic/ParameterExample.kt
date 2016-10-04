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

package debop4k.examples.basic

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * ParameterExample
 * @author sunghyouk.bae@gmail.com
 */
class ParameterExample : AbstractExampleTest() {

  @Test
  fun parameterBackingField() {

    fun overrideParameter(arg1: Int): Int {
      var arg1: Int = arg1
      arg1 += arg1
      return arg1
    }

    assertThat(overrideParameter(1)).isEqualTo(2)
    assertThat(overrideParameter(2)).isEqualTo(4)
  }
}