package debop4k.examples.highorderfunctions

import io.kotlintest.specs.FunSpec

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
            println("plus item=$item")
            items += item
          }
        }
        println(items)
        return items
      }

      val list = listOf<Int>(1, 2, 3, 4).filter { i -> i > 3 }
      println(list)

      list shouldBe listOf<Int>(4)
    }

  }
}