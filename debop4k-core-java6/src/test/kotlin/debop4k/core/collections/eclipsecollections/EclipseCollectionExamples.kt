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

package debop4k.core.collections.eclipsecollections

import debop4k.core.collections.eclipsecollections.PetType.CAT
import debop4k.core.collections.toIntArrayList
import io.kotlintest.specs.FunSpec
import org.eclipse.collections.api.bag.MutableBag
import org.eclipse.collections.api.list.primitive.MutableIntList
import org.eclipse.collections.api.multimap.Multimap
import org.eclipse.collections.api.set.primitive.MutableIntSet
import org.eclipse.collections.impl.bag.mutable.HashBag
import org.eclipse.collections.impl.list.mutable.FastList
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet
import org.slf4j.LoggerFactory
import java.util.concurrent.*

/**
 * EclipseCollectionExamples
 * @author debop sunghyouk.bae@gmail.com
 */
class EclipseCollectionExamples : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  val executor: ExecutorService = Executors.newFixedThreadPool(4)

  init {
    test("select method") {
      val xs = FastList.wrapCopy(1, 2, 3, 4, 5)
      val even = xs.select { it % 2 == 0 }
      even.all { it % 2 == 0 } shouldEqual true
    }

    test("asParallel") {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }

      val even = xs.asParallel(executor, 4).select { it % 2 == 0 }.toList()
      even.size shouldEqual 500

      even.detect { it % 2 == 0 } shouldEqual 0
      even.detectIndex { it % 2 == 0 } shouldEqual 0
      even.detectLastIndex { it % 2 == 0 } shouldEqual 499
      even.detect { it % 2 == 1 } shouldEqual null
      even.select { it % 2 == 1 }.size shouldEqual 0
    }

    test("partition") {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }

      val evenOdd = xs.partition { it % 2 == 0 }
      evenOdd.selected.size shouldEqual 500
      evenOdd.rejected.size shouldEqual 500
    }

    test("build IntArrayList") {
      val xs = (0 until 1000).toIntArrayList()

      xs.size() shouldEqual 1000
      xs.asLazy().select { it % 2 == 0 }.size() shouldEqual 500
    }

    test("group by") {
      var count = 0
      val xs = FastList.newWithNValues(1000) { count++ }

      val map: Multimap<String, Int> = xs.groupBy { if (it % 2 == 0) "even" else "odd" }
      map.get("even").size() shouldEqual 500
      map.get("odd").size() shouldEqual 500

      map.get("even").sumOfInt { it } shouldEqual 249500L
      map.get("odd").sumOfInt { it } shouldEqual 250000L

      xs.sumOfInt { it } shouldEqual 499500L
    }

    val people = FastList.newList<Person>().apply {
      add(Person("Mary", "Smith").addPet(CAT, "Tabby", 2))
      add(Person("Bob", "Smith").addPet(PetType.CAT, "Dolly", 3).addPet(PetType.DOG, "Spot", 2))
      add(Person("Ted", "Smith").addPet(PetType.DOG, "Spike", 4))
      add(Person("Jake", "Snake").addPet(PetType.SNAKE, "Serpy", 1))
      add(Person("Barry", "Bird").addPet(PetType.BIRD, "Tweety", 2))
      add(Person("Terry", "Turtle").addPet(PetType.TURTLE, "Speedy", 1))
      add(Person("Harry", "Hamster").addPet(PetType.HAMSTER, "Fuzzy", 1).addPet(PetType.HAMSTER, "Muzzy", 1))
    }

    test("group people by lastName") {

      val byLastName = people.groupBy { it.lastName }
      byLastName["Smith"].size shouldEqual 3

    }

    test("group people by their pets") {
      val peopleByPets = people.groupByEach { p -> p.petTypes }

      val catPeople = peopleByPets[PetType.CAT]
      catPeople.collect { it.firstName }.toSortedList() shouldEqual listOf("Bob", "Mary")

      val dogPeople = peopleByPets[PetType.DOG]
      dogPeople.collect { it.firstName }.toSortedList() shouldEqual listOf("Bob", "Ted")
    }

    test("get total number of pets") {
      val numberOfPets = people.sumOfInt { it.numgerOfPets }
      numberOfPets shouldEqual 9L
    }

    test("get ages of pets") {

      val sortedAges: MutableIntList = people
          .asLazy()
          .flatCollect { it.pets }
          .collectInt { it.age }
          .toSortedList()

      val uniqueAges: MutableIntSet = sortedAges.toSet()

      sortedAges.allSatisfy { it > 0 } shouldEqual true
      sortedAges.allSatisfy { it > 0 } shouldEqual true
      sortedAges.allSatisfy { it == 0 } shouldEqual false
      sortedAges.allSatisfy { it < 0 } shouldEqual false

      uniqueAges shouldEqual IntHashSet.newSetWith(1, 2, 3, 4)
      sortedAges.median() shouldEqual (2.0 plusOrMinus 1.0e-8)
    }

    test("group by pet type") {
      val counts: MutableBag<PetType> = people.asLazy().flatCollect { it.pets }.collect { it.type }.toBag()

      println("group by pet type = $counts")

      counts.occurrencesOf(PetType.CAT) shouldEqual 2
      counts.occurrencesOf(PetType.DOG) shouldEqual 2
      counts.occurrencesOf(PetType.HAMSTER) shouldEqual 2
      counts.occurrencesOf(PetType.SNAKE) shouldEqual 1
      counts.occurrencesOf(PetType.TURTLE) shouldEqual 1
      counts.occurrencesOf(PetType.BIRD) shouldEqual 1

    }

    test("counts by pet age") {
      val counts = people.asLazy().flatCollect { it.pets }.collectInt { it.age }.toBag()

      println("counts by pet age = $counts")

      counts.occurrencesOf(1) shouldEqual 4
      counts.occurrencesOf(2) shouldEqual 3
      counts.occurrencesOf(3) shouldEqual 1
      counts.occurrencesOf(4) shouldEqual 1
      counts.occurrencesOf(5) shouldEqual 0
    }
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

