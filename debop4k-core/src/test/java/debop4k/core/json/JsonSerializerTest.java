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

package debop4k.core.json;

import debop4k.core.AbstractCoreTest;
import debop4k.core.json.model.Professor;
import debop4k.core.json.model.Student;
import debop4k.core.json.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JacksonSerializerTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 15.
 */
@Slf4j
public class JsonSerializerTest extends AbstractCoreTest {

  static List<JsonSerializer> serializers =
      Arrays.asList(new GsonSerializer(),
                    new JacksonSerializer());
  // new MessagePackSerializer());

  @Test
  public void serializeValueObject() {
    Professor prof = new Professor("professor", 50, "engineering");
    Student bob = new Student("bob", 22, "Freshman");

    for (JsonSerializer serializer : serializers) {
      byte[] bytes = serializer.toByteArray(prof);
      Professor copiedProf = serializer.fromByteArray(bytes, Professor.class);

      assertThat(copiedProf).isNotNull();
      assertThat(copiedProf).isEqualTo(prof);

      bytes = serializer.toByteArray(bob);
      Student copiedStudent = serializer.fromByteArray(bytes, Student.class);
      assertThat(copiedStudent).isNotNull();
      assertThat(copiedStudent).isEqualTo(bob);
    }
  }

  @Test
  public void nestedClass() {
    User user = User.newUser(10);
    for (JsonSerializer serializer : serializers) {
      String text = serializer.toString(user);
      User copied = serializer.fromString(text, User.class);

      assertThat(copied).isEqualTo(user);
    }
  }
}
