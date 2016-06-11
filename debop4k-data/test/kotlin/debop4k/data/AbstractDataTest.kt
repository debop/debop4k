/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.data

import org.junit.Test
import org.slf4j.LoggerFactory

/**
 * @author debop sunghyouk.bae@gmail.com
 */
abstract class AbstractDataTest {
  private val log = LoggerFactory.getLogger(this.javaClass)

  @Test
  fun test() {
    log?.debug("Message")
  }
}