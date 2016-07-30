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
import org.assertj.core.api.Assertions.fail
import org.junit.Test
import java.util.*

class KotlinTypeExample : AbstractExampleTest() {

  data class Person(val name: String, val manager: Person?)

  data class Address(val street: String, val zipCode: Int, val city: String, val country: String)
  data class Company(val name: String, val address: Address?)
  data class Employee(val name: String, val company: Company?)


  fun Person.managerName(): String? = this.manager?.name

  fun Employee.countryName(): String = company?.address?.country ?: "Unknown"


  @Test fun `nullability 처리 방식`() {
    val ceo = Person("Da Boss", null)
    val employee = Person("Bob Smith", ceo)

    assertThat(ceo.managerName()).isNull()
    assertThat(employee.managerName()).isEqualTo("Da Boss")
  }

  @Test fun `safe acceess operators can go in a chain`() {
    val emp = Employee("Dmitry", null)
    assertThat(emp.countryName()).isEqualTo("Unknown")
  }

  @Test fun `Safe cast - as?`() {
    class Person(val firstName: String, val lastName: String) {
      override fun equals(other: Any?): Boolean {
        // as?  == if (is Type) type else null
        val otherPerson = other as? Person ?: return false
        return otherPerson.hashCode() == hashCode()
//          return if(other is Person) other.hashCode() == hashCode() else false
      }

      override fun hashCode(): Int = Objects.hash(firstName, lastName)
    }

    val p1 = Person("Dmitry", "Jemerov")
    val p2 = Person("Dmitry", "Jemerov")

    assertThat(p1 == p2).isTrue()
    assertThat(p1.equals("Dmitry")).isFalse()
  }

  @Test fun `let function - nullable 인자를 쉽게 다루기`() {
    // foo?.let {  it... }
    //   if foo != null  -> run lambda
    //   else foo == null  -> nothing hanppen

    // nothing happen
    val p: Person? = null
    p?.let { p -> fail("nothing happen!!! for null object") }
  }

  data class InnerPerson(val name: String?) {}

  @Test fun `nullable type check Elvis`() {
    fun yellAsSafe(person: InnerPerson) {
      println((person.name ?: "Nobody").toUpperCase() + "!!!")
    }
    yellAsSafe(InnerPerson(null))
  }


  @Test fun `Inheritance for nullable or not-null`() {
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