/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.units

import io.kotlintest.specs.FunSpec

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class MassFunTest : FunSpec() {

  init {

    test("gram to gram") {

      5.0.gram().gram shouldBe (5.0 plusOrMinus 1.0e-8)
      Double.POSITIVE_INFINITY.gram().gram shouldBe Double.POSITIVE_INFINITY
      Mass.POSITIVE_INF.gram shouldBe Double.POSITIVE_INFINITY
    }
  }
}