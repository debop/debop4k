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
@file:JvmName("gettersetters")

package debop4k.core.functional

interface GetterOperation<K, V> {
  val getter: (K) -> V
  operator fun get(key: K): V = getter(key)
}

class GetterOperationImpl<K, V>(override val getter: (K) -> V) : GetterOperation<K, V>

interface SetterOperation<K, V> {
  val setter: (K, V) -> Unit
  operator fun set(key: K, value: V) {
    setter(key, value)
  }
}

class SetterOperationImpl<K, V>(override val setter: (K, V) -> Unit) : SetterOperation<K, V>

class GetterSetterOperation<K, V>(override val getter: (K) -> V,
                                  override val setter: (K, V) -> Unit) :
    GetterOperation<K, V>,
    SetterOperation<K, V>
