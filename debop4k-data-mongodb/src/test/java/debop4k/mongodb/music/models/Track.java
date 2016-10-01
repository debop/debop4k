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

package debop4k.mongodb.music.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Getter
@Setter
@NoArgsConstructor
public class Track extends AbstractValueObject {

  private int number;
  private String name;
  private Stars rating = Stars.ZERO;

  public Track(int number, String name) {
    this.number = number;
    this.name = name;
  }

  @Override
  public int hashCode() {
    return Hashx.compute(number, name);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  private static final long serialVersionUID = 6417972798048299170L;
}
