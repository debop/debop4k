/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.lambda

import io.kotlintest.specs.FunSpec
import org.slf4j.LoggerFactory

class LambdaExample : FunSpec() {

  val log = LoggerFactory.getLogger(javaClass)

  data class Person(val name: String, val age: Int)

  init {

    test("람다 식 정의") {
      val sum = { x: Int, y: Int -> x + y }

      sum(1, 2) shouldBe 3
    }

    test("람다 식 직접 실행") {
      { x: Int, y: Int -> x + y } (1, 2) shouldBe 3
    }

    test("컬렉션에서 필터링하기") {
      val people = listOf(Person("Alice", 29), Person("Bob", 31))

      people.maxBy { p: Person -> p.age } shouldBe Person("Bob", 31)
      people.maxBy { it.age } shouldBe Person("Bob", 31)
    }

    test("컬렉션 transform 수행") {
      val people = listOf(Person("Alice", 29), Person("Bob", 31))

      // 모두 같은 기능을 수행한다.
      people.joinToString(separator = " ", transform = { p -> p.name })  shouldBe "Alice Bob"
      people.joinToString(separator = " ", transform = { it.name })  shouldBe "Alice Bob"
      people.joinToString(separator = " ") { it.name } shouldBe "Alice Bob"
      people.joinToString(" ") { it.name } shouldBe "Alice Bob"

      val transform = { p: Person -> p.name }
      people.joinToString(separator = " ", transform = transform) shouldBe "Alice Bob"
      people.joinToString(" ", transform = transform) shouldBe "Alice Bob"
    }

    test("변수 접근 방식 - Java 처럼 final 이 아니어도 되고, 변수를 변경해도 됨") {
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
      getProblemCounts(responses) shouldBe Pair(1, 1)
    }

    test("Ref 사용하기") {
      var counter = 0
      val inc: () -> Int = { ++counter }

      inc() shouldBe 1
      inc() shouldBe 2
    }

    test("Member Reference") {
      val people = listOf(Person("Alice", 29), Person("Bob", 31))
      // Java 의 Member reference
      val getAge = Person::age

      people.maxBy(getAge) shouldEqual Person("Bob", 31)
    }

    test("Member Reference with not a member of class") {
      fun salute(): String = "Salute!"
      run(::salute) shouldBe "Salute!"
    }

    test("Constructor reference") {
      val createPerson = ::Person
      val p = createPerson("Alice", 29)
      p shouldBe Person("Alice", 29)
    }

    test("Extension Method 를 Referenece Method 로 사용하기") {
      fun Person.isAdult() = this.age > 21
      val predicate = Person::isAdult

      val people = listOf(Person("Alice", 29), Person("Bob", 31))
      people.all(predicate) shouldBe true
    }

    test("Lazy로 작업하기 - asSequence") {
      val people = listOf(Person("Alice", 29), Person("Bob", 31))
      people.asSequence().forEach { p -> log.debug("Person={}", p) }
    }

    test("Functional Interface type - Java 8") {
      val people = listOf(Person("Alice", 29), Person("Bob", 31))

      val nameAsc = java.util.Comparator { p1: Person, p2: Person -> p1.name.compareTo(p2.name) }

      people.sortedWith(nameAsc) shouldBe people

      people.sortedBy { it.name } shouldBe people
    }

    test("Java 메소드에 Kotlin lambda 제공하기") {
      Thread {
        println("runnable called")
      }.start();
    }

    test("with - lambda with receiver") {
      // 여기서 StringBuilder instance 가 receiver 이다.
      fun alphabet() = with(StringBuilder()) {
        for (letter in 'A'..'Z') {
          this.append(letter)
        }
        // with 바깥에 있는 함수에 접근할 때
        this@LambdaExample.log.debug("with alphabet={}", toString())
        this.toString()
      }

      println(alphabet())
    }

    test("apply") {
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

    test("let ") {
      fun alphabet() = StringBuilder().let { sb ->
        for (letter in 'A'..'Z') {
          sb.append(letter)
        }
        // with 바깥에 있는 함수에 접근할 때
        log.debug("let alphabet={}", sb.toString())
        sb.toString()
      }

      println(alphabet())
    }
  }

}