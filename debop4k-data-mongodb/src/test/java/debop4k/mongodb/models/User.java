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

package debop4k.mongodb.models;

import debop4k.core.AbstractValueObject;
import debop4k.core.collections.Arrayx;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.joda.time.DateTime;

import java.util.Arrays;
import java.util.List;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Getter
@Setter
public class User extends AbstractValueObject implements Comparable<User> {

  public static User of() {
    return of(100);
  }

  @SneakyThrows(Exception.class)
  public static User of(int favoriteMovieSize) {
    User user = new User();
    user.firstName = "성혁";
    user.lastName = "배";
    user.addressStr = "정릉1동 현대홈타운 107동 301호";
    user.city = "서울";
    user.state = "서울";
    user.email = "sunghyouk.bae@gmail.com";
    user.username = "debop";

    user.homeAddress = new Address("정릉1동 현대홈타운 107동 301호", "555-5555");
    user.homeAddress.getProperties().addAll(Arrays.asList("home", "addr"));

    user.officeAddress = new Address("부산 센텀시티 큐비e센텀 24층", "555-5555");
    user.officeAddress.getProperties().addAll(Arrays.asList("office", "addr"));

    // 객체 생성 비용을 늘리기 위해
    Thread.sleep(100);

    for (int i = 0; i < favoriteMovieSize; i++) {
      user.favoriteMovies.add("Favorite Movie number-" + i);
    }

    return user;
  }

  private String id;
  private String firstName;
  private String lastName;
  private String addressStr;
  private String city;
  private String state;
  private String zipcode;
  private String email;
  private String username;
  private String password;

  private int age = 0;
  private DateTime updateTime = DateTime.now();

  byte[] byteArray = Arrayx.getRandomBytes(1024);

  Address homeAddress = new Address(null, null);
  Address officeAddress = new Address(null, null);

  List<String> favoriteMovies = FastList.newList();

  @Override
  public int compareTo(User o) {
    return firstName.compareTo(o.firstName);
  }

  @Override
  public int hashCode() {
    return Hashx.compute(id, firstName, lastName, age);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  private static final long serialVersionUID = 4457153208569468072L;
}
