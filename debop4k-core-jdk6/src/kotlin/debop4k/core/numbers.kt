/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core

import java.text.DecimalFormat

fun Int.min(other: Int = Int.MAX_VALUE): Int = Math.min(this, other)
fun Int.max(other: Int = Int.MIN_VALUE): Int = Math.max(this, other)
fun Int.coerce(min: Int, max: Int): Int = this.min(min).max(max)
fun Int.coerce(range: IntRange): Int = this.coerce(range.start, range.endInclusive)


fun Long.min(other: Long = Long.MAX_VALUE): Long = Math.min(this, other)
fun Long.max(other: Long = Long.MIN_VALUE): Long = Math.max(this, other)
fun Long.coerce(min: Long, max: Long): Long = this.min(min).max(max)
fun Long.coerce(range: LongRange): Long = this.coerce(range.start, range.endInclusive)

fun Byte.min(other: Byte = Byte.MAX_VALUE): Byte = if (this < other) this else other
fun Byte.max(other: Byte = Byte.MIN_VALUE): Byte = if (this > other) this else other
fun Byte.coerce(min: Byte, max: Byte): Byte = this.min(min).max(max)
fun Byte.coerce(range: IntRange): Byte = this.coerce(range.start.toByte(), range.endInclusive.toByte())


fun Short.min(other: Short = Short.MAX_VALUE): Short = if (this < other) this else other
fun Short.max(other: Short = Short.MIN_VALUE): Short = if (this > other) this else other
fun Short.coerce(min: Short, max: Short): Short = this.min(min).max(max)
fun Short.coerce(range: IntRange): Short = this.coerce(range.start.toShort(), range.endInclusive.toShort())

fun Float.min(other: Float = Float.MAX_VALUE): Float = Math.min(this, other)
fun Float.max(other: Float = Float.MIN_VALUE): Float = Math.max(this, other)
fun Float.coerce(min: Float, max: Float): Float = this.min(min).max(max)

fun Double.min(other: Double = Double.MAX_VALUE): Double = Math.min(this, other)
fun Double.max(other: Double = Double.MIN_VALUE): Double = Math.max(this, other)
fun Double.coerce(min: Double, max: Double): Double = this.min(min).max(max)


val DEFAULT_DECIMAL_FORMAT = DecimalFormat("#,##0.#")
/**
 * 숫자를 인간이 보기 편하도록 보여준다
 */
fun Number.toHuman(): String = DEFAULT_DECIMAL_FORMAT.format(this.toDouble())



