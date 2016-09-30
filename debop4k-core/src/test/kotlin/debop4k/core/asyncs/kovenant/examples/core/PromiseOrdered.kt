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

package debop4k.core.asyncs.kovenant.examples.core

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.asyncs.kovenant.examples.fib
import debop4k.core.asyncs.ready
import debop4k.core.asyncs.readyAll
import nl.komponents.kovenant.*
import org.junit.Test
import java.util.concurrent.atomic.*

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class PromiseOrdered : AbstractCoreKotlinTest() {

  @Test fun `ordered promise`() {
    val firstRef = AtomicReference<String>()
    val secondRef = AtomicReference<String>()

    val first = task { "hello" } success { firstRef.set(it) }
    val second = task { "world" } success { secondRef.set(it) }

    all(first, second) success {
      log.debug("${firstRef.get()}, ${secondRef.get()}")
    }
    readyAll(first, second)
  }

  @Test fun `promise then`() {
    val p: Promise<String, Exception> = task {
      fib(20)
    } then {
      "fib(20) = $it, and fib(21)=(${fib(21)})"
    } success {
      log.debug("{}", it)
    }
    p.ready()
  }

  @Test fun threads() {
    val context = Kovenant.context {
      callbackContext.jvmDispatcher {
        threadFactory = {
          target, dispatcherName, id ->
          Thread(target, "custom name")
        }
      }
    }
    val p = task(context) {
      fib(20)
    } then {
      "fib(20)=$it"
    } success {
      log.debug("{}", it)
    }
    p.ready()
  }
}
