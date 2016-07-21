/*
 * Copyright (c) 2016. Sunghyouk Bae <sunghyouk.bae@gmail.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.core

val EmptyCharArray = CharArray(0)
val EmptyByteArray = ByteArray(0)
val EmptyIntArray = IntArray(0)
val EmptyLongArray = LongArray(0)
val EmptyDoubleArray = DoubleArray(0)

fun CharArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun ByteArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun IntArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun LongArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun FloatArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun DoubleArray?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

