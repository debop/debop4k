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

package debop4k.examples.operators

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.math.BigDecimal

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class OperatorExample : AbstractExampleTest() {

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
      0 -> x = value
      1 -> y = value
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
      0 -> leftTop.assign(value)
      1 -> rightBottom.assign(value)
      else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
  }

  // operator : in 에 대응된다.
  // p in r
  operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in leftTop.x until rightBottom.x &&
           p.y in rightBottom.y until leftTop.y
  }


  @Test fun `Operator overloading`() {
    val p1 = Point(1, 1)
    val p2 = Point(2, 2)

    assertThat(p1 + p2).isEqualTo(Point(3, 3))

    var p3 = p1
    p3 += p2
    assertThat(p3).isEqualTo(Point(3, 3))
  }

  @Test fun `Unary operator`() {
    assertThat(-Point(1, 1)).isEqualTo(Point(-1, -1))
  }

  @Test fun `BigDecimal extension`() {
    var b = BigDecimal.ZERO
    val b1 = b.inc()
    assertThat(b1).isEqualTo(BigDecimal.ONE)
  }

  @Test fun `access object with bracket`() {
    val p = MutablePoint(10, 20)
    p[1] = 42

    val p2 = MutablePoint(40, 0)

    val r = Rectangle(p, p2)

    r[0] = MutablePoint(0, 100)

    log.debug("r={}", r)
  }

  @Test fun `operator contains is 'in'`() {
    val rec = Rectangle(MutablePoint(10, 20), MutablePoint(50, 0))

    assertThat(Point(20, 10) in rec).isTrue()
    assertThat(Point(20, 20) in rec).isFalse()
    assertThat(Point(100, 10) in rec).isFalse()
  }

  @Test fun `destructuring declarations`() {
    val p = Point(10, 20)
    val (x, y) = p
    assertThat(x).isEqualTo(p.x).isEqualTo(p.component1())
    assertThat(y).isEqualTo(p.y).isEqualTo(p.component2())
  }

  @Test fun named_components() {
    data class NameComponents(val name: String, val extension: String)

    fun splitFilename(fullname: String): NameComponents {
      val (name, ext) = fullname.split(".", limit = 2)
      return NameComponents(name, ext)
    }

    val nc = splitFilename("file.txt")
    assertThat(nc.name).isEqualTo("file")
    assertThat(nc.extension).isEqualTo("txt")
  }

  @Test fun `destructing declarations in loops`() {
    fun printEntries(map: Map<String, String>) {
      for ((key, value) in map) {
        log.debug("$key -> $value")
      }
    }

    printEntries(mapOf("Sun" to "Java", "JetBrains" to "Kotlin"))
  }

}