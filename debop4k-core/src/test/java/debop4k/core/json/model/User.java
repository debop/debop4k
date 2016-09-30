/*
 * Copyright 2015-2020 KESTI s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.json.model;

import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.collections.Arrayx;
import debop4k.core.utils.Hashx;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractValueObject implements Comparable<User> {

  private String firstName;
  private String lastName;
  private String addressStr;
  private String city;
  private String state;
  private String zipCode;
  private String email;
  private String username;
  private String password;

  private int age = 0;
  private DateTime updateTime = DateTime.now();

  private byte[] byteArray = Arrayx.getRandomBytes(1024);

  private Address homeAddr = new Address(null, null);
  private Address officeAddr = new Address(null, null);

  private List<String> favoriteMovies = new ArrayList<String>(); //FastList.newList();

  private static final long serialVersionUID = 3882148036190361851L;

  @Override
  public int compareTo(User o) {
    return firstName.compareTo(o.firstName);
  }

  @Override
  public int hashCode() {
    return Hashx.compute(firstName, lastName);
  }

  @Override
  public ToStringHelper buildStringHelper() {
    return super.buildStringHelper()
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("addressStr", addressStr);
  }

  public static User newUser(int favoriteMovieSize) {
    User user = new User();

    user.firstName = "성혁";
    user.lastName = "배";
    user.addressStr = "정릉1동 현대홈타운 107동 301호";
    user.city = "서울";
    user.state = "서울";
    user.email = "sunghyouk.bae@gmail.com";
    user.username = "debop";
    // user.password = "1234"
    user.homeAddr = new Address("정릉1동 현대홈타운 107동 301호", "555-5555");
    user.homeAddr.getProperties().addAll(Arrays.asList("home", "addr"));

    user.officeAddr = new Address("운니동 삼환빌딩 10F", "555-5555");
    user.officeAddr.getProperties().addAll(Arrays.asList("office", "addr"));

    int x = 0;
    while (x < favoriteMovieSize) {
      user.favoriteMovies.add("Favorite Movie number-" + x++);
    }

    return user;
  }
}
