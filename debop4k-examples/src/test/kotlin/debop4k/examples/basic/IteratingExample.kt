/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.basic

import org.junit.Test
import org.slf4j.LoggerFactory
import java.util.*

class IteratingExample {

  val log = LoggerFactory.getLogger(this.javaClass)

  @Test
  fun iteratingMap() {
    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
      val binary = Integer.toBinaryString(c.toInt())
      binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
      log.debug("$letter = $binary")
    }
  }

  @Test
  fun iteratingWithIndex() {
    val list = arrayListOf("10", "11", "1001")

    for ((index, element) in list.withIndex()) {
      log.debug("$index: $element")
    }
  }

}