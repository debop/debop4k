/*
 * Copyright (c) 2016. KESTI co, ltd
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
 */

package debop4k.core.utils

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.uninitialized
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTime
import org.junit.Test
import java.math.BigDecimal
import java.math.BigInteger

/**
 * ConvertExKotlinTest
 * @author debop sunghyouk.bae@gmail.com
 */
class ConvertExKotlinTest : AbstractCoreKotlinTest() {

  @Test
  fun testAsChar() {
    val one: Char = 'A'
    val empty: Char? = null

    assertThat(one.asChar()).isEqualTo('A'.toChar())
    assertThat(empty.asChar()).isEqualTo('\u0000')
    assertThat('A'.asInt()).isEqualTo('A'.toInt())

    assertThat(5000.asChar()).isEqualTo(5000.asByte().toChar())
  }

  @Test
  fun testAsByte() {
    val one = java.lang.Byte.valueOf("1")
    val empty: Byte? = null

    assertThat(one.asByte()).isEqualTo(1.toByte())
    assertThat(empty.asByte()).isEqualTo(0.toByte())
    assertThat(12.asByte()).isEqualTo(12.toByte())
    assertThat("12".asByte()).isEqualTo(12.toByte())

    assertThat(5000.asByte()).isEqualTo(5000.toByte())
  }

  @Test
  fun testAsShort() {
    val one = 1.toShort()
    val empty: Short? = null

    assertThat(one.asShort()).isEqualTo(1.toShort())
    assertThat(empty.asShort()).isEqualTo(0.toShort())
    assertThat(12.asShort()).isEqualTo(12.toShort())
    assertThat("12.2".asShort()).isEqualTo(12.toShort())

    assertThat(5000.asShort()).isEqualTo(5000.toShort())
  }

  @Test
  fun asInt111() {
    val v: Any? = null
    val a = if (v != null) v as Int? else 0
    assertThat(a).isEqualTo(0)
  }

  @Test
  fun testAsInt() {
    val one = 1
    val empty: Int? = null

    assertThat(one.asInt()).isEqualTo(1)
    assertThat(empty.asInt()).isEqualTo(0)
    assertThat(12.asInt()).isEqualTo(12)
    assertThat("12.2".asInt()).isEqualTo(12)

    assertThat("12.abc".asInt()).isEqualTo(0)
    assertThat(5000L.asInt()).isEqualTo(5000)
  }

  @Test
  fun testAsLong() {
    val one = 1L
    val empty: Long? = null

    assertThat(one.asLong()).isEqualTo(1L)
    assertThat(empty.asLong()).isEqualTo(0L)
    assertThat(12.asLong()).isEqualTo(12L)
    assertThat("12.2".asLong()).isEqualTo(12L)

    assertThat("12.abc".asLong()).isEqualTo(0L)
    assertThat(5000.asLong()).isEqualTo(5000L)
  }

  @Test
  fun testAsFloat() {
    val one = 1.0f
    val empty: Float? = null

    assertThat(one.asFloat()).isEqualTo(1f)
    assertThat(empty.asFloat()).isEqualTo(0f)
    assertThat(12.asFloat()).isEqualTo(12f)
    assertThat("12.12".asFloat()).isEqualTo(12.12f)

    assertThat("12.abc".asFloat()).isEqualTo(0f)
    assertThat(5000.asFloat()).isEqualTo(5000f)
  }

  @Test
  fun testAsDouble() {
    val one = 1.0
    val empty: Double? = null

    assertThat(one.asDouble()).isEqualTo(1.0)
    assertThat(empty.asDouble()).isEqualTo(0.0)
    assertThat(12.asDouble()).isEqualTo(12.0)
    assertThat("12.12".asDouble()).isEqualTo(12.12)

    assertThat("12.abc".asDouble()).isEqualTo(0.0)
    assertThat(5000.asDouble()).isEqualTo(5000.0)
  }

  @Test
  fun testAsBigInt() {
    val one = BigInteger.ONE
    val empty: BigInteger? = null

    assertThat(one.asBigInt()).isEqualTo(BigInteger.ONE)
    assertThat(empty.asBigInt()).isEqualTo(BigInteger.ZERO)
    assertThat(12.asBigInt()).isEqualTo(BigInteger.valueOf(12L))
    assertThat("12.12".asBigInt()).isEqualTo(BigInteger.valueOf(12L))

    assertThat(5000.asBigInt()).isEqualTo(BigInteger.valueOf(5000L))
  }

  @Test
  fun testAsBigDecimal() {
    val one = BigDecimal.ONE
    val empty: BigDecimal? = null

    assertThat(one.asBigDecimal()).isEqualTo(BigDecimal.ONE)
    assertThat(empty.asBigDecimal()).isEqualTo(BigDecimal.ZERO)
    assertThat(12.0.asBigDecimal()).isEqualTo(BigDecimal.valueOf(12.0))
    assertThat("12.12".asBigDecimal()).isEqualTo(BigDecimal.valueOf(12.12))

    assertThat(5000.asBigDecimal()).isEqualTo(BigDecimal.valueOf(5000.0))
  }

