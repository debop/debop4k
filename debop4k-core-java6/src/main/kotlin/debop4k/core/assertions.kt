/*
 * Copyright 2016 Sunghyouk Bae<sunghyouk.bae@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package debop4k.core

inline fun <T> T.assertBy(assertion: (T) -> Unit): T {
  assertion(this)
  return this
}

inline fun <T> T.assertWith(assertWith: T.() -> Unit): T {
  this.assertWith()
  return this
}

fun Any?.shouldNotBeNull(name: String) {
  if (this == null)
    throw RuntimeException("$name should not be null")
}

fun Number.shouldBePositive(name: String) {
  if (this.toDouble() <= 0)
    throw IllegalArgumentException("$name should be positive number. number=$this")
}

fun String?.shouldNotBeNullOrEmpty(name: String) {
  if (this.isNullOrEmpty())
    throw IllegalArgumentException("$name should not be null or empty")
}

fun String?.shouldNotBeNullOrBlank(name: String) {
  if (this.isNullOrBlank())
    throw IllegalArgumentException("$name should not be null or blank")
}


fun String?.nullIfBlank(): String? = if (this.isNullOrBlank()) null else this
fun String?.nullIfEmpty(): String? = if (this.isNullOrEmpty()) null else this