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

@file:JvmName("composition")

package debop4k.core.functional

infix fun<T, K, R> Function1<T, K>.andThen(f: (K) -> R): (T) -> R = forwardCompose(f)

infix fun<T, K, R> Function1<T, K>.forwardCompose(f: (K) -> R): (T) -> R {
  return { t: T -> f(this(t)) }
}

infix fun<K, R, T> Function1<K, R>.compose(f: (T) -> K): (T) -> R {
  return { t: T -> this(f(t)) }
}