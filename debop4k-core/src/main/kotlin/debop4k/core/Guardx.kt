/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

@file:JvmName("Guardx")

package debop4k.core

fun <T> firstNotNull(first: T, second: T): T {
  if (!areEquals(first, null)) return first
  else if (!areEquals(second, null)) return second
  else throw IllegalArgumentException("all parameter is null.")
}

fun shouldBe(cond: Boolean): Unit = assert(cond)

inline fun shouldBe(cond: Boolean, msg: () -> Any): Unit = assert(cond, msg)

fun shouldBe(cond: Boolean, fmt: String, vararg args: Any): Unit {
  assert(cond) { fmt.format(*args) }
}

fun Any.shouldBeEquals(expected: Any, actualName: String): Unit {
  shouldBe(areEquals(this, expected), ShouldBeEquals, actualName, this, expected)
}

fun Any?.shouldBeNull(argName: String): Any? {
  shouldBe(this == null, ShouldBeNull, argName)
  return this
}

fun Any?.shouldNotBeNull(argName: String): Any? {
  shouldBe(this != null, ShouldNotBeNull, argName)
  return this
}

fun String?.shouldBeEmpty(argName: String): String? {
  shouldBe(this.isNullOrEmpty(), ShouldBeEmptyString, argName)
  return this
}

fun String?.shouldNotBeEmpty(argName: String): String? {
  shouldBe(!this.isNullOrEmpty(), ShouldBeEmptyString, argName)
  return this
}

fun String?.shouldBeWhitespace(argName: String): String? {
  shouldBe(this.isNullOrBlank(), ShouldBeWhiteSpace, argName)
  return this
}

fun String?.shouldNotBeWhitespace(argName: String): String? {
  shouldBe(!this.isNullOrBlank(), ShouldNotBeWhiteSpace, argName)
  return this
}

fun <T : Number> T.shouldBePositiveNumber(argName: String): T {
  shouldBe(this.toDouble() > 0.0, ShouldBePositiveNumber, argName)
  return this
}

fun <T : Number> T.shouldBePositiveOrZeroNumber(argName: String): T {
  shouldBe(this.toDouble() >= 0.0, ShouldBePositiveNumber, argName)
  return this
}

fun <T : Number> T.shouldBeNotPositiveNumber(argName: String): T {
  shouldBe(this.toDouble() <= 0.0, ShouldBePositiveNumber, argName)
  return this
}


fun <T : Number> T.shouldBeNegativeNumber(argName: String): T {
  shouldBe(this.toDouble() < 0.0, ShouldBePositiveNumber, argName)
  return this
}

fun <T : Number> T.shouldBeNegativeOrZeroNumber(argName: String): T {
  shouldBe(this.toDouble() <= 0.0, ShouldBePositiveNumber, argName)
  return this
}

fun <T : Number> T.shouldNotBeNegativeNumber(argName: String): T {
  shouldBe(this.toDouble() >= 0.0, ShouldBePositiveNumber, argName)
  return this
}

fun <T : Number> shouldBeInRange(value: T, fromInclude: T, toExclude: T, argName: String): Unit {
  val v = value.toDouble()
  shouldBe(v >= fromInclude.toDouble() && v < toExclude.toDouble(),
           ShouldBeInRangeInt, argName, value, fromInclude, toExclude)
}


fun Int.shouldBeInRange(range: IntProgression, argName: String): Unit {
  shouldBe(range.contains(this),
           ShouldBeInRangeInt, argName, this, range.first, range.last)
}

fun Long.shouldBeInRange(range: LongProgression, argName: String): Unit {
  shouldBe(range.contains(this),
           ShouldBeInRangeInt, argName, this, range.first, range.last)
}

fun <T : Number> T.shouldBeBetween(fromInclude: T, toInclude: T, argName: String): Unit {
  val v = this.toDouble()
  shouldBe(v >= fromInclude.toDouble() && v <= toInclude.toDouble(),
           ShouldBeInRangeDouble, argName, this, fromInclude, toInclude)
}

fun Int.shouldBeBetween(range: IntProgression, argName: String): Unit {
  shouldBe(range.contains(this),
           ShouldBeInRangeInt, argName, this, range.first, range.last)
}

fun Long.shouldBeBetween(range: LongProgression, argName: String): Unit {
  shouldBe(range.contains(this),
           ShouldBeInRangeInt, argName, this, range.first, range.last)
}