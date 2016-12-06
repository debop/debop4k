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
 */

package debop4k.data.orm.hibernate;

import debop4k.core.ToStringHelper;
import debop4k.data.AbstractNamedParameter;
import lombok.Getter;
import lombok.NonNull;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * Hibernate Query 에 사용하는 쿼리 파라미터를 표현합니다.
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 8.
 */
@Getter
public class HibernateParameter extends AbstractNamedParameter {

  /**
   * Static constructor
   *
   * @param name  parameter name
   * @param value parameter value
   * @return {@link HibernateParameter} 인스턴스
   */
  public static HibernateParameter of(@NonNull String name, Object value) {
    return of(name, value, StandardBasicTypes.SERIALIZABLE);
  }

  /**
   * Static constructor
   *
   * @param name      parameter name
   * @param value     parameter value
   * @param paramType 파라미터 수형
   * @return {@link HibernateParameter} 인스턴스
   */
  public static HibernateParameter of(@NonNull String name, Object value, @NonNull Type paramType) {
    return new HibernateParameter(name, value, paramType);
  }

  private final Type paramType;

//  /**
//   * default constructor
//   */
  //protected HibernateParameter() { this("parameter", null, null); }

  /**
   * Constructor
   *
   * @param name  parameter name
   * @param value parameter value
   */
  public HibernateParameter(@NonNull String name, Object value) {
    this(name, value, StandardBasicTypes.SERIALIZABLE);
  }

  /**
   * Constructor
   *
   * @param name      parameter name
   * @param value     parameter value
   * @param paramType 파라미터 수형
   */
  public HibernateParameter(@NonNull String name, Object value, @NonNull Type paramType) {
    super(name, value);
    this.paramType = paramType;
  }

  @Override
  protected ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("paramType", paramType);
  }

  private static final long serialVersionUID = -3718357247098634996L;
}
