/*
 * Copyright (c) 2016. KESTI co, ltd
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

@file:JvmName("function")

package debop4k.core.utils

/**
 * 단순 함수를 실행합니다.
 */
fun <T> (() -> T).exec(): T = this.invoke()

@FunctionalInterface
interface Action1<in T> {
  fun accept(arg: T): Unit
}

@FunctionalInterface
interface Function0<out T> {
  fun get(): T
}

fun <T> Action(func: (T) -> Unit): Action1<T> {
  return object : Action1<T> {
    override fun accept(arg: T) {
      func(arg)
    }
  }
}

fun <T> Supplier(action: () -> T): Function0<T> {
  return object : Function0<T> {
    override fun get(): T {
      return action()
    }
  }
}
