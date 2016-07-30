/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
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

package debop4k.examples.highorderfunctions

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.BufferedReader
import java.io.FileReader
import java.util.concurrent.locks.*

/**
 * @author sunghyouk.bae@gmail.com
 */
class HighOrderFunctionExample : AbstractExampleTest() {

  val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
  val action: () -> Unit = { println(42) }


  @Test fun `Calling Functions passed as argument`() {

      fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): MutableList<T> {
        val items = mutableListOf<T>()

        this.forEach { item ->
          log.debug("item={}", item)
          if (predicate(item)) {
            items.add(item)
          }
        }
        return items
      }

      val list = listOf<Int>(1, 2, 3, 4).filter { i -> i > 3 }
    log.debug("list={}", list)

    assertThat(list).isEqualTo(listOf<Int>(4))
    }

  @Test fun `Returning Function`() {
      val contacts = listOf(Person("Sunghyouk", "Bae", "123-4567"),
                            Person("Misook", "Kwon", null))

      with(ContactListFilters) {
        prefix = "B"
        onlyWithPhoneNumber = true
      }
    val filtered = contacts.filter(ContactListFilters.getPredicate())
    assertThat(filtered).isEqualTo(listOf(Person("Sunghyouk", "Bae", "123-4567")))
    }

  @Test fun `inline functions - removing the overhead of lambda`() {
      val lock = ReentrantLock()
      synchronized(lock) {
        println("synchronized")
      }
    }

  @Test fun `Lambda - Design Pattern for Resources`() {
      val lines = BufferedReader(FileReader("../README.md")).use { it.readLines() }
    assertThat(lines).isNotNull()
    assertThat(lines.size).isGreaterThan(0)
    log.debug("lines={}", lines)
    }
}

data class Person(val firstName: String, val lastName: String, val phoneNumber: String?)

object ContactListFilters {
  var prefix: String = ""
  var onlyWithPhoneNumber: Boolean = false

  fun getPredicate(): (Person) -> Boolean {
    val startsWithPrefix = { p: Person ->
      p.firstName.startsWith(prefix) || p.lastName.startsWith(prefix)
    }
    if (!onlyWithPhoneNumber) {
      return startsWithPrefix
    }
    return { startsWithPrefix(it) && it.phoneNumber != null }
  }
}

// inline 은 lambda 식을 매번 생성하지 않고, 컴파일 해서 쓴다. 매번 생성할 때보다 성능이 좋다.
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
  lock.lock()
  try {
    return action()
  } finally {
    lock.unlock()
  }
}