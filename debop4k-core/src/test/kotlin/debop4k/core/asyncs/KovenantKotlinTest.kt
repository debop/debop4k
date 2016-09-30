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

package debop4k.core.asyncs

import debop4k.core.AbstractCoreKotlinTest
import nl.komponents.kovenant.deferred
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * KovenantKotlinTest
 * @author sunghyouk.bae@gmail.com
 */
class KovenantKotlinTest : AbstractCoreKotlinTest() {

  @Test fun deferredResolve() {
    val defer = deferred<Int, Exception>()
    defer.resolve(100)
    val promise = defer.promise

    assertThat(promise.isSuccess()).isTrue()
    assertThat(promise.get()).isEqualTo(100)
  }

  @Test fun deferredReject() {
    val defer = deferred<Int, Exception>()
    defer.reject(RuntimeException("ERROR FOR TEST"))
    val promise = defer.promise

    assertThat(promise.isFailure()).isTrue()
    assertThat(promise.getError()).isInstanceOf(RuntimeException::class.java)
  }
}