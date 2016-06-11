/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.examples.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.slf4j.LoggerFactory

class InCheckExample {

  val log = LoggerFactory.getLogger(this.javaClass)

  fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
  fun isNotDigit(c: Char) = c !in '0'..'9'

  fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "$c is a digit"
    in 'a'..'z', in 'A'..'Z' -> "$c is a letter"
    else -> "I don't know ... $c"
  }

  @Test
  fun inCheck() {
    assertThat(isLetter('q')).isTrue();
    assertThat(isLetter('?')).isFalse();

    assertThat(isNotDigit('a')).isTrue();
    assertThat(isNotDigit('0')).isFalse();

    assertThat(recognize('5')).contains("digit")
    assertThat(recognize('a')).contains("letter")
    assertThat(recognize('@')).contains("don't")
  }
}