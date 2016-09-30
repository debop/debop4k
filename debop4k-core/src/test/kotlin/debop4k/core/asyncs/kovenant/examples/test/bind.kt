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

package debop4k.core.asyncs.kovenant.examples.test

import nl.komponents.kovenant.DirectDispatcher
import nl.komponents.kovenant.Kovenant
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.testMode
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class BindTest {

  @Before fun setup() {
    Kovenant.testMode {
      fail(it.message)
    }
  }

  @Test fun bindSuccess() {
    fun timesTwo(i: Int): Promise<Int, Exception> = Promise.of(i * 2)

    var result = 0
    Promise.of(13) bind { timesTwo(it) } success { result = it }
    assertEquals(26, result, "should chain")
  }

  @Test fun bindFail() {
    fun error(): Promise<Int, Exception> = Promise.ofFail(Exception())
    var count = 0
    Promise.of(13) bind { error() } fail { count++ }
    assertEquals(1, count, "should report a failure")
  }
}

class BindContextTest {

  val alternativeContext = Kovenant.context {
    callbackContext.dispatcher = DirectDispatcher.instance
    workerContext.dispatcher = DirectDispatcher.instance
  }

  @Test fun defaultContext() {
    fun timesTwo(i: Int): Promise<Int, Exception> = Promise.of(i * 2)

    val p = Promise.of(13) bind { timesTwo(it) }
    assertEquals(Kovenant.context, p.context, "Expected the default context")
  }

  @Test fun alternativeContext() {
    fun timesTwo(i: Int): Promise<Int, Exception> = Promise.of(i * 2)

    val p = Promise.of(13, alternativeContext) bind { timesTwo(it) }
    assertEquals(alternativeContext, p.context, "Expected the default context")
  }

  @Test fun specifiedContext() {
    fun timesTwo(i: Int): Promise<Int, Exception> = Promise.of(i * 2)

    val p = Promise.of(13).bind(alternativeContext) { timesTwo(it) }
    assertEquals(alternativeContext, p.context, "Expected the default context")
  }
}