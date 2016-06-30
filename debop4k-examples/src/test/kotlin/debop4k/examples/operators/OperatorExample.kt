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

  data class MutablePoint(var x: Int, var y: Int)

  operator fun MutablePoint.set(index: Int, value: Int) {
    when (index) {
      0    -> x = value
      1    -> y = value
      else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
  }

  fun MutablePoint.assign(value: MutablePoint) {
    this.x = value.x
    this.y = value.y
  }

  data class Rectangle(val leftTop: MutablePoint, val rightBottom: MutablePoint)

  operator fun Rectangle.set(index: Int, value: MutablePoint) {
    when (index) {
      0    -> leftTop.assign(value)
      1    -> rightBottom.assign(value)
      else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
  }

  // operator : in 에 대응된다.
  // p in r
  operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in leftTop.x until rightBottom.x &&
           p.y in rightBottom.y until leftTop.y
  }

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

    test("access object with bracket") {
      val p = MutablePoint(10, 20)
      p[1] = 42

      val p2 = MutablePoint(40, 0)

      val r = Rectangle(p, p2)

      r[0] = MutablePoint(0, 100)

      println(r)
    }

    test("operator contains is 'in' ") {
      val rec = Rectangle(MutablePoint(10, 20), MutablePoint(50, 0))

      (Point(20, 10) in rec) shouldBe true
      (Point(20, 20) in rec) shouldBe false
      (Point(100, 10) in rec) shouldBe false
    }

    test("destructuring declarations") {
      val p = Point(10, 20)
      val (x, y) = p
      x shouldBe p.x
      y shouldBe p.y

      x shouldBe p.component1()
      y shouldBe p.component2()
    }

    test("named components") {
      data class NameComponents(val name: String, val extension: String)

      fun splitFilename(fullname: String): NameComponents {
        val (name, ext) = fullname.split(".", limit = 2)
        return NameComponents(name, ext)
      }

      val nc = splitFilename("file.txt")
      nc.name shouldBe "file"
      nc.extension shouldBe "txt"
    }

    test("destructing declarations in loops") {
      fun printEntries(map: Map<String, String>) {
        for ((key, value) in map) {
          println("$key -> $value")
        }
      }

      printEntries(mapOf("Sun" to "Java", "JetBrains" to "Kotlin"))
    }
  }
}