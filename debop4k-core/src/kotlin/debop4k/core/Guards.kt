/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package debop4k.core

fun Any?.shouldNotBeNull(name: String) {
    if (this == null)
        throw RuntimeException("$name 값이 null 이 아니여야 합니다.")
}

fun String?.shouldNotBeNullOrEmpty(name: String) {
    if (this.isNullOrEmpty())
        throw IllegalArgumentException("$name should not be null or empty")
}