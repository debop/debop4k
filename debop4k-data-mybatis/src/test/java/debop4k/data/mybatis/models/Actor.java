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

package debop4k.data.mybatis.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class Actor extends AbstractValueObject {

  private Integer id;

  @NotNull(message = "성은 필수입니다.")
  @NotBlank(message = "성은 필수입니다.")
  private String firstname;

  @NotNull
  private String lastname;

  @Override
  public int hashCode() {
    return id != null ? Hashx.compute(id) : Hashx.compute(firstname, lastname);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("id", id)
                .add("firstname", firstname)
                .add("lastname", lastname);
  }

  private static final long serialVersionUID = -3112188052141954357L;
}
