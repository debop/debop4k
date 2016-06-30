/*
 * Copyright (c) 2016. sunghyouk.bae@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package debop4k.reactive

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import rx.Completable
import rx.Single
import java.util.*
import java.util.concurrent.Callable


class CompletableTest : AbstractReactiveTest() {

  @Test
  fun testCreateFromAction() {
    var count = 0
    val c1: Completable = { count++ }.toCompletable()
    assertThat(c1).isNotNull()
    c1.subscribe()
    assertThat(count).isEqualTo(1)

    count = 0
    val c2: Completable = completableOf { count++ }
    assertThat(c2).isNotNull()
    c2.subscribe()
    assertThat(count).isEqualTo(1)
  }

  @Test fun testCreateFromCallable() {
    var count = 0
    val c1 = Callable { count++ }.toCompletable()
    assertThat(c1).isNotNull()
    c1.subscribe()
    assertThat(count).isEqualTo(1)
  }

  // 완료되었으므로, 더 이상 emit 되는 데이터는 없다.
  @Test(expected = NoSuchElementException::class)
  fun testCreateFromFuture() {
    val c1 = 1.toSingletonObservable().toBlocking().toFuture().toCompletable()
    assertThat(c1).isNotNull()
    c1.toObservable<Int>().toBlocking().first()
  }

  // 완료되었으므로, 더 이상 emit 되는 데이터는 없다.
  @Test(expected = NoSuchElementException::class)
  fun testCreateFromSingle() {
    val c1 = Single.just("Hello World!").toCompletable()
    assertThat(c1).isNotNull()
    c1.toObservable<String>().toBlocking().first()
  }
}