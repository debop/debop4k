/*
 * Copyright (c) 2016. KESTI co, ltd
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core.examples;

import debop4k.core.AbstractCoreTest;
import debop4k.core.AbstractValueObject;
import debop4k.core.ToStringHelper;
import debop4k.core.utils.Hashx;
import debop4k.core.utils.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author sunghyouk.bae@gmail.com
 */
@Slf4j
public class EquivalenceTest extends AbstractCoreTest {

  @Test
  public void testStringEquals() {
    String a = "Hello";
    String b = "hello";

    assertThat(a == b).isFalse();
    assertThat(a.toLowerCase() == b).isFalse();

    assertThat(a.equals(b)).isFalse();
    assertThat(a.toLowerCase().equals(b)).isTrue();

    assertThat(equalsIgnoreCase(a, b)).isTrue();

  }

  public static boolean equalsIgnoreCase(String a, String b) {
    return Objects.equals(a.toLowerCase(), b.toLowerCase());
  }

  static class User extends AbstractValueObject {
    public User(String name, int age, String gender) {
      this.name = name;
      this.age = age;
      this.gender = gender;
    }

    private String name;    // 이름
    private int age;        // 나이
    private String gender;  // 성별

    @Override
    public boolean equals(Object obj) {
      return obj != null && obj instanceof User && hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, age, gender);
    }

    @NotNull
    @Override
    protected ToStringHelper buildStringHelper() {
      return super.buildStringHelper()
                  .add("name", name)
                  .add("age", age)
                  .add("gender", gender);
    }
  }

  @Test
  public void testUsersEqual() {
    User dooli = new User("둘리", 100, "M");
    User doner = new User("도우너", 50, "F");
    User dooli2 = new User("둘리", 100, "M");

    log.debug("dooli={}, dooli2={}", dooli.hashCode(), dooli2.hashCode());
    log.debug("doll1={}, dooli2={}", dooli.toString(), dooli2.toString());

    assertThat(dooli.equals(doner)).isFalse();
    assertThat(dooli.equals(dooli2)).isTrue();
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  static class UserVO extends AbstractValueObject {

    private static final long serialVersionUID = -8753490986336472401L;

    public UserVO(String name, int age, String gender) {
      this.name = name;
      this.age = age;
      this.gender = gender;
    }

    private String name;    // 이름
    private int age;        // 나이
    private String gender;  // 성별
    private String address;

    @Override
    public boolean equals(@Nullable Object other) {
      return (other != null) && (other.getClass() == getClass()) && (other.hashCode() == hashCode());
    }

    @Override
    public int hashCode() {
      return Hashx.compute(name, age, gender);
    }
  }
}