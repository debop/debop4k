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

@file:JvmName("currying")

package debop4k.core.functional


fun<T1, T2, R> Function2<T1, T2, R>.curried(): (T1) -> (T2) -> R {
  return { t1: T1 -> { t2: T2 -> this(t1, t2) } }
}

fun<T1, T2, T3, R> Function3<T1, T2, T3, R>.curried(): (T1) -> (T2) -> (T3) -> R {
  return { t1: T1 -> { t2: T2 -> { t3: T3 -> this(t1, t2, t3) } } }
}

fun<T1, T2, T3, T4, R> Function4<T1, T2, T3, T4, R>.curried(): (T1) -> (T2) -> (T3) -> (T4) -> R {
  return { t1: T1 -> { t2: T2 -> { t3: T3 -> { t4: T4 -> this(t1, t2, t3, t4) } } } }
}

fun<T1, T2, T3, T4, T5, R> Function5<T1, T2, T3, T4, T5, R>.curried(): (T1) -> (T2) -> (T3) -> (T4) -> (T5) -> R {
  return { t1: T1 -> { t2: T2 -> { t3: T3 -> { t4: T4 -> { t5: T5 -> this(t1, t2, t3, t4, t5) } } } } }
}

// TODO: 구현 필요