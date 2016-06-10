/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package kesti4k.core.utils

import debop4k.core.utils.isNull
import debop4k.core.utils.nonEmpty
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.slf4j.LoggerFactory

class StringExTest {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Test
    fun testIsNull() {
        val a: String? = null
        assertThat(a.isNull()).isTrue()

        val b: String? = "non null"
        assertThat(b.isNull()).isFalse()

        log.debug("Success isNull test")
    }

    @Test
    fun testNonEmpty() {
        val a: String? = null
        assertThat(a.nonEmpty()).isFalse()

        val b: String? = "non null"
        assertThat(b.nonEmpty()).isTrue()

        log.debug("Success nonEmpty test")
    }
}