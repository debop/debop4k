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

package debop4k.data.orm.mapping.compositeid.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.AbstractPersistentObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Composite Id 정의 방식 중, 엔티티에 여러개의 Id 를 정의하는 방식입니다.
 * 단 @IdClass 를 지정해 줘야 합니다.
 */
@Entity(name = "CompositeId_IdClassCar")
@IdClass(CarIdentifier.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IdClassCar extends AbstractPersistentObject {

  public IdClassCar(CarIdentifier identifier) {
    this.brand = identifier.getBrand();
    this.year = identifier.getYear();
  }

  public IdClassCar(String brand, int year) {
    this.brand = brand;
    this.year = year;
  }

  @Id
  private String brand;

  @Id
  private int year;

  private String serialNo;

  @Override
  public int hashCode() {
    return Hashx.compute(brand, year);
  }

  private static final long serialVersionUID = -7751706872287407407L;
}
