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

package debop4k.core.java.collections.eclipsecollections

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.java.collections.toIntArrayList
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.collections.api.bag.MutableBag
import org.eclipse.collections.api.list.primitive.MutableIntList
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.api.set.primitive.MutableIntSet
import org.eclipse.collections.impl.bag.mutable.HashBag
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet
import org.junit.Test
import java.util.concurrent.*

/**
 * EclipseCollectionExamples
 * @author debop sunghyouk.bae@gmail.com
 */
class EclipseCollectionExamples : AbstractCoreKotlinTest() {

  val executor: ExecutorService = Executors.newFixedThreadPool(4)


  @Test
  fun filtering() {
    val xs = FastList.wrapCopy(1, 2, 3, 4, 5)
    val even = xs.select { it % 2 == 0 }
    assertThat(even.all { it % 2 == 0 }).isTrue()
  }

  @Test
  fun asParallel() {
    var count = 0
    val xs = FastList.newWithNValues(1000) { count++ }

    val even = xs.asParallel(executor, 4).select { it % 2 == 0 }.toList()
    assertThat(even.size).isEqualTo(500)

    assertThat(even.detect { it % 2 == 0 }).isEqualTo(0)
    assertThat(even.detectIndex { it % 2 == 0 }).isEqualTo(0)
    assertThat(even.detectLastIndex { it % 2 == 0 }).isEqualTo(499)
    assertThat(even.detect { it % 2 == 1 }).isNull()
    assertThat(even.select { it % 2 == 1 }).hasSize(0)
  }

  @Test
  fun partitioning() {
    var count = 0
    val xs = FastList.newWithNValues(1000) { count++ }

    val evenOdd = xs.partition { it % 2 == 0 }
    assertThat(evenOdd.selected.size).isEqualTo(500)
    assertThat(evenOdd.rejected.size).isEqualTo(500)
  }

  @Test
  fun buildIntArrayList() {
    val xs = (0 until 1000).toIntArrayList()

    assertThat(xs.size()).isEqualTo(1000)
    assertThat(xs.asLazy().select { it % 2 == 0 }.size()).isEqualTo(500)
  }

  @Test fun groupBy() {
    var count = 0
    val xs = FastList.newWithNValues(1000) { count++ }

    val map: Multimap<String, Int> = xs.groupBy { if (it % 2 == 0) "even" else "odd" }
    assertThat(map.get("even").size()).isEqualTo(500)
    assertThat(map.get("odd").size()).isEqualTo(500)

    assertThat(map.get("even").sumOfInt { it }).isEqualTo(249500L)
    assertThat(map.get("odd").sumOfInt { it }).isEqualTo(250000L)

    assertThat(xs.sumOfInt { it }).isEqualTo(499500L)
  }

  val people = FastList.newList<Person>().apply {
    add(Person("Mary", "Smith").addPet(PetType.CAT, "Tabby", 2))
    add(Person("Bob", "Smith").addPet(PetType.CAT, "Dolly", 3).addPet(PetType.DOG, "Spot", 2))
    add(Person("Ted", "Smith").addPet(PetType.DOG, "Spike", 4))
    add(Person("Jake", "Snake").addPet(PetType.SNAKE, "Serpy", 1))
    add(Person("Barry", "Bird").addPet(PetType.BIRD, "Tweety", 2))
    add(Person("Terry", "Turtle").addPet(PetType.TURTLE, "Speedy", 1))
    add(Person("Harry", "Hamster").addPet(PetType.HAMSTER, "Fuzzy", 1).addPet(PetType.HAMSTER, "Muzzy", 1))
  }

  @Test
  fun `group people by lastName`() {

    val byLastName = people.groupBy { it.lastName }
    assertThat(byLastName["Smith"].size).isEqualTo(3)

  }

  @Test fun `group people by their pets`() {
    val peopleByPets = people.groupByEach { p -> p.petTypes }

    val catPeople = peopleByPets[PetType.CAT]
    assertThat(catPeople.collect { it.firstName }.toSortedList()).isEqualTo(listOf("Bob", "Mary"))

    val dogPeople = peopleByPets[PetType.DOG]
    assertThat(dogPeople.collect { it.firstName }.toSortedList()).isEqualTo(listOf("Bob", "Ted"))
  }

  @Test fun `get total number of pets`() {
    val numberOfPets = people.sumOfInt { it.numgerOfPets }
    assertThat(numberOfPets).isEqualTo(9L)
  }

  @Test fun `get ages of pets`() {

    val sortedAges: MutableIntList = people
        .asLazy()
        .flatCollect { it.pets }
        .collectInt { it.age }
        .toSortedList()

    val uniqueAges: MutableIntSet = sortedAges.toSet()

    assertThat(sortedAges.allSatisfy { it > 0 }).isTrue()
    assertThat(sortedAges.allSatisfy { it > 0 }).isTrue()
    assertThat(sortedAges.allSatisfy { it == 0 }).isFalse()
    assertThat(sortedAges.allSatisfy { it < 0 }).isFalse()

    assertThat(uniqueAges).isEqualTo(IntHashSet.newSetWith(1, 2, 3, 4))
    assertThat(sortedAges.median()).isEqualTo(2.0, TOLERANCE)
  }

  @Test fun `group by pet type`() {
    val counts: MutableBag<PetType> = people.asLazy().flatCollect { it.pets }.collect { it.type }.toBag()

    println("group by pet type = $counts")

    assertThat(counts.occurrencesOf(PetType.CAT)).isEqualTo(2)
    assertThat(counts.occurrencesOf(PetType.DOG)).isEqualTo(2)
    assertThat(counts.occurrencesOf(PetType.HAMSTER)).isEqualTo(2)
    assertThat(counts.occurrencesOf(PetType.SNAKE)).isEqualTo(1)
    assertThat(counts.occurrencesOf(PetType.TURTLE)).isEqualTo(1)
    assertThat(counts.occurrencesOf(PetType.BIRD)).isEqualTo(1)

  }

  @Test fun `counts by pet age`() {
    val counts = people.asLazy().flatCollect { it.pets }.collectInt { it.age }.toBag()

    println("counts by pet age = $counts")

    assertThat(counts.occurrencesOf(1)).isEqualTo(4)
    assertThat(counts.occurrencesOf(2)).isEqualTo(3)
    assertThat(counts.occurrencesOf(3)).isEqualTo(1)
    assertThat(counts.occurrencesOf(4)).isEqualTo(1)
    assertThat(counts.occurrencesOf(5)).isEqualTo(0)
  }
}

enum class PetType {
  CAT, DOG, HAMSTER, TURTLE, BIRD, SNAKE
}

data class Pet(val type: PetType, val name: String, val age: Int)

data class Person(val firstName: String, val lastName: String,
                  val pets: FastList<Pet> = FastList.newList<Pet>()) {

  val getPetType: (Pet) -> PetType = { it.type }

  fun named(name: String): Boolean {
    return (firstName + " " + lastName) == name
  }

  fun hasPet(petType: PetType): Boolean {
    return this.pets.anySatisfy { it.type == petType }
  }

  val petTypes: MutableBag<PetType>
    get() = pets.collect({ it.type }, HashBag.newBag())

  fun addPet(petType: PetType, name: String, age: Int): Person {
    pets.add(Pet(petType, name, age))
    return this
  }

  val numgerOfPets: Int
    get() = pets.size
}

