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

@file:JvmName("partials")

package debop4k.core.functional

class Partial<T>

fun <T> partial(): Partial<T> = Partial()

operator fun<P1, P2, R> Function2<P1, P2, R>.invoke(p1: P1, partial2: Partial<P2> = partial()): (P2) -> R {
  return { p2: P2 -> this(p1, p2) }
}

operator fun<P1, P2, R> Function2<P1, P2, R>.invoke(partial1: Partial<P1> = partial(), p2: P2): (P1) -> R {
  return { p1: P1 -> this(p1, p2) }
}