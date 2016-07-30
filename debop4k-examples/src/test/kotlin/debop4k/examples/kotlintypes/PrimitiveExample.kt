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

package debop4k.examples.kotlintypes

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFails

class PrimitiveExample : AbstractExampleTest() {

  @Before fun setup() {
    log.debug("before")
  }

  @After fun cleanup() {
    log.debug("clean up")
  }

  data class Address(val street: String, val zipCode: Int, val city: String, val country: String)
  data class Company(val name: String, val address: Address?)
  data class Employee(val name: String, val company: Company?)

  @Test fun `Primitive Types`() {
    val i: Int = 1
    val list: List<Int> = listOf(1, 2, 3)
    val percent: Int = 49.coerceIn(0, 100)

    assertThat(49.coerceIn(0, 100)).isEqualTo(49)
    assertThat((-100).coerceIn(0, 100)).isEqualTo(0)
    assertThat(101.coerceIn(0, 100)).isEqualTo(100)
  }

  @Test fun `Nullable Primitives`() {
    val i: Int? = null
    val j: Long? = 4L

    println("23".toLong())
  }

  @Test fun `Nothing type - This function never returns`() {
    fun fail(message: String): Nothing {
      throw IllegalStateException(message)
    }

    val company = Company("a", null)

    assertThatThrownBy {
      val address: Address? = company.address ?: fail("No address")
      assertFails { address == null }
    }.isInstanceOf(IllegalStateException::class.java)
  }

  @Test fun `nullable collections`() {

    val listOfNullableInt: List<Int?> = listOf(1, null, 2)
    val nullableListOfInt: List<Int>? = listOfNotNull(1, 2, 3)
    val nullableListOfNullableInt: List<Int?>? = listOf(1, null, 2)
  }

  @Test fun arrays() {
    val array = arrayOf(1, 2, 3)
    val arrayNullable = arrayOfNulls<String>(1)

    assertThat(arrayNullable[0]).isNull()

    arrayNullable[0] = "a"
    assertThat(arrayNullable[0] != null).isTrue()
    assertThat(arrayNullable[0]).isNotNull()
  }

  @Test fun `Array of Primitive types`() {
    val intArray = intArrayOf(1, 2, 3)
    val ia2 = IntArray(5)

    intArray.forEachIndexed { index, element ->
      log.debug("Argument $index is: $element")
    }
  }
}