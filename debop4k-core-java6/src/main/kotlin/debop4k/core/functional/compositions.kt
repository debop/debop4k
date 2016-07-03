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

@file:JvmName("composition")

package debop4k.core.functional

inline infix fun <A, B, C> Function1<A, B>.forwardCompose(crossinline f: (B) -> C): (A) -> C
    = { a -> f(this(a)) }

infix fun <A, B, C> Function1<A, B>.andThen(f: (B) -> C): (A) -> C = forwardCompose(f)

/**
 * 두 함수를 결합합니다.
 *  (::length).compose(::isOdd)
 */
inline infix fun <A, B, C> Function1<B, C>.compose(crossinline f: (A) -> B): (A) -> C
    = { a: A -> this(f(a)) }