package debop4k.examples.highorderfunctions

import io.kotlintest.matchers.be
import io.kotlintest.specs.FunSpec
import java.io.BufferedReader
import java.io.FileReader
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * @author sunghyouk.bae@gmail.com
 */
class HighOrderFunctionExample : FunSpec() {

  val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
  val action: () -> Unit = { println(42) }


  init {

    test("Calling Functions passed as argument") {

      fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): MutableList<T> {
        val items = mutableListOf<T>()

        this.forEach { item ->
          println("item=$item")
          if (predicate(item)) {
            items.add(item)
          }
        }
        return items
      }

      val list = listOf<Int>(1, 2, 3, 4).filter { i -> i > 3 }
      println(list)

      list shouldBe listOf<Int>(4)
    }

    test("Returning Function") {
      val contacts = listOf(Person("Sunghyouk", "Bae", "123-4567"),
                            Person("Misook", "Kwon", null))

      with(ContactListFilters) {
        prefix = "B"
        onlyWithPhoneNumber = true
      }
      contacts.filter(ContactListFilters.getPredicate()) shouldBe listOf(Person("Sunghyouk", "Bae", "123-4567"))
    }

    test("inline functions : removing the overhead of lambda") {

      val lock = ReentrantLock()
      synchronized(lock) {
        println("synchronized")
      }

    }

    test("Lambda : Design Pattern for Resources") {
      val lines = BufferedReader(FileReader("../README.md")).use { it.readLines() }
      lines should be != null
      lines.size should be gt 0
      println(lines)
    }

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