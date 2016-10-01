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

package debop4k.data.orm.springdata.jpa21;


import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.EntityManager;

/**
 * User 관리를 위한 Repository 입니다.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

  /**
   * Explicitly mapped to named stored procedure {@code User.plus1} in the {@link EntityManager}
   */
  // FIXME : spring-data-jpa 1.9.0.RELEASE 에서는 아직 버그가 있다. (spring 예제 사이트에서도 예외가 발생한다.)
  @Procedure(name = "User.plus1")
  Integer plus1BackedByOtherNamedStoredProcedure(Integer arg);

  /**
   * 직접 Stored Procedure 와 같은 이름의 메소드를 정의합니다.
   */
  @Procedure
  Integer plus1inout(Integer arg);

}
