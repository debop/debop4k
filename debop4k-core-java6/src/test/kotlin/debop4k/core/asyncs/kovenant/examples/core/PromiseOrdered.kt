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

package debop4k.core.asyncs.kovenant.examples.core

import debop4k.core.asyncs.await
import debop4k.core.asyncs.awaitAll
import debop4k.core.asyncs.kovenant.examples.fib
import io.kotlintest.specs.FunSpec
import nl.komponents.kovenant.*
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.*

/**
 * @author debop sunghyouk.bae@gmail.com
 */
class PromiseOrdered : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  init {
    test("ordered promise") {
      val firstRef = AtomicReference<String>()
      val secondRef = AtomicReference<String>()

      val first = task { "hello" } success { firstRef.set(it) }
      val second = task { "world" } success { secondRef.set(it) }

      all(first, second) success {
        log.debug("${firstRef.get()}, ${secondRef.get()}")
      }
      awaitAll(first, second)
    }

    test("promise then") {
      val p: Promise<String, Exception> = task {
        fib(20)
      } then {
        "fib(20) = $it, and fib(21)=(${fib(21)})"
      } success {
        log.debug("{}", it)
      }
      p.await()
    }

    test("threads") {
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
      p.await()
    }
  }
}