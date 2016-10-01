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

package debop4k.mongodb.springdata.model;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.regex.Pattern;

@Getter
public class EmailAddress extends AbstractValueObject {

  static String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  static Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

  @Field("email")
  private String value;

  public EmailAddress() {}

  public EmailAddress(String emailAddr) {
    this.value = emailAddr;
  }

  @Override
  public int hashCode() {
    return Hashx.compute(value);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("value", value);
  }

  private static final long serialVersionUID = -7422259085665307751L;
}
