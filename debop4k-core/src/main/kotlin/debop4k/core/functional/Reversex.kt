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

@file:JvmName("Reversex")

package debop4k.core.functional

fun <P1, P2, R> Function2<P1, P2, R>.reverse(): (P2, P1) -> R {
  return { p2: P2, p1: P1 -> this(p1, p2) }
}

fun <P1, P2, P3, R> Function3<P1, P2, P3, R>.reverse(): (P3, P2, P1) -> R {
  return { p3: P3, p2: P2, p1: P1 -> this(p1, p2, p3) }
}

// TODO: 구현 필요