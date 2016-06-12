/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.operators

import io.kotlintest.specs.FunSpec
import java.math.BigDecimal

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class OperatorExample : FunSpec() {

  override val oneInstancePerTest: Boolean = true

  data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y)
    operator fun unaryMinus(): Point = Point(-x, -y)
    operator fun inc(): Point = Point(x + 1, y + 1)
    operator fun dec(): Point = Point(x - 1, y - 1)
  }

  fun BigDecimal.inc(): BigDecimal = this.add(BigDecimal.ONE)

  init {
    test("Operator overloading") {
      val p1 = Point(1, 1)
      val p2 = Point(2, 2)

      p1 + p2 shouldBe Point(3, 3)

      var p3 = p1
      p3 += p2
      p3 shouldBe Point(3, 3)
    }

    test("Unary operator") {
      -Point(1, 1) shouldBe Point(-1, -1)
    }

    test("BigDecimal extension") {
      var b = BigDecimal.ZERO
      val b1 = b.inc()
      b1 shouldBe BigDecimal.ONE
    }
  }
}