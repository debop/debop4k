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

package debop4k.core.collections;

import debop4k.core.AbstractCoreTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.collections.impl.list.mutable.FastList.newListWith;


/**
 * BoundedStackTest
 *
 * @author sunghyouk.bae@gmail.com
 * @since 2015. 8. 14.
 */
@Slf4j
public class BoundedStackTest extends AbstractCoreTest {

  @Test
  public void emptyStack() {
    final BoundedStack<String> stack = new BoundedStack<String>(4);
    assertThat(stack.length()).isEqualTo(0);
    assertThat(stack.length()).isEqualTo(0);
    assertThat(stack.size()).isEqualTo(0);
    assertThat(stack.isEmpty()).isTrue();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void raiseIndexOutOfBoundsException() {
    final BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.get(0);
  }

  @Test(expected = NoSuchElementException.class)
  public void raiseNoSuchElementException() {
    final BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.pop();
  }


  @Test
  public void singleElement() {
    BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.add("a");
    assertThat(stack.size()).isEqualTo(1);
    assertThat(stack.get(0)).isEqualTo("a");
    assertThat(stack.toList()).isEqualTo(newListWith("a"));
  }

  @Test
  public void multipleElements() {
    BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.addAll(newListWith("a", "b", "c"));
    assertThat(stack.size()).isEqualTo(3);
    assertThat(stack.get(0)).isEqualTo("c");
    assertThat(stack.get(1)).isEqualTo("b");
    assertThat(stack.get(2)).isEqualTo("a");
    assertThat(stack.toList()).isEqualTo(newListWith("c", "b", "a"));

    assertThat(stack.pop()).isEqualTo("c");
    assertThat(stack.size()).isEqualTo(2);
    assertThat(stack.pop()).isEqualTo("b");
    assertThat(stack.size()).isEqualTo(1);
    assertThat(stack.pop()).isEqualTo("a");
    assertThat(stack.size()).isEqualTo(0);
  }

  @Test
  public void handleOverwrite() {
    BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.addAll(newListWith("a", "b", "c", "d", "e", "f"));
    assertThat(stack.size()).isEqualTo(4);
    assertThat(stack.get(0)).isEqualTo("f");
    assertThat(stack.toList()).isEqualTo(newListWith("f", "e", "d", "c"));
  }

  @Test
  public void handleUpdate() {
    BoundedStack<String> stack = new BoundedStack<String>(4);
    stack.addAll(newListWith("a", "b", "c", "d", "e", "f"));
    for (int i = 0; i < stack.length(); i++) {
      String old = stack.get(i);
      String updated = old + "2";
      stack.update(i, updated);
      assertThat(stack.get(i)).isEqualTo(updated);
    }
    assertThat(stack.toList()).isEqualTo(newListWith("f2", "e2", "d2", "c2"));
  }

  @Test
  public void insertAtZero() {
    BoundedStack<String> stack = new BoundedStack<String>(3);

    stack.insert(0, "a");
    assertThat(stack.size()).isEqualTo(1);
    assertThat(stack.get(0)).isEqualTo("a");

    stack.insert(0, "b");
    assertThat(stack.size()).isEqualTo(2);
    assertThat(stack.get(0)).isEqualTo("b");
    assertThat(stack.get(1)).isEqualTo("a");

    stack.insert(0, "c");
    assertThat(stack.size()).isEqualTo(3);
    assertThat(stack.get(0)).isEqualTo("c");
    assertThat(stack.get(1)).isEqualTo("b");
    assertThat(stack.get(2)).isEqualTo("a");

    stack.insert(0, "d");
    assertThat(stack.size()).isEqualTo(3);
    assertThat(stack.get(0)).isEqualTo("d");
    assertThat(stack.get(1)).isEqualTo("c");
    assertThat(stack.get(2)).isEqualTo("b");
  }

  @Test
  public void insertAtCountPushToBottom() {
    BoundedStack<String> stack = new BoundedStack<String>(3);

    stack.insert(0, "a");
    stack.insert(1, "b");
    stack.insert(2, "c");

    assertThat(stack.get(0)).isEqualTo("a");
    assertThat(stack.get(1)).isEqualTo("b");
    assertThat(stack.get(2)).isEqualTo("c");

    assertThat(stack.toList()).isEqualTo(newListWith("a", "b", "c"));
  }

  @Test
  public void insertGreaterThanCount() {
    final BoundedStack<String> stack = new BoundedStack<String>(1);
//    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
//      @Override
//      public void call() throws Throwable {
//        stack.insert(1, "a");
//        log.debug("insert 1, a");
//      }
//    }).isInstanceOf(IndexOutOfBoundsException.class);
//
//    stack.insert(0, "a");
//    log.debug("insert 0, a");
//
//    assertThatThrownBy(new ThrowableAssert.ThrowingCallable() {
//      @Override
//      public void call() throws Throwable {
//        stack.insert(2, "b");
//        log.debug("insert 2, b");
//      }
//    }).isInstanceOf(IndexOutOfBoundsException.class);
  }

}
