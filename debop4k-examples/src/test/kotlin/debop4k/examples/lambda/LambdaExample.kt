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

package debop4k.examples.lambda

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.concurrent.thread

class LambdaExample : AbstractExampleTest() {

  data class Person(val name: String, val age: Int)


  @Test fun `람다 식 정의`() {
    val sum = { x: Int, y: Int -> x + y }

    assertThat(sum(1, 2)).isEqualTo(3)
  }

  @Test fun `람다 식 직접 실행`() {
    val x = { x: Int, y: Int -> x + y }(1, 2)
    assertThat(x).isEqualTo(3)
  }

  @Test fun `컬렉션에서 필터링하기`() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))

    val p = people.maxBy { p: Person -> p.age }
    assertThat(p).isEqualTo(Person("Bob", 31))

    val mp = people.maxBy { it.age }
    assertThat(mp).isEqualTo(Person("Bob", 31))
  }

  @Test fun `컬렉션 transform 수행`() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))

    // 모두 같은 기능을 수행한다.
    assertThat(people.joinToString(separator = " ", transform = { p -> p.name })).isEqualTo("Alice Bob")
    assertThat(people.joinToString(separator = " ", transform = { it.name })).isEqualTo("Alice Bob")
    assertThat(people.joinToString(separator = " ") { it.name }).isEqualTo("Alice Bob")
    assertThat(people.joinToString(" ") { it.name }).isEqualTo("Alice Bob")

    val transform = { p: Person -> p.name }
    assertThat(people.joinToString(separator = " ", transform = transform)).isEqualTo("Alice Bob")
    assertThat(people.joinToString(" ", transform = transform)).isEqualTo("Alice Bob")
  }

  @Test fun `변수 접근 방식 - Java 처럼 final 이 아니어도 되고, 변수를 변경해도 됨`() {
    fun getProblemCounts(responses: Collection<String>): Pair<Int, Int> {
      var clientErrors = 0
      var serverErrors = 0

      responses.forEach {
        if (it.startsWith("4")) {
          clientErrors++
        } else if (it.startsWith("5")) {
          serverErrors++
        }
      }
      return Pair(clientErrors, serverErrors)
    }

    val responses = listOf("200 OK", "418 I'm teapot", "500 Internal Server Error")
    assertThat(getProblemCounts(responses)).isEqualTo(Pair(1, 1))
  }

  @Test fun `Ref 사용하기`() {
    var counter = 0
    val inc: () -> Int = { ++counter }

    assertThat(inc()).isEqualTo(1)
    assertThat(inc()).isEqualTo(2)
  }

  @Test fun `Member Reference`() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    // Java 의 Member reference
    val getAge = Person::age

    assertThat(people.maxBy(getAge)).isEqualTo(Person("Bob", 31))
  }

  @Test fun `Member Reference with not a member of class`() {
    fun salute(): String = "Salute!"
    assertThat(run(::salute)).isEqualTo("Salute!")
  }

  @Test fun `Constructor reference`() {
    val createPerson = ::Person
    val p = createPerson("Alice", 29)
    assertThat(p).isEqualTo(Person("Alice", 29))
  }

  @Test fun `Extension Method 를 Referenece Method 로 사용하기`() {
    fun Person.isAdult() = this.age > 21
    val predicate = Person::isAdult

    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    assertThat(people.all(predicate)).isTrue()
  }

  @Test fun `Lazy로 작업하기 - asSequence`() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))
    people.asSequence().forEach { p -> log.debug("Person={}", p) }
  }

  @Test fun `Functional Interface type - Java 8`() {
    val people = listOf(Person("Alice", 29), Person("Bob", 31))

    val nameAsc = java.util.Comparator { p1: Person, p2: Person -> p1.name.compareTo(p2.name) }

    assertThat(people.sortedWith(nameAsc)).isEqualTo(people)

    assertThat(people.sortedBy { it.name }).isEqualTo(people)
  }

  @Test fun `Java 메소드에 Kotlin lambda 제공하기`() {
    thread(start = false) {
      println("runnable called")
    }.start()
  }

  @Test fun `with - lambda with receiver`() {
    // 여기서 StringBuilder instance 가 receiver 이다.
    fun alphabet() = with(StringBuilder()) {
      for (letter in 'A'..'Z') {
        this.append(letter)
      }
      // with 바깥에 있는 함수에 접근할 때
      this@LambdaExample.log.debug("with alphabet={}", toString())
      this.toString()
    }

    log.debug("alphabet={}", alphabet())
  }

  @Test fun testApply() {
    // apply 는 receiver (StringBuilder) 를 반환합니다.
    fun alphabet() = StringBuilder().apply {
      for (letter in 'A'..'Z') {
        this.append(letter)
      }
      // with 바깥에 있는 함수에 접근할 때
      log.debug("apply alphabet={}", toString())
    }

    println(alphabet().toString())
  }

  @Test fun testLet() {
    fun alphabet() = StringBuilder().let { sb ->
      for (letter in 'A'..'Z') {
        sb.append(letter)
      }
      // with 바깥에 있는 함수에 접근할 때
      log.debug("let alphabet={}", sb.toString())
      sb.toString()
    }

    log.debug("alphabet={}", alphabet())
  }

  @Test fun `Lambda return by a label`() {

    fun lookForAlice(people: List<Person>) {
      people.forEach {
        if (it.name == "Alice") return      // return@forEach
      }
      log.debug("Alice might be somewhere")
    }
  }

  @Test fun `Lambda this expression`() {

    // 중첩된 lambda 식에서, this 를 지정하기 위해 label 을 사용한다.
    val str = StringBuilder().apply sb@{
      listOf(1, 2, 3).apply {
        this@sb.append(this.toString())
      }
    }.toString()

    log.debug("str={}", str)
    assertThat(str).isEqualTo("[1, 2, 3]")
  }

  @Test fun `람다 식 대신 Anonymous Function 사용하기`() {
    fun lookForAlice(people: List<Person>) {
      // 람다 식 방식
//        people.forEach { person ->
//          if (person.name == "Alice") return
//          println("${person.name} is not Alice")
//        }
      // Anonymous Function
      people.forEach(fun(person) {
        if (person.name == "Alice") return  // 이 return 은 Lambda 와는 달리 함수에 대한 return 이므로, 모든 people 에 대해 순환합니다.
        println("${person.name} is not Alice")
      })
    }

    // Anonymous Function 은 람다식과는 달리 함수의 반환 수형을 직접 정의할 수 있다.
    val people = listOf(Person("debop", 49), Person("midoogi", 48))
    people.filter(fun(person): Boolean {
      return person.age < 30
    })
    people.filter(fun(person) = person.age < 30)
  }
}