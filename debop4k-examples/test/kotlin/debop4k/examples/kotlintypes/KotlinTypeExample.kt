/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.kotlintypes

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
  }
}