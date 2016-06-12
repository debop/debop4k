/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.kotlintypes

import com.google.common.base.Objects
import io.kotlintest.specs.FunSpec

class KotlinTypeExample : FunSpec() {

  data class Person(val name: String, val manager: Person?)

  data class Address(val street: String, val zipCode: Int, val city: String, val country: String)
  data class Company(val name: String, val address: Address?)
  data class Employee(val name: String, val company: Company?)


  fun Person.managerName(): String? = this.manager?.name

  fun Employee.countryName(): String = company?.address?.country ?: "Unknown"

  init {

    test("nullability 처리 방식") {
      val ceo = Person("Da Boss", null)
      val employee = Person("Bob Smith", ceo)

      ceo.managerName() shouldBe null
      employee.managerName() shouldBe "Da Boss"
    }

    test("safe acceess operators can go in a chain") {
      val emp = Employee("Dmitry", null)
      emp.countryName() shouldBe "Unknown"
    }

    test("Safe cast: as?") {
      class Person(val firstName: String, val lastName: String) {
        override fun equals(other: Any?): Boolean {
          // as?  == if (is Type) type else null
          val otherPerson = other as? Person ?: return false
          return otherPerson.hashCode() == hashCode()
//          return if(other is Person) other.hashCode() == hashCode() else false
        }

        override fun hashCode(): Int = Objects.hashCode(firstName, lastName)
      }

      val p1 = Person("Dmitry", "Jemerov")
      val p2 = Person("Dmitry", "Jemerov")

      (p1 == p2) shouldBe true
      p1.equals("Dmitry") shouldBe false
    }

    test("let function - nullable 인자를 쉽게 다루기") {
      // foo?.let {  it... }
      //   if foo != null  -> run lambda
      //   else foo == null  -> nothing hanppen

      // nothing happen
      val p: Person? = null
      p?.let { p -> fail("nothing happen!!! for null object") }
    }

    test("nullable type check ?:") {
      data class Person(val name: String?)

      fun yellAsSafe(person: Person) {
        println((person.name ?: "Nobody").toUpperCase() + "!!!")
      }
      yellAsSafe(Person(null))
    }


    test("Inheritance for nullable or not-null") {
      class StringPrinter : StringProcessor {
        override fun process(value: String) {
          println(value)
        }
      }

      class NullableStringPrinter : StringProcessor {
        override fun process(value: String?) {
          if (value != null) {
            println(value)
          }
        }
      }
    }
  }


}