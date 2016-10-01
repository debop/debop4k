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

package debop4k.data.orm.springdata.examples.simple;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 보통 {@link JpaRepository} 로부터 상속 받으면 된다.
 * 인터페이스이므로, Spring Data Jpa 에서 자동으로 Concrete class 를 생성해준다.
 */
public interface SimpleUserRepository extends CrudRepository<User, Integer> {

  /**
   * username 으로 사용자 찾기
   */
  User findByUsername(String username);

  /**
   * Lastname 으로 사용자 찾기
   */
  List<User> findByLastname(String lastname);

  /**
   * 파라미터의 정보를 이용하여 SpringEL 을 통해 JPQL 문으로 질의를 수행합니다.
   * HINT: 사용하고자 하는 paremeter에는 @Param 을 지정해야 합니다.
   */
  @Query("select u from User u where u.firstname = :firstname")
  List<User> findByFirstname(@Param("firstname") String firstname);

  /**
   * 파라미터의 정보를 이용하여 SpringEL 을 통해 JPQL 문으로 질의를 수행합니다.
   * HINT: 사용하고자 하는 paremeter에는 @Param 을 지정해야 합니다.
   */
  @Query("select u from User u where u.firstname = :name or u.lastname = :name")
  List<User> findByFirstnameOrLastname(@Param("name") String name);

  /**
   * lastname 이 같은 User 들을 모두 삭제합니다.
   */
  Long removeByLastname(String lastname);

  /**
   * 페이징 처리를 수행한 사용자 목록
   */
  Slice<User> findByLastnameOrderByUsernameAsc(String lastname, Pageable page);

  /**
   * lastname 으로 오름차순 정렬을 수행하여 Top 2 만 반환한다.
   */
  List<User> findFirst2ByOrderByLastnameAsc();

  /**
   * 지정한 정렬 방식으로 정렬한 후 Top 2 의 레코드를 반환한다.
   */
  List<User> findTop2By(Sort sort);

  /**
   * Spring EL 을 이용하여 JPQL 을 빌드합니다.
   * HINT: 사용하고자 하는 paremeter에는 @Param 을 지정해야 합니다.
   */
  @Query("select u from User u where u.firstname = :#{#user.firstname} or u.lastname = :#{#user.lastname}")
  Iterable<User> findByFirstnameOrLastname(@Param("user") User user);
}
