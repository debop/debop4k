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

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.bag.mutable.HashBag;
import org.eclipse.collections.impl.block.factory.Predicates2;
import org.eclipse.collections.impl.list.mutable.FastList;

@Data
@RequiredArgsConstructor
public class Person {
  private final String firstName;
  private final String lastName;
  private final MutableList<Pet> pets = FastList.newList();

  private Function<Pet, PetType> getPetType = new Function<Pet, PetType>() {
    @Override
    public PetType valueOf(Pet object) {
      return object.getType();
    }
  };

  public boolean named(String name) {
    return (getFirstName() + " " + getLastName()).equals(name);
  }

  public boolean hasPet(PetType petType) {
    return this.pets.anySatisfyWith(Predicates2.attributeEqual(getPetType), petType);
  }

  public MutableBag<PetType> getPetTypes() {
    return this.pets.collect(getPetType, HashBag.<PetType>newBag());
  }

  public Person addPet(PetType petType, String name, int age) {
    this.pets.add(new Pet(petType, name, age));
    return this;
  }

  public int getNumberOfPets() {
    return this.pets.size();
  }
}