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

package debop4k.data.orm.mapping.associations.manytoone.models;

import debop4k.core.utils.Hashx;
import debop4k.data.orm.model.IntEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "ManyToOne_JugMeter")
@Getter
@Setter
@NoArgsConstructor
public class JugMeter extends IntEntity {

  public JugMeter(String name) {
    this.name = name;
  }

  private String name;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "memberOf", nullable = false)
  @LazyToOne(LazyToOneOption.PROXY)
  private Jug memberOf;

  @Override
  public int hashCode() {
    return Hashx.compute(name);
  }

  private static final long serialVersionUID = 4318476267492089400L;
}
