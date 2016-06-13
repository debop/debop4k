/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.generics

import com.google.gson.Gson
import io.kotlintest.specs.FunSpec

class GenericsExample : FunSpec() {

  data class Person(val name: String, val age: Int)

  val gson = Gson()
  // reified 는 scala 의 manifest, classTag 와 같은 역활을 한다
  inline fun <reified T : Person> deserialize(jsonText: String): T {
    return gson.fromJson(jsonText, T::class.java)
  }

  interface Animal {
    var voice: String
  }

  class Cat : Animal {
    lateinit override var voice: String
  }

  fun <T : Animal> newAnimal(animalClass: Class<T>, initialize: (T) -> Unit): T {
    val animal: T = animalClass.newInstance()
    initialize(animal)
    return animal
  }

  // reified 를 이용해서 객체 생성하기
  inline fun <reified T : Animal> newAnimal2(noinline initialize: (T) -> Unit): T {
//    val animal: T = T::class.java.newInstance()
//    initialize(animal)
//    return animal
    return newAnimal(T::class.java, initialize)
  }

  init {

    test("Type Parameter Constraints") {
      fun <T : Comparable<T>> max(first: T, second: T): T = if (first > second) first else second

      max("kotlin", "java") shouldBe "kotlin"
    }

    test("Multiple Type Parameter Constraints") {

      fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
        if (!seq.endsWith('.')) {
          seq.append('.')
        }
      }

      val sb = StringBuilder("kotlin")
      ensureTrailingPeriod(sb)
      sb.toString() shouldBe "kotlin."
    }

    test("Type Checks and Casts") {
      fun sum(c: Collection<*>?): Int {
        val intList = c as? Collection<Int> ?: throw IllegalArgumentException("List is expected")
        return intList.sum()
      }

      sum(listOf(1, 2, 3)) shouldBe 6
      sum(setOf(1, 2, 3)) shouldBe 6

      // ClassCastException 이 발생합니다.
      shouldThrow<ClassCastException> {
        sum(listOf("a", "b", "c"))
      }

      shouldThrow<IllegalArgumentException> {
        sum(null)
      }
    }

    test("Class References") {

      val gson = Gson()
      val p = Person("debop", 49)
      val json = gson.toJson(p)
      println("json=$json")

      val cp = gson.fromJson(json, Person::class.java)
      cp shouldBe p

      // Type Constraint 로 작업 가능!!!
      val dp = deserialize<Person>(json)
      dp shouldBe p
    }

    test("Dynamically instancing Class") {
      val cat = newAnimal(Cat::class.java) { it.voice = "meow" }
      (cat is Cat) shouldBe true

      val cat2 = newAnimal2<Cat> { it.voice = "meow" }
      (cat2 is Cat) shouldBe true
    }

    test("Covariance: preserved Subtyping Relationship") {
      fun <T> copyData(source: List<out T>, dest: MutableList<T>) {
        for (item in source) {
          dest.add(item)
        }
      }

      val ints = listOf(1, 2, 3)
      val target = mutableListOf<Any>()
      copyData(ints, target)
      target.size shouldBe ints.size
    }
  }


}