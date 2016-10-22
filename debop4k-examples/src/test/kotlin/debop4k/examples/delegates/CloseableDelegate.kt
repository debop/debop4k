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

package debop4k.examples.delegates

import java.io.Closeable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
  val foo = Foo()
  foo.access()
  foo.close()
}

class CloseableTest : Closeable {
  override fun close() {
    println("Closing $this")
  }
}


fun <T : Closeable> closeableLazy(initializer: () -> T) = CloseableLazyVal(initializer)

class CloseableLazyVal<T : Closeable>(private val initializer: () -> T)
: ReadOnlyProperty<CloseableDelegateHost, T> {

  private var mValue: T? = null

  override fun getValue(thisRef: CloseableDelegateHost, property: KProperty<*>): T {
    var value = mValue

    if (value == null) {
      value = initializer()
      thisRef.registerCloseable(value)
    }

    return value
  }
}

interface CloseableDelegateHost : Closeable {
  fun registerCloseable(prop: Closeable)
}

class ClosableDelegateHostImpl : CloseableDelegateHost {

  val closeables = arrayListOf<Closeable>()

  override fun registerCloseable(prop: Closeable) {
    closeables.add(prop)
  }

  override fun close() = closeables.forEach { it.close() }
}

class Foo : CloseableDelegateHost by ClosableDelegateHostImpl() {
  private val stream by closeableLazy { CloseableTest() }

  fun access() {
    println("Accessing ${stream}")
  }
}