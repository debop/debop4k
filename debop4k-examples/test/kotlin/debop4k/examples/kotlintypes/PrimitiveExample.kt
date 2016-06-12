/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.kotlintypes

import io.kotlintest.matchers.be
import io.kotlintest.specs.FunSpec
import kotlin.test.assertFails

class PrimitiveExample : FunSpec() {

  override val oneInstancePerTest = true

  override fun beforeEach() {
    println("before each")
  }

  override fun afterEach() {
    println("success test")
  }

  data class Address(val street: String, val zipCode: Int, val city: String, val country: String)
  data class Company(val name: String, val address: Address?)
  data class Employee(val name: String, val company: Company?)

  init {

    test("Primitive Types") {
      val i: Int = 1
      val list: List<Int> = listOf(1, 2, 3)
      val percent: Int = 49.coerceIn(0, 100)

      49.coerceIn(0, 100) shouldBe 49
      (-100).coerceIn(0, 100) shouldBe 0
      101.coerceIn(0, 100) shouldBe 100
    }

    test("Nullable Primitives") {
      val i: Int? = null
      val j: Long? = 4L

      println("23".toLong())
    }

    test("Nothing type : This function never returns") {
      fun fail(message: String): Nothing {
        throw IllegalStateException(message)
      }

      val company = Company("a", null)

      shouldThrow<IllegalStateException> {
        val address: Address? = company.address ?: fail("No address")

        assertFails { address == null }
      }
    }

    test("nullable collections") {

      val listOfNullableInt: List<Int?> = listOf(1, null, 2)
      val nullableListOfInt: List<Int>? = listOfNotNull(1, 2, 3)
      val nullableListOfNullableInt: List<Int?>? = listOf(1, null, 2)
    }

    test("arrays") {
      val array = arrayOf(1, 2, 3)
      val arrayNullable = arrayOfNulls<String>(1)
      arrayNullable[0] shouldBe null
      arrayNullable[0] = "a"
      (arrayNullable[0] != null) shouldBe true
      arrayNullable[0] should be != null
    }

    test("Array of Primitive types") {
      val intArray = intArrayOf(1, 2, 3)
      val ia2 = IntArray(5)

      intArray.forEachIndexed { index, element ->
        println("Argument $index is: $element")
      }
    }
  }
}