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

@file:JvmName("memorization")

package debop4k.core.functional

import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap


private interface MemorizedCall<F, R> {
  operator fun invoke(f: F): R
}

private data class MemorizeKey1<P1, R>(val p1: P1) : MemorizedCall<(P1) -> R, R> {
  override fun invoke(f: (P1) -> R): R = f(p1)
}

private class MemorizedHandler<F, K : MemorizedCall<F, R>, R>(val f: F) {
  private val m = ConcurrentHashMap<K, R>()
  operator fun invoke(k: K): R {
    return m[k].toOption().fold({
                                  val r = k(f)
                                  m.putIfAbsent(k, r)
                                  r
                                }) { it }
  }
}

fun <T, R> ((T) -> R).memorize(): (T) -> R {
  return object : (T) -> R {
    private val m = MemorizedHandler<((T) -> R), MemorizeKey1<T, R>, R>(this@memorize)
    override fun invoke(t1: T): R = m(MemorizeKey1(t1))
  }
}