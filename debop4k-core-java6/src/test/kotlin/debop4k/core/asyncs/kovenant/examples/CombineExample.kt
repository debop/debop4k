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

import io.kotlintest.specs.FunSpec
import nl.komponents.kovenant.combine.and
import nl.komponents.kovenant.task
import org.slf4j.LoggerFactory

class CombineExample : FunSpec() {

  private val log = LoggerFactory.getLogger(javaClass)

  init {
    test("combine example") {
      val fib20Promise = task { fib(20) }
      val helloWorldPromise = task { "hello world" }

      fib20Promise and helloWorldPromise success {
        val (fib, msg) = it
        log.debug("{}, fib(20)={}", msg, fib)
      }

      task { fib(20) } and task { "hello world" } success {
        log.debug("{}, fib(20)={}", it.second, it.first)
      }
    }
  }
}