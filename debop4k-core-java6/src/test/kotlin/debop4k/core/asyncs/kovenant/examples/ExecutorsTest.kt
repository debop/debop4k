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

import debop4k.core.AbstractCoreKotlinTest
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.jvm.asExecutorService
import org.junit.Test
import java.util.concurrent.*

class ExecutorsTest : AbstractCoreKotlinTest() {


  @Test fun customContext() {

    val executorService = Kovenant.context.workerContext.dispatcher.asExecutorService()

    val tasks = Array(5) { x -> Callable { Pair(25 - x, fib(25 - x)) } }.toList()

    val (n, fib) = executorService.invokeAny(tasks)
    log.debug("invokeAny: fib($n) = $fib")

    val results = executorService.invokeAll(tasks)
    results.forEach { future ->
      val (i, res) = future.get()
      log.debug("invokeAll: fib($i) = $res")
    }
  }

}
