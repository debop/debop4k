/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.basic

import debop4k.examples.AbstractExampleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class WhenExample : AbstractExampleTest() {

  @Test
  fun whenUsingType() {
    assertThat(eval(Sum(Num(1), Num(2)))).isEqualTo(3)
  }

  interface Expr
  class Num(val value: Int) : Expr
  class Sum(val left: Expr, val right: Expr) : Expr

  fun eval(e: Expr): Int {
    when (e) {
      is Num -> return e.value
      is Sum -> return eval(e.left) + eval(e.right)
      else -> throw IllegalArgumentException("Unknown Expression")
    }
  }
}

