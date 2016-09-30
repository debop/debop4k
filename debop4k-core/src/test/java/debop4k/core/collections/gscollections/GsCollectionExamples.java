/*
 * Copyright 2015-2020 KESTI s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.collections.gscollections;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.data.Offset;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.primitive.IntBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.block.function.Function0;
import org.eclipse.collections.api.block.function.primitive.IntFunction;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.block.predicate.primitive.IntPredicate;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.api.multimap.Multimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.api.partition.list.PartitionMutableList;
import org.eclipse.collections.api.set.primitive.IntSet;
import org.eclipse.collections.impl.block.factory.primitive.IntPredicates;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * gs-collection 관련 Examples
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class GsCollectionExamples extends AbstractCoreTest {

  private Predicate<Integer> isEvenPredicate = new Predicate<Integer>() {
    @Override
    public boolean accept(Integer each) {
      return each % 2 == 0;
    }
  };
  private Predicate<Integer> isOddPredicate = new Predicate<Integer>() {
    @Override
    public boolean accept(Integer each) {
      return each % 2 == 1;
    }
  };

  private IntPredicate isEvenIntPredicate = new IntPredicate() {
    @Override
    public boolean accept(int value) {
      return value % 2 == 0;
    }
  };

  private IntPredicate positivePredicate = new IntPredicate() {
    @Override
    public boolean accept(int value) {
      return value > 0;
    }
  };
  private IntPredicate negativePredicate = new IntPredicate() {
    @Override
    public boolean accept(int value) {
      return value < 0;
    }
  };
  private IntPredicate isZeroPredicate = new IntPredicate() {
    @Override
    public boolean accept(int value) {
      return value == 0;
    }
  };

  Function<Person, Iterable<Pet>> getPets = new Function<Person, Iterable<Pet>>() {
    @Override
    public Iterable<Pet> valueOf(Person object) {
      return object.getPets();
    }
  };
  IntFunction<Pet> getPetAge = new IntFunction<Pet>() {
    @Override
    public int intValueOf(Pet anObject) {
      return anObject.getAge();
    }
  };

  @Test
  public void select() {
    FastList<Integer> xs = FastList.wrapCopy(1, 2, 3, 4, 5);
    FastList<Integer> even = xs.select(isEvenPredicate);
    //assertThat(even).contains(2, 4).doesNotContain(1, 3, 5);
  }

  @Test
  public void asParallel() {

    final int[] array = new int[]{0};
    FastList<Integer> xs = FastList.newWithNValues(1000, new Function0<Integer>() {
      @Override
      public Integer value() {
        return array[0]++;
      }
    });

    MutableList<Integer> even = xs.asParallel(Executors.newFixedThreadPool(4), 4)
                                  .select(isEvenPredicate)
                                  .toList();
    assertThat(even).hasSize(500);

    // findFirst 와 같다.
    assertThat(even.detect(isEvenPredicate)).isEqualTo(0);

    // indexOf
    assertThat(even.detectIndex(isEvenPredicate)).isEqualTo(0);

    // indexLastOf
    assertThat(even.detectLastIndex(isEvenPredicate)).isEqualTo(499);

    assertThat(even.detect(isOddPredicate)).isNull();
  }

  @Test
  public void partition() {
    final int[] array = new int[]{0};
    FastList<Integer> xs = FastList.newWithNValues(1000, new Function0<Integer>() {
      @Override
      public Integer value() {
        return array[0]++;
      }
    });

    PartitionMutableList<Integer> evenOdd = xs.partition(isEvenPredicate);
    assertThat(evenOdd.getSelected().size()).isEqualTo(500);
    assertThat(evenOdd.getRejected().size()).isEqualTo(500);
  }

  @Test
  public void intArrayList() {
    IntArrayList xs = new IntArrayList(1000);
    for (int i = 0; i < 1000; i++) {
      xs.add(i);
    }
    assertThat(xs.size()).isEqualTo(1000);
    assertThat(xs.asLazy().select(isEvenIntPredicate).size()).isEqualTo(500);
  }

  @Test
  public void groupBy() {
    final int[] array = new int[]{0};
    FastList<Integer> xs = FastList.newWithNValues(1000, new Function0<Integer>() {
      @Override
      public Integer value() {
        return array[0]++;
      }
    });

    Multimap<String, Integer> map = xs.groupBy(new Function<Integer, String>() {
      @Override
      public String valueOf(Integer object) {
        return object % 2 == 0 ? "even" : "odd";
      }
    });
    assertThat(map.get("even").size()).isEqualTo(500);
    assertThat(map.get("odd").size()).isEqualTo(500);

    IntFunction<Integer> intIdentity = new IntFunction<Integer>() {
      @Override
      public int intValueOf(Integer anObject) {
        return anObject;
      }
    };
    assertThat(map.get("even").sumOfInt(intIdentity)).isEqualTo(249500);
    assertThat(map.get("odd").sumOfInt(intIdentity)).isEqualTo(250000);

    assertThat(xs.sumOfInt(intIdentity))
        .isEqualTo(map.get("even").sumOfInt(intIdentity) + map.get("odd").sumOfInt(intIdentity));
  }


  MutableList<Person> people;

  @Before
  public void setup() {
    people = FastList.newListWith(
        new Person("Mary", "Smith").addPet(PetType.CAT, "Tabby", 2),
        new Person("Bob", "Smith").addPet(PetType.CAT, "Dolly", 3).addPet(PetType.DOG, "Spot", 2),
        new Person("Ted", "Smith").addPet(PetType.DOG, "Spike", 4),
        new Person("Jake", "Snake").addPet(PetType.SNAKE, "Serpy", 1),
        new Person("Barry", "Bird").addPet(PetType.BIRD, "Tweety", 2),
        new Person("Terry", "Turtle").addPet(PetType.TURTLE, "Speedy", 1),
        new Person("Harry", "Hamster").addPet(PetType.HAMSTER, "Fuzzy", 1).addPet(PetType.HAMSTER, "Muzzy", 1)
                                 );
  }

  @Test
  public void groupPeopleByLastName() {
    Function<Person, String> getLastName = new Function<Person, String>() {
      @Override
      public String valueOf(Person object) {
        return object.getLastName();
      }
    };
    MutableListMultimap<String, Person> byLastName = people.groupBy(getLastName);

    assertThat(byLastName.get("Smith").size()).isEqualTo(3);
  }

  @Test
  public void groupPeopleByTheirPets() {
    Function<Person, String> getFirstName = new Function<Person, String>() {
      @Override
      public String valueOf(Person object) {
        return object.getFirstName();
      }
    };
    Function<Person, Iterable<PetType>> getPetTypes = new Function<Person, Iterable<PetType>>() {
      @Override
      public Iterable<PetType> valueOf(Person object) {
        return object.getPetTypes();
      }
    };
    Multimap<PetType, Person> peopleByPets = people.groupByEach(getPetTypes);

    RichIterable<Person> catPeople = peopleByPets.get(PetType.CAT);
    assertThat(catPeople.collect(getFirstName).toSortedList().makeString()).isEqualTo("Bob, Mary");

    RichIterable<Person> dogPeople = peopleByPets.get(PetType.DOG);
    assertThat(dogPeople.collect(getFirstName).toSortedList().makeString()).isEqualTo("Bob, Ted");
  }

  @Test
  public void getTotalNumberOfPets() {
    IntFunction<Person> personNumberOfPets = new IntFunction<Person>() {
      @Override
      public int intValueOf(Person anObject) {
        return anObject.getNumberOfPets();
      }
    };
    long numberOfPets = people.sumOfInt(personNumberOfPets);
    assertThat(numberOfPets).isEqualTo(9);
  }

  @Test
  public void getAgesOfPets() {

    IntList sortedAges = people.asLazy()
                               .flatCollect(getPets)
                               .collectInt(getPetAge)
                               .toSortedList();

    IntSet uniqueAges = sortedAges.toSet();

    assertThat(sortedAges.allSatisfy(IntPredicates.greaterThan(0))).isTrue();
    assertThat(sortedAges.allSatisfy(positivePredicate)).isTrue();
    assertThat(sortedAges.allSatisfy(isZeroPredicate)).isFalse();
    assertThat(sortedAges.allSatisfy(negativePredicate)).isFalse();
    assertThat(uniqueAges).isEqualTo(IntHashSet.newSetWith(1, 2, 3, 4));
    assertThat(sortedAges.median()).isEqualTo(2.0, Offset.offset(0.0));

  }

  @Test
  public void getCountsByPetType() {
    Function<Pet, PetType> getPetType = new Function<Pet, PetType>() {
      @Override
      public PetType valueOf(Pet object) {
        return object.getType();
      }
    };
    Bag<PetType> counts = people.asLazy()
                                .flatCollect(getPets)
                                .collect(getPetType)
                                .toBag();
    assertThat(counts.occurrencesOf(PetType.CAT)).isEqualTo(2);
    assertThat(counts.occurrencesOf(PetType.DOG)).isEqualTo(2);
    assertThat(counts.occurrencesOf(PetType.HAMSTER)).isEqualTo(2);
    assertThat(counts.occurrencesOf(PetType.SNAKE)).isEqualTo(1);
    assertThat(counts.occurrencesOf(PetType.TURTLE)).isEqualTo(1);
    assertThat(counts.occurrencesOf(PetType.BIRD)).isEqualTo(1);
  }

  @Test
  public void getCountsByPetAge() {
    IntFunction<Pet> getPetAge = new IntFunction<Pet>() {
      @Override
      public int intValueOf(Pet anObject) {
        return anObject.getAge();
      }
    };
    IntBag counts = people.asLazy()
                          .flatCollect(getPets)
                          .collectInt(getPetAge)
                          .toBag();
    assertThat(counts.occurrencesOf(1)).isEqualTo(4);
    assertThat(counts.occurrencesOf(2)).isEqualTo(3);
    assertThat(counts.occurrencesOf(3)).isEqualTo(1);
    assertThat(counts.occurrencesOf(4)).isEqualTo(1);
    assertThat(counts.occurrencesOf(5)).isEqualTo(0);
  }

}
