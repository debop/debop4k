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
import nl.komponents.kovenant.functional.apply
import nl.komponents.kovenant.testMode
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.fail

class ApplyTest {

  @Before fun setup() {
    Kovenant.testMode {
      fail(it.message)
    }
  }

  @Test fun bindSuccess() {
    var result = 0
    Promise.of(13) apply Promise.of({ i: Int -> i * 2 }) success { result = it }
    assertEquals(26, result, "should chain")
  }

  @Test fun bindFail() {
    var count = 0
    Promise.of(13) apply Promise.ofFail<(Int) -> Int, Exception>(Exception()) fail { count++ }
    assertEquals(1, count, "should report a failure")
  }
}

class ApplyContextTest {

  val alternativeContext = Kovenant.context {
    callbackContext.dispatcher = DirectDispatcher.instance
    workerContext.dispatcher = DirectDispatcher.instance
  }

  @Test fun defaultContext() {
    val p = Promise.of(13) apply Promise.of({ i: Int -> i + 2 })
    assertEquals(Kovenant.context, p.context, "Expected the default context")
  }

  @Test fun alternativeContext() {
    val p = Promise.of(13, alternativeContext) apply Promise.of({ i: Int -> i + 2 })
    assertEquals(alternativeContext, p.context, "Expected the default context")
  }

  @Test fun specifiedContext() {
    val p = Promise.of(13).apply(alternativeContext, Promise.of({ i: Int -> i + 2 }))
    assertEquals(alternativeContext, p.context, "Expected the default context")
  }
}