  @Test
  fun testAsDateTime() {
    val today = DateTime.now().withTimeAtStartOfDay()
    val empty: DateTime? = null

    assertThat(today.asDateTime()).isEqualTo(DateTime.now().withTimeAtStartOfDay())
    assertThat(empty.asDateTime()).isEqualTo(empty)
    assertThat(0L.asDateTime()).isEqualTo(DateTime(0L))
    assertThat("2000-10-14".asDateTime()).isEqualTo(DateTime().withTimeAtStartOfDay().withDate(2000, 10, 14))
    assertThat("".asDateTime()).isEqualTo(empty)
    assertThat("abc".asDateTime()).isNull()
    assertThat("2000".asDateTime()).isEqualTo(DateTime().withTimeAtStartOfDay().withDate(2000, 1, 1))
  }

  @Test
  fun testAsFloatFloor() {
    val one = 1.00123456f
    val one1 = 1.011111f
    val one5 = 1.0502345f
    val one49 = 1.0499999999f
    val empty: Float? = null

    assertThat(one.asFloatFloor(2)).isEqualTo(1.00f)
    assertThat(one.asFloatFloor(1)).isEqualTo(1.0f)

    assertThat(one1.asFloatFloor(2)).isEqualTo(1.01f)
    assertThat(one1.asFloatFloor(1)).isEqualTo(1.0f)

    assertThat(one5.asFloatFloor(2)).isEqualTo(1.05f)
    assertThat(one5.asFloatFloor(1)).isEqualTo(1.1f)

    assertThat(one49.asFloatFloor(2)).isEqualTo(1.05f)
    assertThat(one49.asFloatFloor(1)).isEqualTo(1.0f)

    assertThat(empty.asFloatFloor(2)).isEqualTo(0.00f)
    assertThat(empty.asFloatFloor(1)).isEqualTo(0.0f)
  }

  @Test
  fun testAsDoubleFloor() {
    val one = 1.00123456
    val one1 = 1.011111
    val one5 = 1.0512341
    val one49 = 1.0499999999
    val empty: Double? = null

    assertThat(one.asDoubleFloor(2)).isEqualTo(1.00)
    assertThat(one.asDoubleFloor(1)).isEqualTo(1.0)

    assertThat(one1.asDoubleFloor(2)).isEqualTo(1.01)
    assertThat(one1.asDoubleFloor(1)).isEqualTo(1.0)

    assertThat(one5.asDoubleFloor(2)).isEqualTo(1.05)
    assertThat(one5.asDoubleFloor(1)).isEqualTo(1.1)

    assertThat(one49.asDoubleFloor(2)).isEqualTo(1.05)
    assertThat(one49.asDoubleFloor(1)).isEqualTo(1.0)

    assertThat(empty.asDoubleFloor(2)).isEqualTo(0.00)
    assertThat(empty.asDoubleFloor(1)).isEqualTo(0.0)
  }

  @Test
  fun testAsFloatRound() {
    val one = 1.00123456f
    val one1 = 1.011111f
    val one5 = 1.0502345f
    val one49 = 1.0499999999f
    val empty: Float? = uninitialized()

    assertThat(one.asFloatRound(2)).isEqualTo(1.00f)
    assertThat(one.asFloatRound(1)).isEqualTo(1.0f)

    assertThat(one1.asFloatRound(2)).isEqualTo(1.01f)
    assertThat(one1.asFloatRound(1)).isEqualTo(1.0f)

    assertThat(one5.asFloatRound(2)).isEqualTo(1.05f)
    assertThat(one5.asFloatRound(1)).isEqualTo(1.1f)

    assertThat(one49.asFloatRound(2)).isEqualTo(1.05f)
    assertThat(one49.asFloatRound(1)).isEqualTo(1.0f)

    assertThat(empty.asFloatRound(2)).isEqualTo(0.00f)
    assertThat(empty.asFloatRound(1)).isEqualTo(0.0f)
  }

  @Test
  fun testAsDoubleRound() {
    val one = 1.00123456
    val one1 = 1.011111
    val one5 = 1.0512341
    val one49 = 1.0499999999
    val empty: Double? = uninitialized()

    assertThat(one.asDoubleRound(2)).isEqualTo(1.00)
    assertThat(one.asDoubleRound(1)).isEqualTo(1.0)

    assertThat(one1.asDoubleRound(2)).isEqualTo(1.01)
    assertThat(one1.asDoubleRound(1)).isEqualTo(1.0)

    assertThat(one5.asDoubleRound(2)).isEqualTo(1.05)
    assertThat(one5.asDoubleRound(1)).isEqualTo(1.1)

    assertThat(one49.asDoubleRound(2)).isEqualTo(1.05)
    assertThat(one49.asDoubleRound(1)).isEqualTo(1.0)

    assertThat(empty.asDoubleRound(2)).isEqualTo(0.00)
    assertThat(empty.asDoubleRound(1)).isEqualTo(0.0)
  }
}