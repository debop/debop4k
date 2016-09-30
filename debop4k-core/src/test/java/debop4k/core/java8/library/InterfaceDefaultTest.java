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

package debop4k.core.java8.library;

import lombok.Data;
import org.junit.Test;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author sunghyouk.bae@gmail.com
 */
public class InterfaceDefaultTest {

  @Test
  public void interfaceDefaultMethods() {
    Stream.of(asList(1, 2, 3, 4)).forEach(System.out::println);
  }

  public interface Parent {
    public void setMessage(String body);

    public default void welcome() {
      setMessage("Parent: Hi!");
    }

    public String getMessage();
  }

  @Data
  public class ParentImpl implements Parent {
    // NOTE: Class 는 Interface의 default method 를 override 하지 않아도 됩니다.
    private String message;
  }

  @Test
  public void parentDefaultUsed() {
    Parent parent = new ParentImpl();
    parent.welcome();
    assertEquals("Parent: Hi!", parent.getMessage());
  }

  public interface Child extends Parent {
    // NOTE: Interface 는 상속 시 default method 를 override 해줘야 합니다.
    @Override
    public default void welcome() {
      setMessage("Child: Hi!");
    }
  }

  @Data
  public class ChildImpl implements Child {
    private String message;
  }

  @Test
  public void childOverrideDefault() {
    Child child = new ChildImpl();
    child.welcome();
    assertEquals("Child: Hi!", child.getMessage());
  }

  public class OverridingParent extends ParentImpl {
    @Override
    public void welcome() {
      setMessage("Class Parent: Hi!");
    }
  }

  @Test
  public void concreteBeatsDefault() {
    Parent parent = new OverridingParent();
    parent.welcome();
    assertEquals("Class Parent: Hi!", parent.getMessage());
  }

  public class OverridingChild extends ChildImpl {
    @Override
    public void welcome() {
      setMessage("Class Child: Hi!");
    }
  }

  @Test
  public void concreteBeatsCloserDefault() {
    Child child = new OverridingChild();
    child.welcome();
    assertEquals("Class Child: Hi!", child.getMessage());
  }

  // Multiple inheritance

  public interface JukeBox {
    public default String rock() {
      return "... all over the world";
    }
  }

  public interface Carriage {
    public default String rock() {
      return "... from side to side";
    }
  }

  public class MusicalCarriage implements JukeBox, Carriage {
    @Override
    public String rock() {
      // 중복된 메소드에 대해서 직접 정한다.
      return Carriage.super.rock();
    }
  }

  @Test
  public void multipleInheritance() {
    JukeBox jukeBox = new MusicalCarriage();
    assertEquals("... from side to side", jukeBox.rock());
  }
}
