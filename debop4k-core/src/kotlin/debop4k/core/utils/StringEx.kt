/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */
@file:JvmName("StringEx")

package debop4k.core.utils

val UNIX_LINE_SEPARATOR = "\n"

public fun CharSequence.isEmpty(): Boolean {
  return trim().length == 0;
}

public fun CharSequence?.isNull(): Boolean = this == null

public fun CharSequence?.isNullOrEmpty(): Boolean =
    this == null || trim().length == 0

public fun CharSequence?.nonEmpty(): Boolean =
    this != null && trim().length > 0

val CharSequence.lastChar: Char
  get() = if (this.isEmpty()) 0.toChar() else this.get(length - 1)