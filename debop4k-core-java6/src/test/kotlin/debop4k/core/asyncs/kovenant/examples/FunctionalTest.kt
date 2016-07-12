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

package debop4k.core.asyncs.kovenant.examples

import debop4k.core.asyncs.ready
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.functional.apply
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.functional.map
import nl.komponents.kovenant.unwrap
import org.junit.Test

class FunctionalTest {

  @Test fun testApply() {
    val p = Promise.of(21) apply Promise.of({ x: Int -> x * 2 })
    p success { println(it) }
    p.ready()
  }

  @Test fun testBind() {
    val p = Promise.of(13).bind {
      divide(it, 12)
    } success {
      println("Success: $it")
    } fail {
      println("Fail: ${it.message}")
    }

    p.ready()
  }

  private fun divide(a: Int, b: Int): Promise<Int, Exception> {
    return if (a == 0 || b == 0) {
      Promise.ofFail(Exception("Cannot divide by zero $a/$b"))
    } else {
      Promise.ofSuccess(a / b)
    }
  }

  @Test fun testMap() {
    val p = Promise.of(21) map { it * 2 } success { println(it) }
    p.ready()
  }

  @Test fun testUnwrap() {
    val nested: Promise<Promise<Int, Exception>, Exception> = Promise.of(Promise.of(42))
    val promise: Promise<Int, Exception> = nested.unwrap()

    promise success { println(it) }
    promise.ready()
  }
}