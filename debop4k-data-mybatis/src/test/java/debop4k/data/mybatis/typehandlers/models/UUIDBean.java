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

package debop4k.data.mybatis.typehandlers.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UUIDBean extends AbstractValueObject {

  private UUID testId;
  private String name;
  private String password;

  @Override
  public int hashCode() {
    return Hashx.compute(testId);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("testId", testId)
                .add("name", name)
                .add("password", password);
  }

  private static final long serialVersionUID = -8729457926097487360L;
}
