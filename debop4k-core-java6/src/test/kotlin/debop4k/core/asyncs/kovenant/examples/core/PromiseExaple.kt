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

import debop4k.core.AbstractCoreKotlinTest
import debop4k.core.asyncs.await
import debop4k.core.asyncs.awaitAll
import debop4k.core.asyncs.kovenant.examples.fib
import nl.komponents.kovenant.*
import nl.komponents.kovenant.properties.lazyPromise
import org.junit.Test
import java.util.*

class PromiseExaple : AbstractCoreKotlinTest() {

  private val threadName: String
    get() = Thread.currentThread().name


  @Test fun testAll() {
    val promises = Array(10) { n ->
      task {
        Pair(n, fib(n))
      }
    }

    all(*promises) success {
      it.forEach { pair -> println("fib(${pair.first}) = ${pair.second}") }
    } always {
      println("All ${promises.size} promises are done.")
    }
  }

  @Test fun testAny() {
    val promises = Array(10) { n ->
      task {
        while (!Thread.currentThread().isInterrupted) {
          val luckyNumber = Random(System.currentTimeMillis() * (n + 1)).nextInt(100)
          if (luckyNumber == 7) break
        }
        "Promise number $n won!"
      }
    }

    any(*promises) success { msg ->
      println(msg)
      println()

      promises.forEachIndexed { n, p ->
        p.fail { println("promise[$n] was cancelled") }
        p.success { println("promise[$n] finished") }
      }
    }
    awaitAll(*promises)
    println("finish any")
  }

  @Test fun testReadyAll() {
    val promises = Array(50) { task { Thread.sleep(100L) } }

    print("waiting...")
    awaitAll(*promises)
    println(" done.")
  }

  @Test fun testContext() {
    val ctx = Kovenant.createContext {
      callbackContext.dispatcher { name = "cb-now" }
      workerContext.dispatcher { name = "work-new" }
    }

    task {
      println("default task $threadName")
    } success {
      println("default success $threadName")
    }

    task(ctx) {
      println("default task $threadName")
    } success {
      println("default success $threadName")
    }
  }

  @Test fun testDeferred() {
    fun handlePromise(promise: Promise<String, Exception>) {
      promise success { result -> println(result) }
      promise fail { e -> println(e) }
    }

    val deferred = deferred<String, Exception>()
    handlePromise(deferred.promise)
    deferred.resolve("Hello World")
//    deferred.reject(Exception("Hello exceptional world"))
    deferred.promise.await()
  }

  @Test fun testIsDone() {
    val p = task { Thread.sleep(1000) }
    while (!p.isDone()) {
      Thread.sleep(10)
      println("not done")
    }
    println("done!!!")
  }

  @Test fun testGet() {
    val (n, fib) = task { Pair(30, fib(30)) }.get()
    println("fib($n) = $fib")
  }


  @Test fun testLazyPromise() {
    log.debug("start program")

    expensiveResource thenApply {
      "Get [$value]"
    } success {
      log.debug(it)
    }
    expensiveResource.await()
  }


  val expensiveResource by lazyPromise {
    log.debug("init promise")
    ExpensiveResource()
  }

  class ExpensiveResource {
    val value: String = "result"
  }

}