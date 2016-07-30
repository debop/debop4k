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

package debop4k.examples.classes

import debop4k.examples.AbstractExampleTest
import org.junit.Test

/**
 * @author sunghyouk.bae@gmail.com
 */

enum class BitCount(val value: Int) {
  x16(16),
  x32(32),
  x64(64);

  companion object {
    fun from(findValue: Int): BitCount = BitCount.values().first { it.value == findValue }
  }
}


class EnumClassExample : AbstractExampleTest() {

  @Test
  fun `enum class 생성하기`() {
      val bit = BitCount.x32
      println(bit)
    }

  @Test fun `MassUnit enum class 생성하기`() {
      val mg = MassUnit.MILLIGRAM
      println(mg)
    }
}