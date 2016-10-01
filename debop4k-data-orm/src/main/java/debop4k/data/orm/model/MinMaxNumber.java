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

package debop4k.data.orm.model;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * 최대, 최소 값을 가지는 Component 입니다.
 *
 * @author sunghyouk.bae@gmail.com
 */
@Embeddable
@Getter
@Setter
public class MinMaxNumber<T extends Number> extends AbstractValueObject {

  private T min;
  private T max;

  public MinMaxNumber() {}

  public MinMaxNumber(T min, T max) {
    this.min = min;
    this.max = max;
  }


  @Override
  public int hashCode() {
    return Hashx.compute(min, max);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("min", min)
                .add("max", max);
  }

  private static final long serialVersionUID = 2146843068650706703L;
}
