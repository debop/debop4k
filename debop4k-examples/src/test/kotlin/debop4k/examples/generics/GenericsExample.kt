/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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
 *
 */

package debop4k.examples.generics

import com.google.gson.Gson
import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class GenericsExample : AbstractExampleTest() {

  data class Person(val name: String, val age: Int)

  val gson = Gson()
  // reified 는 scala 의 manifest, classTag 와 같은 역활을 한다
  inline fun <reified T : Person> deserialize(jsonText: String): T {
    return gson.fromJson(jsonText, T::class.java)
  }

  interface Animal {
    var voice: String
  }

  open class Cat : Animal {
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

  @Test
  fun `Type Parameter Constraints`() {
    fun <T : Comparable<T>> max(first: T, second: T): T = if (first > second) first else second

    assertThat(max("kotlin", "java")).isEqualTo("kotlin")
  }

  @Test fun `Multiple Type Parameter Constraints`() {

    fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
      if (!seq.endsWith('.')) {
        seq.append('.')
      }
    }

    val sb = StringBuilder("kotlin")
    ensureTrailingPeriod(sb)
    assertThat(sb.toString()).isEqualTo("kotlin.")
  }

  @Test fun `Type Checks and Casts`() {
    fun sum(c: Collection<*>?): Int {
      val intList = c as? Collection<Int> ?: throw IllegalArgumentException("List is expected")
      return intList.sum()
    }

    assertThat(sum(listOf(1, 2, 3))).isEqualTo(6)
    assertThat(sum(setOf(1, 2, 3, 3, 3, 3))).isEqualTo(6)

    // ClassCastException 이 발생합니다.
    assertThatThrownBy {
      sum(listOf("a", "b", "c"))
    }.isInstanceOf(ClassCastException::class.java)

    assertThatThrownBy {
      sum(null)
    }.isInstanceOf(IllegalArgumentException::class.java)
  }

  @Test fun `Class References`() {

    val gson = Gson()
    val p = Person("debop", 49)
    val json = gson.toJson(p)
    log.debug("json={}", json)

    val cp = gson.fromJson(json, Person::class.java)
    assertThat(cp).isEqualTo(p)

    // Type Constraint 로 작업 가능!!!
    val dp = deserialize<Person>(json)
    assertThat(dp).isEqualTo(p)
  }

  @Test fun `Dynamically instancing Class`() {
    val cat = newAnimal(Cat::class.java) { it.voice = "meow" }
    assertThat(cat).isInstanceOf(Cat::class.java)
    assertThat(cat is Cat).isTrue()

    val cat2 = newAnimal2<Cat> { it.voice = "meow" }
    assertThat(cat2 is Cat).isTrue()
  }

  @Test fun `Covariance - preserved Subtyping Relationship`() {
    fun <T> copyData(source: List<T>, dest: MutableList<T>) {
      for (item in source) {
        dest.add(item)
      }
    }

    val ints = listOf(1, 2, 3)
    val target = mutableListOf<Any>()
    copyData(ints, target)

    assertThat(target.size).isEqualTo(ints.size)
  }

}