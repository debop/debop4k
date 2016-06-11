/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.basic

import org.junit.Test

class ForExample {

  fun fizzBuzz(i: Int) = when {
    i % 5 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz "
    i % 5 == 0 -> "Buzz "
    else -> "$i "
  }

  @Test
  fun rangeForLoop() {
    for (i in 1..100) {
      print(fizzBuzz(i))
    }
  }

  @Test
  fun rangeForDownTo() {
    for (i in 100 downTo 1 step 2) {
      print(fizzBuzz(i))
    }
  }

}