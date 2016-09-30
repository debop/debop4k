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

package debop4k.core.examples

import debop4k.core.examples.DelegateExamples.CloseableLazyVal
import org.junit.Test
import java.io.Closeable
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * DelegateExamples
 * @author sunghyouk.bae@gmail.com
 */
class DelegateExamples {


  @Test
  fun testCloseableProperty() {
    val foo = Foo()
    foo.access()
    foo.close()
  }

  class CloseableTest : Closeable {
    override fun close() {
      println("Closing $this")
    }
  }


  class CloseableLazyVal<out T : Closeable>(private val initializer: () -> T) : ReadOnlyProperty<CloseableDelegateHost, T> {

    private val mValue: T? = null

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

  class CloseableDelegateHostImpl : CloseableDelegateHost {

    val closeables = arrayListOf<Closeable>()

    override fun registerCloseable(prop: Closeable) {
      closeables.add(prop)
    }

    override fun close() {
      closeables.forEach { it.close() }
    }
  }

  class Foo : CloseableDelegateHost by CloseableDelegateHostImpl() {

    private val stream by closeableLazy { CloseableTest() }

    fun access() {
      println("Accessing $stream")
    }
  }
}

fun <T : Closeable> closeableLazy(initializer: () -> T) = CloseableLazyVal(initializer)


class DelegatePropertiesExample {

  class Example {
    var p: String by Delegates.observable("", { prop, old, new -> println("property=${prop.name}, old=$old --> new=$new") })
  }

  @Test
  fun observableProperty() {
    val e = Example()
    e.p = "abc"
    e.p = "가나다"
  }


  interface Base {
    fun print()
  }

  class BaseImpl(val x: Int) : Base {
    override fun print() {
      print(x)
    }
  }

  class Derived(b: Base) : Base by b

  @Test
  fun classDelegates() {
    val b = BaseImpl(10)
    Derived(b).print()
  }

}