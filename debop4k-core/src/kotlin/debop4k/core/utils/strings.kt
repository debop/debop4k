/*
 * Copyright 2010-2015 sunghyouk.bae@gmail.com
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

@file:JvmName("strings")

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