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

package debop4k.data.orm.jpa;

import debop4k.core.ToStringHelper;
import debop4k.data.AbstractNamedParameter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * JPQL 의 Parameter 를 표현한 클래스입니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 8.
 */
@Getter
@Setter
public class JapParameter extends AbstractNamedParameter {

  private final Type paramType;

  public JapParameter(String name, Object value) {
    this(name, value, StandardBasicTypes.SERIALIZABLE);
  }

  public JapParameter(String name, Object value, Type paramType) {
    super(name, value);
    this.paramType = paramType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("paramType", paramType);
  }

  private static final long serialVersionUID = -921048328741530228L;
}
